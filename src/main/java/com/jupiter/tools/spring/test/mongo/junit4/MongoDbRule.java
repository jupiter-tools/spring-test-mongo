package com.jupiter.tools.spring.test.mongo.junit4;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.jupiter.tools.spring.test.mongo.annotation.ExpectedMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.ExportMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo;
import com.jupiter.tools.spring.test.mongo.internal.MongoDbTest;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Created by Korovin A. on 21.01.2018.
 * <p>
 * {@link MongoDbRule} is a rule to write integration tests
 * of applications with a MongoDb persistence layer, in JUnit4.
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRule implements TestRule {

    private MongoTemplate mongoTemplate;
    private Supplier<MongoTemplate> mongoTemplateProvider;
    private String initialDataSetFilePath;

    /**
     * Init test rule, required MongoTemplate provider
     * for lazy obtaining mongo template.
     * <p>
     * It is necessary because the priority of the test rule is higher
     * than the application context.
     *
     * @param templateProvider mongoTemplate provider
     */
    public MongoDbRule(Supplier<MongoTemplate> templateProvider) {
        mongoTemplateProvider = templateProvider;
    }

    /**
     * Test rule handler
     */
    @Override
    public Statement apply(Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                mongoTemplate = mongoTemplateProvider.get();
                Guard.check(mongoTemplate != null, InternalException.class, MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY);

                beforeInvocation(description);
                base.evaluate();
                afterInvocation(description);
            }
        };
    }

    /**
     * Clean a database before the test execution if it needed,
     * then populate data-set from the file.
     */
    private void beforeInvocation(Description description) {

        MongoDataSet mongoDataSet = description.getAnnotation(MongoDataSet.class);

        if (mongoDataSet == null) { return; }

        // clean before
        if (mongoDataSet.cleanBefore()) {
            cleanDataBase();
        }

        // populate before test invocation
        if (!mongoDataSet.value().isEmpty()) {
            new MongoDbTest(mongoTemplate).importFrom(mongoDataSet.value());
        }

        // processing read-only data set
        if (isReadOnlyDataSet(description)) {
            File tempFile;
            try {
                tempFile = File.createTempFile("mongo-test-", "-readonly");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while creating temp file to store immutable dataset", e);
            }
            tempFile.deleteOnExit();
            new MongoDbTest(mongoTemplate).exportTo(tempFile.getAbsolutePath());
            this.initialDataSetFilePath = tempFile.getAbsolutePath();
        }
    }


    /**
     * Clean a database after test execution,
     * if it required by the {@link MongoDataSet} annotation.
     */
    private void afterInvocation(Description description) {
        expectedDataSet(description);
        exportDataSet(description);
        cleanAfter(description);
    }

    private void expectedDataSet(Description description) {

        if (isReadOnlyDataSet(description)) {
            try {
                new MongoDbTest(mongoTemplate).expect(initialDataSetFilePath);
            } catch (Error e) {
                throw new RuntimeException("Expected ReadOnly dataset, but found some modifications:", e);
            }
            return;
        }

        ExpectedMongoDataSet expectedMongoDataSet = description.getAnnotation(ExpectedMongoDataSet.class);
        if (expectedMongoDataSet == null) {
            return;
        }
        new MongoDbTest(mongoTemplate).expect(expectedMongoDataSet.value());
    }

    private void exportDataSet(Description description) {

        ExportMongoDataSet exportMongoDataSet = description.getAnnotation(ExportMongoDataSet.class);

        if (exportMongoDataSet == null) {
            return;
        }

        new MongoDbTest(mongoTemplate).exportTo(exportMongoDataSet.outputFile());
    }

    private void cleanAfter(Description description) {

        MongoDataSet mongoDataSet = description.getAnnotation(MongoDataSet.class);

        if (mongoDataSet != null && mongoDataSet.cleanAfter()) {
            cleanDataBase();
        }
    }

    /**
     * Clean all collections in mongodb, obtained by the current MongoTemplate
     */
    private void cleanDataBase() {
        mongoTemplate.getCollectionNames()
                     .forEach(mongoTemplate::dropCollection);
    }

    private boolean isReadOnlyDataSet(Description description) {
        MongoDataSet mongoDataSet = description.getAnnotation(MongoDataSet.class);
        return mongoDataSet != null && mongoDataSet.readOnly();
    }
}