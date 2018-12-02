package com.antkorwin.springtestmongo.junit5;

import org.junit.jupiter.api.extension.Extension;
import org.springframework.util.SocketUtils;
import org.testcontainers.containers.GenericContainer;

/**
 * Created on 02.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbTcExtension implements Extension {

    private static final Integer MONGO_PORT = 27017;

    static {
        System.out.println("Start MongoDb testcontainers extension...\n");

        GenericContainer mongo = new GenericContainer("mongo:latest")
                .withExposedPorts(MONGO_PORT);

        mongo.start();

        System.setProperty("spring.data.mongodb.host", mongo.getContainerIpAddress());
        System.setProperty("spring.data.mongodb.port", mongo.getMappedPort(MONGO_PORT).toString());
    }

}
