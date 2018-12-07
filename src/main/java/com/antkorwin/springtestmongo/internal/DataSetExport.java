package com.antkorwin.springtestmongo.internal;

/**
 * Export data from MongoDb --> {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
interface DataSetExport {

    /**
     * export data set from MongoDb to the {@link DataSet} instance
     *
     * @return {@link DataSet}
     */
    DataSet export();
}
