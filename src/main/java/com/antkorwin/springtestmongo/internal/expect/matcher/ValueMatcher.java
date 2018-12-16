package com.antkorwin.springtestmongo.internal.expect.matcher;


/**
 * Algorithm to match two objects and check equals.
 *
 * @author Korovin Anatoliy
 */
public interface ValueMatcher {

    /**
     * Match two objects
     *
     * @param originValue     value of original object
     * @param comparableValue value of comparable(expected) object
     *
     * @return tru is this objects are same and false if not.
     */
    boolean match(Object originValue, Object comparableValue);

    /**
     * Check necessary of applied this matcher to current value of expected object.
     */
    boolean isNecessary(Object value);
}