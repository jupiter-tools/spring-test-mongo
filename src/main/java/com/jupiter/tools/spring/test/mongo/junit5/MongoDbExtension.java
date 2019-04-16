package com.jupiter.tools.spring.test.mongo.junit5;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.jupiter.tools.spring.test.mongo.annotation.ExpectedMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.ExportMongoDataSet;
import com.jupiter.tools.spring.test.mongo.annotation.MongoDataSet;
import com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo;
import com.jupiter.tools.spring.test.mongo.internal.MongoDbTest;
import org.junit.jupiter.api.extension.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

/**
 * Created on 30.11.2018.
 * <p>
 * Junit5 extension:
 * - starting mongodb test container
 * - processing {@link MongoDataSet} annotation in tests
 *
 * @author Korovin Anatoliy
 */
public class MongoDbExtension implements Extension, BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    private MongoTemplate mongoTemplate;

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create("com", "jupiter-tools", "spring-test-mongo", "read-only-dataset");

    /**
     * check existence of the {@link MongoTemplate} in the context
     *
     * @param context junit5 extension context
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        mongoTemplate = SpringExtension.getApplicationContext(context)
                                       .getBean(MongoTemplate.class);

        Guard.check(mongoTemplate != null, InternalException.class, MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY);
    }

    /**
     * Clean a database before the test execution if it needed,
     * then populate data-set from the file.
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        MongoDataSet mongoDataSet = getAnnotationFromCurrentMethod(context);

        if (mongoDataSet == null) {
            return;
        }

        if (mongoDataSet.cleanBefore()) {
            cleanDataBase();
        }

        // populate before test invocation
        if (!mongoDataSet.value().isEmpty()) {
            new MongoDbTest(mongoTemplate).importFrom(mongoDataSet.value());
        }

        // if read-only data set than we need to save a mongo state before run test in temp file
        if (isReadOnlyDataSet(context)) {
            File tempFile = File.createTempFile("mongo-test-", "-readonly");
            tempFile.deleteOnExit();
            new MongoDbTest(mongoTemplate).exportTo(tempFile.getAbsolutePath());
            context.getStore(NAMESPACE).put("beforeDataSetFile", tempFile.getAbsolutePath());
        }
    }

    /**
     * Clean a database after test execution,
     * if it required by the {@link MongoDataSet} annotation.
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        expectedDataSet(context);
        exportDataSet(context);
        cleanAfter(context);
    }

    private void expectedDataSet(ExtensionContext context) {

        if (isReadOnlyDataSet(context)) {
            String filePath = (String) context.getStore(NAMESPACE)
                                              .get("beforeDataSetFile");
            try {
                new MongoDbTest(mongoTemplate).expect(filePath);
            } catch (Error e) {
                throw new RuntimeException("Expected read only dataset",e);
            }
            return;
        }

        ExpectedMongoDataSet expectedMongoDataSet = context.getRequiredTestMethod()
                                                           .getAnnotation(ExpectedMongoDataSet.class);

        if (expectedMongoDataSet == null) {
            return;
        }

        new MongoDbTest(mongoTemplate).expect(expectedMongoDataSet.value());
    }

    private void cleanAfter(ExtensionContext context) {
        MongoDataSet mongoDataSet = getAnnotationFromCurrentMethod(context);
        if (mongoDataSet != null && mongoDataSet.cleanAfter()) {
            cleanDataBase();
        }
    }

    private void exportDataSet(ExtensionContext context) {
        ExportMongoDataSet exportMongoDataSet = context.getRequiredTestMethod()
                                                       .getAnnotation(ExportMongoDataSet.class);

        if (exportMongoDataSet == null) {
            return;
        }

        new MongoDbTest(mongoTemplate).exportTo(exportMongoDataSet.outputFile());
    }

    private MongoDataSet getAnnotationFromCurrentMethod(ExtensionContext context) {
        return context.getRequiredTestMethod()
                      .getAnnotation(MongoDataSet.class);
    }

    /**
     * Clean all mongodb collections obtained by the current mongoTemplate
     */
    private void cleanDataBase() {
        mongoTemplate.getCollectionNames()
                     .forEach(mongoTemplate::dropCollection);
    }

    private boolean isReadOnlyDataSet(ExtensionContext context) {
        MongoDataSet mongoDataSet = getAnnotationFromCurrentMethod(context);
        return mongoDataSet != null && mongoDataSet.readOnly();
    }
}
