package com.antkorwin.springtestmongo.junit5;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.MongoPopulator;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.junit.jupiter.api.extension.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * Created on 30.11.2018.
 *
 * Junit5 extension:
 * - starting mongodb test container
 * - processing {@link MongoDataSet} annotation in tests
 *
 * @author Korovin Anatoliy
 */
public class MongoDbExtension implements Extension, BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    private MongoTemplate mongoTemplate;

    /**
     * check existence of the {@link MongoTemplate} in the context
     * @param context junit5 extension context
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        mongoTemplate = SpringExtension.getApplicationContext(context)
                                       .getBean(MongoTemplate.class);

        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
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
            MongoPopulator.populate(mongoTemplate, mongoDataSet.value());
        }
    }

    /**
     * Clean a database after test execution,
     * if it required by the {@link MongoDataSet} annotation.
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        MongoDataSet mongoDataSet = getAnnotationFromCurrentMethod(context);

        if (mongoDataSet != null && mongoDataSet.cleanAfter()) {
            cleanDataBase();
        }
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
}
