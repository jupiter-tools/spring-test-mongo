package com.antkorwin.springtestmongo.internal;

import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDbTest {

    private final MongoTemplate mongoTemplate;

    public MongoDbTest(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // mongo -> file
    public void exportTo(String fileName) {
        new ExportFile(new JsonExport(new MongoDataExport(this.mongoTemplate))).write(fileName);
    }

    // file -> mongo
    public void importFrom(String fileName) {
        new MongoDataImport(mongoTemplate).importFrom(new JsonImport(new ImportFile(fileName)));
    }
}