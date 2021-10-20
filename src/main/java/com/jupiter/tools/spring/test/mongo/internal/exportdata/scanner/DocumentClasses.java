package com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner;

import java.util.Map;

/**
 * Created on 04.01.2019.
 *
 * Retrieve class of the MongoDb Document by the name of the collection.
 *
 * @author Korovin Anatoliy
 */
public class DocumentClasses {

    private final Map<String, Class<?>> documents;

    public DocumentClasses(DocumentScanner scanner) {
        this.documents = scanner.scan();
    }

    public Class<?> getDocumentClass(String collectionName) {
        return documents.get(collectionName);
    }

    public String getDocumentClassName(String collectionName) {
        return documents.get(collectionName).getCanonicalName();
    }

    public boolean hasCollection(String collectionName) {
        return documents.containsKey(collectionName);
    }
}
