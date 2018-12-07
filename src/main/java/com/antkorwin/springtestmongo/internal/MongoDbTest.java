package com.antkorwin.springtestmongo.internal;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDb test tools,
 * to run importing and exporting MongoDb's data.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbTest {

    private final MongoTemplate mongoTemplate;

    public MongoDbTest(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * export data from MongoBd to the File
     *
     * @param fileName path to the export file
     */
    public void exportTo(String fileName) {
        new ExportFile(new JsonExport(new MongoDataExport(this.mongoTemplate))).write(fileName);
    }

    /**
     * import data from file to MongoDb
     *
     * @param fileName path to file with the data set.
     */
    public void importFrom(String fileName) {
        new MongoDataImport(mongoTemplate).importFrom(new JsonImport(new ImportFile(fileName)));
    }
}