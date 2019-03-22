package com.jupiter.tools.spring.test.mongo.internal.importdata;

import com.jupiter.tools.spring.test.mongo.internal.DataSet;

/**
 * Import data to MongoDb from {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
public interface DataSetImport {

    /**
     * Import {@link DataSet} to the MongoDb
     *
     * @param dataSet source data set
     */
    void importFrom(DataSet dataSet);
}
