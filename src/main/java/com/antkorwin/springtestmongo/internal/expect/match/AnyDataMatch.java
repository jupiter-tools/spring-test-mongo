package com.antkorwin.springtestmongo.internal.expect.match;

import java.util.List;
import java.util.Map;

import com.antkorwin.springtestmongo.internal.expect.ComplexityDataTypes;
import com.antkorwin.springtestmongo.internal.expect.matcher.MatcherFactory;

/**
 * Created on 18.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class AnyDataMatch implements DataMatch {

    private final ComplexityDataTypes complexityDataTypes;

    public AnyDataMatch() {
        complexityDataTypes = new ComplexityDataTypes();
    }

    @Override
    public boolean match(Object original, Object expected) {
        if (original == null) {
            return expected == null;
        }

        if (expected == null) {
            return true;
        }

        if (!complexityDataTypes.isComplexType(original)) {
            if (!original.getClass().isAssignableFrom(expected.getClass())) {
                return false;
            }
        }

        if (original instanceof Map) {
            return new MatchMap().match(original, expected);
        }

        if (original instanceof List) {
            return new MatchList().match(original, expected);
        }

        return new MatcherFactory().getMatcher(expected).match(original, expected);
    }
}
