package com.antkorwin.springtestmongo.junit4;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

/**
 * Created by Korovin A. on 19.01.2018.
 * <p>
 * Runs whole application context and start the MongoDB test container.
 * Also initialize the {@link MongoDbRule} to write integration
 * tests where you can use the {@link com.antkorwin.springtestmongo.annotation.MongoDataSet} annotation.
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseMongoIT {

    private static final Integer MONGO_PORT = 27017;

    static {
        System.out.println("Start MongoDb testcontainers extension...\n");

        GenericContainer mongo = new GenericContainer("mongo:latest")
                .withExposedPorts(MONGO_PORT);

        mongo.start();

        System.setProperty("spring.data.mongodb.host", mongo.getContainerIpAddress());
        System.setProperty("spring.data.mongodb.port", mongo.getMappedPort(MONGO_PORT).toString());
    }

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Rule
    public MongoDbRule mongoDbRule = new MongoDbRule(() -> mongoTemplate);
}