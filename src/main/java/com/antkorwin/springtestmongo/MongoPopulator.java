package com.antkorwin.springtestmongo;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.Guard;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.antkorwin.springtestmongo.errorinfo.RiderErrorInfo.*;


/**
 * Created by Korovin A. on 21.01.2018.
 * <p>
 * MongoDB data populate utility
 *
 * @author Korovin Anatoliy
 */
public class MongoPopulator {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger log = LoggerFactory.getLogger(MongoPopulator.class);

    /**
     * Populate DataSet from file to mongo
     *
     * @param mongoTemplate   connection to mongo database
     * @param dataSetFileName path to the json dataset
     */
    public static void populate(MongoTemplate mongoTemplate, String dataSetFileName) {

        Guard.check(dataSetFileName != null, InternalException.class, DATASET_FILE_NAME_IS_MANDATORY);
        Guard.check(mongoTemplate != null, InternalException.class, MONGO_TEMPLATE_IS_MANDATORY);

        final InputStream inputStream = MongoPopulator.class.getClass().getResourceAsStream(dataSetFileName);
        Guard.check(inputStream != null, InternalException.class, FILE_NOT_FOUND);

        try {
            // read dataset from file:
            String stringDataSet = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            // convert to Map of Object:
            Map<String, Object> objectMap =
                    objectMapper.readValue(stringDataSet,
                                           new TypeReference<Map<String, Object>>() {
                                           });

            // populate data in db:
            internalPopulate(mongoTemplate, objectMap);

        } catch (JsonMappingException | JsonParseException e) {
            e.printStackTrace();
            throw new InternalException(DATASET_PARSING_ERROR, e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(READ_DATASETS_FILE_ERROR, e);
        }
    }


    private static void internalPopulate(MongoTemplate mongoTemplate, Map<String, Object> objectMap) {
        objectMap.forEach((key, value) -> {
            Guard.check(value instanceof List, InternalException.class, DATASET_FORMAT_ERROR);
            populateOneDocumentCollection(mongoTemplate, getDocumentClassByName(key), (List) value);
        });
    }


    private static void populateOneDocumentCollection(MongoTemplate mongoTemplate, Class<?> documentClassType,
                                                      List<?> recordCollection) {
        recordCollection.forEach(document -> {
            try {
                // TODO: unfortunately I don`t find way without twice transformation (Object->String->documentClass)
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


    private static Class<?> getDocumentClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Unresolved document collection class reference: {}", className, e);
            throw new InternalException(UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE, e);
        }
    }
}

