package com.antkorwin.springtestmongo.internal.expect;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
public interface Graph {

    boolean[][] calculate();

    int dataCount();

    int patternCount();
}
