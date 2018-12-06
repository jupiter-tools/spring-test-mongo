package com.antkorwin.springtestmongo.internal;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MongoDataExportTest {

    private MongoTemplate mongoTemplate = mock(MongoTemplate.class, RETURNS_DEEP_STUBS);
    private MongoDataExport mongoDataExport = new MongoDataExport(mongoTemplate);

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(MongoTemplate.class, RETURNS_DEEP_STUBS);
        mongoDataExport = new MongoDataExport(mongoTemplate);
    }

    @Test
    void readSingleDocumentCollection() {
        // Arrange
        List<Bar> bars = mock(List.class);
        arrangeCollections(mongoTemplate, new DocCollection<>(Bar.class, bars));
        // Act
        Map<String, List<?>> dataSet = mongoDataExport.read();
        // Assert
        assertThat(dataSet.get(Bar.class.getCanonicalName())).isEqualTo(bars);
    }

    @Test
    void readMultipleDocumentCollections() {
        // Arrange
        List<Bar> bars = mock(List.class);
        List<Foo> foos = mock(List.class);
        arrangeCollections(mongoTemplate,
                           new DocCollection<>(Bar.class, bars),
                           new DocCollection<>(Foo.class, foos));
        MongoDataExport mongoDataExport = new MongoDataExport(mongoTemplate);
        // Act
        Map<String, List<?>> dataSet = mongoDataExport.read();
        // Assert
        assertThat(dataSet.get(Bar.class.getCanonicalName())).isEqualTo(bars);
        assertThat(dataSet.get(Foo.class.getCanonicalName())).isEqualTo(foos);
    }

    private void arrangeCollections(MongoTemplate mongoTemplate, DocCollection... documents) {
        Set<String> names = new HashSet<>();
        for (DocCollection d : documents) {
            // mock the checking type of the first document
            Document documentX = new Document("_class", d.getType().getCanonicalName());
            when(mongoTemplate.getCollection(d.getType().getSimpleName())
                              .find(Document.class)
                              .first())
                    .thenReturn(documentX);
            // mock stored collection of this documents
            when(mongoTemplate.findAll(d.getType())).thenReturn(d.getData());
            // collect each name of documents class
            names.add(d.getType().getSimpleName());
        }
        when(mongoTemplate.getCollectionNames()).thenReturn(names);
    }

    @Getter
    @AllArgsConstructor
    class DocCollection<T> {
        private Class<T> type;
        private List<T> data;
    }
}