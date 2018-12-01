package com.antkorwin.springtestmongo.annotation;

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
     * Path to file with dataSet for populating
     */
    String value() default "";

    /**
     * Clean MongoDB data before run test
     */
    boolean cleanBefore() default false;

    /**
     * Clean MongoDB data after run test
     */
    boolean cleanAfter() default false;
}