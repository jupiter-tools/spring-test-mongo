package com.jupiter.tools.spring.test.mongo.junit4;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


public class IdeaTestSkipRule implements TestRule {

    private boolean isRunInIdea() {
        String property = System.getProperty("sun.java.command");
        return property != null && property.matches("com.intellij.rt.execution.*");
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (isRunInIdea()) {
                    throw new AssumptionViolatedException("Skipping test in IDEA!");
                } else {
                    base.evaluate();
                }
            }
        };
    }
}
