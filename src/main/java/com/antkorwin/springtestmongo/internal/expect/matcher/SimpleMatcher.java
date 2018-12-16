package com.antkorwin.springtestmongo.internal.expect.matcher;


/**
 * Match two objects of simple types.
 *
 * @author Korovin Anatoliy
 */
class SimpleMatcher implements ValueMatcher {

    @Override
    public boolean match(Object originValue, Object comparableValue) {
        return originValue.equals(comparableValue);
    }

    @Override
    public boolean isNecessary(Object value) {
        return true;
    }
}