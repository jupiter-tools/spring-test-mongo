package com.antkorwin.springtestmongo.internal.expect.graph;

import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * Graph is a model to calculate the matching of data in
 * mongodb and patterns in provided data sets, for one
 * type of document collection.
 *
 * @author Korovin Anatoliy
 */
public interface Graph {

    /**
     * convert graph to matrix
     *
     * @return matrix view of graph
     */
    boolean[][] calculate();

    /**
     * @return count of data records from mongodb
     */
    int dataCount();

    /**
     * @return count of patterns from data set file
     */
    int patternCount();

    /**
     * retrieve a data record by the index in graph
     *
     * @param index position of needed data record
     * @return data record from mongodb by the index in graph
     */
    Map<String, Object> getDataRecord(int index);

    /**
     * retrieve the pattern from data set file
     *
     * @param index position of this pattern in the graph
     * @return pattern object from a data set file by the index from the graph
     */
    Map<String, Object> getPattern(int index);

    /**
     * @return name of the mongodb document associated to this graph
     */
    String getDocumentName();
}
