package com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner;

import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 03.01.2019.
 * <p>
 * Scans selected package on the MongoDB Documents and returns
 * a map with all founded collections with their class-types.
 * <p>
 * Finds all classes which annotated by the Document annotation
 * and takes collection names from this annotation or takes a class
 * name if the collection name doesn't set by the Document annotation.
 *
 * @author Korovin Anatoliy
 */
public class ReflectionsDocumentScanner implements DocumentScanner {

    private final String basePackage;

    public ReflectionsDocumentScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public Map<String, Class<?>> scan() {

        HashMap<String, Class<?>> result = new CaseInsensitiveMap();

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> documents = reflections.getTypesAnnotatedWith(Document.class);

        for (Class<?> doc : documents) {

            Document annotation = doc.getAnnotation(Document.class);
            String value = (String) AnnotationUtils.getValue(annotation, "value");

            if (empty(value)) {
                value = annotation.collection();
            }

            if(empty(value)){
                value = doc.getSimpleName().toLowerCase();
            }

            result.put(value, doc);
        }

        return result;
    }

    private boolean empty(String value) {
        return value == null || "".equals(value);
    }
}
