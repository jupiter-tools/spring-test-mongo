package com.antkorwin.springtestmongo.internal.expect.matcher;



public class MatcherFactory {

    private final RegexMatcher regexMatcher;
    private final SimpleMatcher simpleMatcher;

    public MatcherFactory() {
        regexMatcher = new RegexMatcher();
        simpleMatcher = new SimpleMatcher();
    }

    public ValueMatcher getMatcher(Object value) {
        if (regexMatcher.isNecessary(value)) {
            return regexMatcher;
        } else {
            return simpleMatcher;
        }
    }
}