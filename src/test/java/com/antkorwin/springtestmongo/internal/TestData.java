package com.antkorwin.springtestmongo.internal;

import java.util.List;
import java.util.Map;

/**
 * Created on 15.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TestData {

    public Map<String, List<Map<String, Object>>> read(String fileName){
        return new JsonImport(new ImportFile(fileName)).read();
    }
}
