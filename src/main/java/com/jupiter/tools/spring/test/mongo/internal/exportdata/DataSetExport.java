package com.jupiter.tools.spring.test.mongo.internal.exportdata;

import com.jupiter.tools.spring.test.mongo.internal.DataSet;

/**
 * Export data from MongoDb --> {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
public interface DataSetExport {

    /**
     * export data set from MongoDb to the {@link DataSet} instance
     *
     * @return {@link DataSet}
     */
    DataSet export();
}
