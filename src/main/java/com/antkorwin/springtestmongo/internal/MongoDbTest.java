package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.internal.expect.MatchDataSets;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * MongoDb test tools,
 * to run importing and exporting MongoDb's data.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbTest {

    private final MongoTemplate mongoTemplate;

    public MongoDbTest(MongoTemplate mongoTemplate) {
        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
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

    public void expect(String fileName) {
        DataSet dataSet = new JsonImport(new ImportFile(fileName));
        DataSet mongoData = new MongoDataExport(mongoTemplate);
        new MatchDataSets(mongoData, dataSet).check();
    }
}