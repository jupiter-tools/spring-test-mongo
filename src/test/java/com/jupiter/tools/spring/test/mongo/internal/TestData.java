package com.jupiter.tools.spring.test.mongo.internal;

import java.util.List;
import java.util.Map;

import com.jupiter.tools.spring.test.mongo.internal.importdata.ImportFile;
import com.jupiter.tools.spring.test.mongo.internal.importdata.JsonImport;

/**
 * Created on 15.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TestData {

    public Map<String, List<Map<String, Object>>> read(String fileName) {
        return new JsonImport(new ImportFile(fileName)).read();
    }

    public DataSet jsonDataSet(String fileName) {
        return new JsonImport(new ImportFile(fileName));
    }
}
