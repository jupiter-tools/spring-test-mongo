package com.jupiter.tools.spring.test.mongo.idea.skip.test;

import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation provides an ability to skip
 * the junit5 test when this test running from IDEA
 */
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@DisabledIfSystemProperty(named = "sun.java.command",
                          matches = "com.intellij.rt.execution.*")
public @interface IdeaSkipTest {
}
