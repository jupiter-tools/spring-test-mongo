package com.antkorwin.springtestmongo.internal.expect.match.simple;

import com.antkorwin.springtestmongo.internal.expect.match.MatchData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 19.12.2018.
 * <p>
 * Match one {@link Number} value to another {@link Number} value.
 *
 * @author Korovin Anatoliy
 */
public class MatchNumber implements MatchData {

    private final MatchObjects matchObjects;
    private final MatchLong matchLong;
    private final ObjectMapper objectMapper;
    private final MatchBigInteger matchBigInteger;

    public MatchNumber() {
        this.matchObjects = new MatchObjects();
        this.matchLong = new MatchLong();
        this.objectMapper = new ObjectMapper();
        this.matchBigInteger = new MatchBigInteger();
    }

    @Override
    public boolean match(Object original, Object expected) {

        JsonParser.NumberType originalType = objectMapper.valueToTree(original).numberType();
        JsonParser.NumberType expectedType = objectMapper.valueToTree(expected).numberType();

        if (originalType == JsonParser.NumberType.INT ||
            originalType == JsonParser.NumberType.LONG) {

            return matchLong.match(original, expected);
        }

        if (originalType == JsonParser.NumberType.BIG_INTEGER ||
            expectedType == JsonParser.NumberType.BIG_INTEGER) {

            return matchBigInteger.match(original, expected);
        }

        return matchObjects.match(original, expected);
    }
}
