package com.jupiter.tools.spring.test.mongo.junit5;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.junit.jupiter.api.extension.Extension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.images.builder.Transferable;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created on 02.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbTcExtension implements Extension {

    private static final Integer MONGO_PORT = 27017;

    static {
        System.out.println("Start MongoDb testcontainers extension...\n");

        MongoDBContainer mongo = new MongoDBContainer("mongo:6.0.2");

        mongo.start();

        System.setProperty("spring.data.mongodb.host", mongo.getHost());
        System.setProperty("spring.data.mongodb.port", mongo.getMappedPort(MONGO_PORT).toString());
        System.setProperty("spring.data.mongodb.replicaSet", "docker-rs");
    }

    private static void executeInContainer(GenericContainer container, String command) {
        byte[] contentBytes = command.getBytes(UTF_8);
        String shellFilePath = "/etc/mongo-" + UUID.randomUUID();

        try {
            container.copyFileToContainer(Transferable.of(contentBytes), shellFilePath);
            container.execInContainer("/bin/bash", shellFilePath);
        } catch (Exception exc) {
            throw new InternalException(exc);
        }
    }
}
