package com.antkorwin.springtestmongo.internal.expect.match;

/**
 * Created on 19.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchString implements MatchData {

    private final MatchRegExp matchRegExp;

    MatchString() {
        this.matchRegExp = new MatchRegExp();
    }

    @Override
    public boolean match(Object original, Object expected) {
        if (matchRegExp.isNecessary(expected)) {
            return matchRegExp.match(original, expected);
        }
        else {
            return expected.equals(original);
        }
    }
}
