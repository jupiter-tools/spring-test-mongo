package com.antkorwin.springtestmongo.internal.expect.matcher;


/**
 * Factory for obtaining a {@link ValueMatcher} to match the given value
 *
 * @author Korovin Anatoliy
 */
public class MatcherFactory {

    private final RegexMatcher regexMatcher;
    private final SimpleMatcher simpleMatcher;

    public MatcherFactory() {
        regexMatcher = new RegexMatcher();
        simpleMatcher = new SimpleMatcher();
    }

    /**
     * get matcher by the value of expected object
     *
     * @param value the value of expected object
     *
     * @return matcher for this value
     */
    public ValueMatcher getMatcher(Object value) {
        if (regexMatcher.isNecessary(value)) {
            return regexMatcher;
        } else {
            return simpleMatcher;
        }
    }
}