package com.jupiter.tools.spring.test.mongo.internal.expect.match.simple;

import com.jupiter.tools.spring.test.mongo.internal.expect.match.MatchData;
import org.apache.commons.math3.util.Precision;

/**
 * Created on 01.04.2019.
 *
 * @author Korovin Anatoliy
 */
public class MatchDouble implements MatchData {

    private static final double THRESHOLD = 1E-14;

    @Override
    public boolean match(Object original, Object expected) {
        Number originalNumber = (Number) original;
        Number expectedNumber = (Number) expected;
        return Precision.equals(originalNumber.doubleValue(), expectedNumber.doubleValue(), THRESHOLD);
    }
}
