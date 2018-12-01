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

import static com.antkorwin.springtestmongo.errorinfo.RiderErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * Created by Korovin A. on 21.01.2018.
 *
 * MongoDB-Rider Test Rule for integration testing application with MongoDb persistence layer.
 *
 * Give effect to:
 * - processing MongoDataSet annotation
 * - populate test data beforeInvocation run test method
 * - clean mongo db after/beforeInvocation test run
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class MongoDbRiderRule implements TestRule {

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
    public MongoDbRiderRule(Supplier<MongoTemplate> templateProvider) {
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
     * internal logic that runs before each test
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
     * internal logic that runs after each test
     */
    private void afterInvocation(MongoDataSet mongoDataSet){
        if (mongoDataSet != null && mongoDataSet.cleanAfter()) {
            cleanDataBase();
        }
    }


    /**
     * Clean all mongodb collection, obtained by current mongoTemplate
     */
    private void cleanDataBase() {
        mongoTemplate.getCollectionNames()
                     .forEach(mongoTemplate::dropCollection);
    }
}