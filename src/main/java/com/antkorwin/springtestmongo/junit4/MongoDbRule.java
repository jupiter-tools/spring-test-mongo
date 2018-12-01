package com.antkorwin.springtestmongo.junit4;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.MongoPopulator;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.function.Supplier;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * Created by Korovin A. on 21.01.2018.
 *
 * {@link MongoDbRule} is a rule to write integration tests
 * of applications with a MongoDb persistence layer, in JUnit4.
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRule implements TestRule {

    private MongoTemplate mongoTemplate;
    private Supplier<MongoTemplate> mongoTemplateProvider;

    /**
     * Init test rule, required MongoTemplate provider
     * for lazy obtaining mongo template.
     *
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
                Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
                MongoDataSet mongoDataSet = description.getAnnotation(MongoDataSet.class);

                beforeInvocation(mongoDataSet);
                base.evaluate();
                afterInvocation(mongoDataSet);
            }
        };
    }

    /**
     * Clean a database before the test execution if it needed,
     * then populate data-set from the file.
     */
    private void beforeInvocation(MongoDataSet mongoDataSet) {

        if(mongoDataSet== null) return;

        // clean before
        if (mongoDataSet.cleanBefore()) {
            cleanDataBase();
        }

        // populate before test invocation
        if (!mongoDataSet.value().isEmpty()) {
            MongoPopulator.populate(mongoTemplate, mongoDataSet.value());
        }
    }


    /**
     * Clean a database after test execution,
     * if it required by the {@link MongoDataSet} annotation.
     */
    private void afterInvocation(MongoDataSet mongoDataSet){
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
}