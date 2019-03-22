package com.jupiter.tools.spring.test.mongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.jupiter.tools.spring.test.mongo.internal.expect.MatchDataSets;
import com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value.DateDynamicValue;
import com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value.DynamicDataSet;
import com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value.DynamicValue;
import com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value.GroovyDynamicValue;
import com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value.JavaScriptDynamicValue;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.ExportFile;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.JsonExport;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.MongoDataExport;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner.ReflectionsDocumentScanner;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner.DocumentClasses;
import com.jupiter.tools.spring.test.mongo.internal.importdata.ImportFile;
import com.jupiter.tools.spring.test.mongo.internal.importdata.JsonImport;
import com.jupiter.tools.spring.test.mongo.internal.importdata.MongoDataImport;
import com.google.common.collect.Sets;
import com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

/**
 * MongoDb test tools,
 * to run importing and exporting MongoDb's data.
 *
 * @author Korovin Anatoliy
 */
public class MongoDbTest {

    private final MongoTemplate mongoTemplate;
    private final DocumentClasses documentClasses;

    public MongoDbTest(MongoTemplate mongoTemplate) {
        Guard.check(mongoTemplate != null, InternalException.class, MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY);
        this.mongoTemplate = mongoTemplate;
        documentClasses = new DocumentClasses(new ReflectionsDocumentScanner(""));
    }

    /**
     * export data from MongoBd to the File
     *
     * @param fileName path to the export file
     */
    public void exportTo(String fileName) {
        new ExportFile(new JsonExport(new MongoDataExport(this.mongoTemplate, documentClasses))).write(fileName);
    }

    /**
     * import data from file to MongoDb
     *
     * @param fileName path to file with the data set.
     */
    public void importFrom(String fileName) {

        new MongoDataImport(mongoTemplate).importFrom(
                new DynamicDataSet(new JsonImport(new ImportFile(fileName)),
                                   getDynamicEvaluators()));
    }

    /**
     * Check data in the mongodb,
     * try to match data from the DB to loaded from file data set.
     *
     * @param fileName path to file with an expected data set
     */
    public void expect(String fileName) {
        DataSet dataSet = new DynamicDataSet(new JsonImport(new ImportFile(fileName)), getDynamicEvaluators());
        DataSet mongoData = new MongoDataExport(mongoTemplate, documentClasses);
        new MatchDataSets(mongoData, dataSet).check();
    }

    private Set<DynamicValue> getDynamicEvaluators() {
        return Sets.newHashSet(new GroovyDynamicValue(),
                               new JavaScriptDynamicValue(),
                               new DateDynamicValue());
    }

}