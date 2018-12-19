package com.antkorwin.springtestmongo.internal.expect.match;

import java.math.BigInteger;

/**
 * Created on 19.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchBigInteger implements MatchData {

    private final MatchObjects matchObjects;

    public MatchBigInteger() {
        this.matchObjects = new MatchObjects();
    }

    @Override
    public boolean match(Object original, Object expected) {

        if (original instanceof BigInteger &&
            expected instanceof BigInteger) {

            BigInteger originalValue = (BigInteger) original;
            BigInteger expectedValue = (BigInteger) expected;
            return originalValue.equals(expectedValue);
        }

        if ((original instanceof Long || original instanceof Integer) &&
            expected instanceof BigInteger) {

            BigInteger originalValue = BigInteger.valueOf(((Number) original).longValue());
            BigInteger expectedValue = (BigInteger) expected;
            return originalValue.equals(expectedValue);
        }

        if (original instanceof BigInteger &&
            (expected instanceof Long || expected instanceof Integer)) {

            BigInteger originalValue = (BigInteger) original;
            BigInteger expectedValue = BigInteger.valueOf(((Number) expected).longValue());
            return originalValue.equals(expectedValue);
        }

        return matchObjects.match(original, expected);
    }
}
