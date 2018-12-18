package com.antkorwin.springtestmongo.internal.expect.match;

/**
 * Created on 18.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public interface DataMatch {

    boolean match(Object original, Object expected);
}
