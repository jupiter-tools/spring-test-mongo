package com.antkorwin.springtestmongo.internal.expect;

import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
public interface Graph {

    boolean[][] calculate();

    int dataCount();

    int patternCount();

    Map<String,Object> getDataRecord(int index);

    Map<String,Object> getPattern(int index);

    String getDocumentName();
}
