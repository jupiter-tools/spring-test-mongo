package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;

/**
 * Export a data from MongoDb to {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
class MongoDataExport implements DataSet {

    private final MongoTemplate mongoTemplate;

    MongoDataExport(MongoTemplate mongoTemplate) {
        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<String, List<?>> read() {
        Map<String, List<?>> map = new HashMap<>();

        for (String name : mongoTemplate.getCollectionNames()) {
            map.put(getEntityClassName(name), getDataSet(name));
        }

        return map;
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