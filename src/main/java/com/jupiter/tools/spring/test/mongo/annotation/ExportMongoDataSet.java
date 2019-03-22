package com.jupiter.tools.spring.test.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 03.12.2018.
 *
 * You can use this annotation in tests to
 * generate files with data sets.
 * All document collections from current database
 * will be store in export file, after test execution.
 *
 * @author Korovin Anatoliy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExportMongoDataSet {

    /**
     * @return path to the export file
     */
    String outputFile();
}
