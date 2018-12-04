package com.antkorwin.springtestmongo.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.bson.Document;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created on 03.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class DataJson {

    private final MongoTemplate mongoTemplate;
    private final String outputFile;
    private final ObjectMapper objectMapper;

    public DataJson(MongoTemplate mongoTemplate, String outputFile) {
        // TODO: check nullability
        this.mongoTemplate = mongoTemplate;
        this.outputFile = outputFile;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void export() {

        Map<String, List<?>> map = new HashMap<>();

        for (String name : mongoTemplate.getCollectionNames()) {
            map.put(getEntityClassName(name), getDataSet(name));
        }

        saveInFile(outputFile, convertToJson(map));
    }

    private void saveInFile(String outputFile, String data) {
        try {
            Files.write(Paths.get(outputFile), data.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }

    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }

    private List<?> getDataSet(String collectionName) {

        Document first = mongoTemplate.getCollection(collectionName)
                                      .find(Document.class)
                                      .first();
        if (first == null) {
            return Collections.emptyList();
        }

        try {
            String className = (String) first.get("_class");
            Class<?> aClass = Class.forName(className);
            return mongoTemplate.findAll(aClass);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }

    private String getEntityClassName(String collectionName) {

        Document first = mongoTemplate.getCollection(collectionName)
                                      .find(Document.class)
                                      .first();

        if (first == null) return collectionName;

        Object classType = first.get("_class");
        return (classType != null) ? (String) classType : collectionName;
    }
}
