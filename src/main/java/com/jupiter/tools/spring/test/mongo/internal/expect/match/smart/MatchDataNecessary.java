package com.jupiter.tools.spring.test.mongo.internal.expect.match.smart;

/**
 * Created on 22.12.2018.
 *
 * Check the necessity of applying a current DataMatcher to the expected value
 *
 * @author Korovin Anatoliy
 */
public interface MatchDataNecessary {

    boolean isNecessary(Object expected);
}
