package com.antkorwin.springtestmongo.internal.expect.matcher;


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