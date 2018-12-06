package com.antkorwin.springtestmongo.internal;


interface DataSetImport {

    /**
     * data --> mongo
     * @param dataSet source data set
     */
    void importFrom(DataSet dataSet);
}
