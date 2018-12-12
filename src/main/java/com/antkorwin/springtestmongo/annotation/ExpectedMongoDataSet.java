package com.antkorwin.springtestmongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You can use this annotation in tests
 * to check a state of the mongodb after the test execution.
 *
 * After test execution, all document collections will check
 * to match to expected data set in the selected file.
 *
 * @author Korovin Anatoliy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExpectedMongoDataSet {

    /**
     * Path to the file with an expected data set (after test execution)
     *
     * @return path to file with an expected data set
     */
    String value();
}
