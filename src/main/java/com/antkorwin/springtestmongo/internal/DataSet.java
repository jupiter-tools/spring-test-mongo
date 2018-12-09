package com.antkorwin.springtestmongo.internal;

import java.util.List;
import java.util.Map;

/**
 * Read a map from some kind of source
 *
 * @author Korovin Anatoliy
 */
public interface DataSet {

    /**
     * Read the data set from some kind of source
     *
     * @return map with the data set,
     * map looks like this:
     * "org.package....FirstDocument" : [FirstDocument doc1, FirstDocument doc2, FirstDocument doc3],
     * "org.package....SecondDocument" : [SecondDocument Doc1, SecondDocument Doc2, SecondDocument Doc3]
     */
    Map<String, List<Map<String, Object>>> read();
}