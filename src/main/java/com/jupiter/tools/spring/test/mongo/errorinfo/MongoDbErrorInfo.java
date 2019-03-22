package com.jupiter.tools.spring.test.mongo.errorinfo;

import com.antkorwin.commonutils.validation.ErrorInfo;

/**
 * Created by Korovin A. on 21.01.2018.
 *
 * Error info messages
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public enum MongoDbErrorInfo implements ErrorInfo {

    READ_DATASETS_FILE_ERROR("Error while reading file with a DataSet."),
    FILE_NOT_FOUND("Not found a DataSet file."),
    DATASET_FILE_NAME_IS_MANDATORY("DataSet's file name is a mandatory argument."),
    MONGO_TEMPLATE_IS_MANDATORY("MongoTemplate is a mandatory argument."),
    UNRESOLVED_DOCUMENT_COLLECTION_CLASS_TYPE("Unresolved document collection class reference from the DataSet."),
    DOCUMENT_RECORD_PARSING_ERROR("Error while parsing the document record in the collection."),
    DATASET_PARSING_ERROR("Error while parsing the file with a dataset."),
    DATASET_FORMAT_ERROR("Wrong dataset's format."),
    JSON_PARSING_ERROR("Error when parsing the JSON text.");

    private final String message;
    private final int base = 1000;


    MongoDbErrorInfo(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return base + ordinal();
    }
}