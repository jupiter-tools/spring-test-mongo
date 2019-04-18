package com.jupiter.tools.spring.test.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Korovin A. on 21.01.2018.
 *
 * This annotation used to specify the path
 * of a data-set file and other options.
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MongoDataSet {

    /**
     * Path to a file with the data-set for populating
     */
    String value() default "";

    /**
     * Clean a MongoDB database before the test execution
     */
    boolean cleanBefore() default false;

    /**
     * Clean a MongoDB database after the test execution
     */
    boolean cleanAfter() default false;

    /**
     * expected unmodifiable data set in this test case
     */
    boolean readOnly() default false;
}