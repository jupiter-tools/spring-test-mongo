package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.antkorwin.springtestmongo.internal.expect.MatchDataSets;
import com.antkorwin.springtestmongo.internal.expect.dynamic.value.DynamicDataSet;
import com.antkorwin.springtestmongo.internal.expect.dynamic.value.DynamicValue;
import com.antkorwin.springtestmongo.internal.expect.dynamic.value.GroovyDynamicValue;
import com.antkorwin.springtestmongo.internal.exportdata.ExportFile;
import com.antkorwin.springtestmongo.internal.exportdata.JsonExport;
import com.antkorwin.springtestmongo.internal.exportdata.MongoDataExport;
import com.antkorwin.springtestmongo.internal.importdata.ImportFile;
import com.antkorwin.springtestmongo.internal.importdata.JsonImport;
import com.antkorwin.springtestmongo.internal.importdata.MongoDataImport;
import com.google.common.collect.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

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

    /**
     * Check data in the mongodb,
     * try to match data from the DB to loaded from file data set.
     *
     * @param fileName path to file with an expected data set
     */
    public void expect(String fileName) {
        Set<DynamicValue> dynamicEvaluators = Sets.newHashSet(new GroovyDynamicValue());
        DataSet dataSet = new DynamicDataSet(new JsonImport(new ImportFile(fileName)), dynamicEvaluators);
        DataSet mongoData = new MongoDataExport(mongoTemplate);
        new MatchDataSets(mongoData, dataSet).check();
    }

}