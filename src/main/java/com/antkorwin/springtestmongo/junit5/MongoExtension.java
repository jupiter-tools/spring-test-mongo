package com.antkorwin.springtestmongo.junit5;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.MongoPopulator;
import com.antkorwin.springtestmongo.annotation.MongoDataSet;
import org.junit.jupiter.api.extension.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

import static com.antkorwin.springtestmongo.errorinfo.RiderErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * Created on 30.11.2018.
 *
 * @author Korovin Anatoliy
 */
public class MongoExtension implements Extension, BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    private static final Integer MONGO_PORT = 27017;

    static {
        System.out.println("Start MongoDb testcontainers extension...\n");

        GenericContainer mongo = new GenericContainer("mongo:latest")
                .withExposedPorts(MONGO_PORT);

        mongo.start();

        System.setProperty("spring.data.mongodb.host", mongo.getContainerIpAddress());
        System.setProperty("spring.data.mongodb.port", mongo.getMappedPort(MONGO_PORT).toString());
    }

    private MongoTemplate mongoTemplate;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        mongoTemplate = SpringExtension.getApplicationContext(context)
                                       .getBean(MongoTemplate.class);

        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
    }


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
