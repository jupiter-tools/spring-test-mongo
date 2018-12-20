package com.antkorwin.springtestmongo.internal.expect.match;

/**
 * Created on 19.12.2018.
 *
 * Match two {@link Long} values,
 * or {@link Long} with {@link Integer} values
 *
 * @author Korovin Anatoliy
 */
public class MatchLong implements MatchData {

    @Override
    public boolean match(Object original, Object expected) {
        Number originalNumber = (Number) original;
        Number expectedNumber = (Number) expected;
        return originalNumber.longValue() == expectedNumber.longValue();
    }

}
