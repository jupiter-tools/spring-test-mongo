package com.jupiter.tools.spring.test.mongo.internal.importdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.jupiter.tools.spring.test.mongo.internal.DataSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo.DOCUMENT_RECORD_PARSING_ERROR;
import static com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo.MONGO_TEMPLATE_IS_MANDATORY;
import static com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo.UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE;

/**
 * Import a data from {@link DataSet} to MongoDb
 *
 * @author Korovin Anatoliy
 */
public class MongoDataImport implements DataSetImport {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(MongoDataImport.class);

    public MongoDataImport(MongoTemplate mongoTemplate) {
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

    private <T> void importDocumentCollection(MongoTemplate mongoTemplate,
                                              Class<T> documentClassType,
                                              List<?> recordCollection) {

        recordCollection.forEach(document -> {
            try {
                T typedDocument = objectMapper.convertValue(document, documentClassType);
                mongoTemplate.save(typedDocument);
            } catch (Exception e){
                log.error("Error while trying to convert Object: {} to: {}", document, documentClassType, e);
                throw new InternalException(DOCUMENT_RECORD_PARSING_ERROR, e);
            }
        });
    }

    private Class<?> getDocumentClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Unresolved document collection class reference: {}", className, e);
            throw new InternalException(UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE, e);
        }
    }
}