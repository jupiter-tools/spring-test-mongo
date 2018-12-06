package com.antkorwin.springtestmongo.internal;

/**
 * Import data to MongoDb from {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
interface DataSetImport {

    /**
     * Import {@link DataSet} to the MongoDb
     *
     * @param dataSet source data set
     */
    void importFrom(DataSet dataSet);
}
