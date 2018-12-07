package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.List;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.*;

/**
 * Import a data from {@link DataSet} to MongoDb
 *
 * @author Korovin Anatoliy
 */
class MongoDataImport implements DataSetImport {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    MongoDataImport(MongoTemplate mongoTemplate) {
        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void importFrom(DataSet dataSet) {
        dataSet.read()
               .forEach((key, values) -> importDocumentCollection(mongoTemplate,
                                                                  getDocumentClassByName(key),
                                                                  values));
    }

    private void importDocumentCollection(MongoTemplate mongoTemplate,
                                          Class<?> documentClassType,
                                          List<?> recordCollection) {

        recordCollection.forEach(document -> {
            try {
                // TODO: unfortunately I did't find a way without double transformations (Object->String->documentClass)
                // it would be nice to find an option to parsing object without obtain target class type
                String stringDocument = objectMapper.writeValueAsString(document);
                Object r = objectMapper.readValue(stringDocument, documentClassType);
                mongoTemplate.save(r);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new InternalException(DOCUMENT_RECORD_PARSING_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalException(DATASET_PARSING_ERROR);
            }
        });
    }

    private Class<?> getDocumentClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            LoggerFactory.getLogger(MongoDataImport.class)
                         .error("Unresolved document collection class reference: {}", className, e);
            throw new InternalException(UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE, e);
        }
    }
}