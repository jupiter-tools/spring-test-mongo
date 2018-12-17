package com.antkorwin.springtestmongo.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.ActiveProfiles;

/**
 * Created on 02.12.2018.
 *
 * Runs single MongoDb testcontainer before all tests.
 *
 * @author Korovin Anatoliy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Tag("antkorwin-mongodb-testcontainers")
@ExtendWith(MongoDbTcExtension.class)
@ActiveProfiles("antkorwin-mongodb-testcontainers")
public @interface EnableMongoDbTestContainers {
}
