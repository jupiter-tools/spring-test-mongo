package com.antkorwin.springtestmongo.internal.expect.matcher;


public interface ValueMatcher {

    boolean match(Object originValue, Object comparableValue);

    boolean isNecessary(Object value);
}