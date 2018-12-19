package com.antkorwin.springtestmongo.internal.expect.match;

/**
 * Created on 19.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchObjects implements MatchData {

    @Override
    public boolean match(Object original, Object expected) {
        return expected.equals(original);
    }
}
