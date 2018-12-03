package com.antkorwin.springtestmongo.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public DataJson(MongoTemplate mongoTemplate, String outputFile) {
        // TODO: check nullability
        this.mongoTemplate = mongoTemplate;
        this.outputFile = outputFile;
    }

//    TODO: add datamongo object
//    public DataJson(DataMongo dataMongo){
//
//    }

    public void export(){

        String name = mongoTemplate.getCollectionNames().stream().findFirst().get();
        Document first = mongoTemplate.getCollection(name).find(Document.class).first();

        if(first ==null){
            return;
        }

        try {
            String className = (String) first.get("_class");
            Class<?> aClass = Class.forName(className);
            List<?> all = mongoTemplate.findAll(aClass);
            Map<String, List<?>> map = new HashMap<>();
            map.put(className, all);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String result = mapper.writeValueAsString(map);
            System.out.println(result);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
