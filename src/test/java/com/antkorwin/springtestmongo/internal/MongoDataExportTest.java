package com.antkorwin.springtestmongo.internal;

import com.antkorwin.springtestmongo.Bar;
import com.antkorwin.springtestmongo.Foo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.groups.Tuple;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

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
        Bar bar1 = new Bar("1", "B");
        Bar bar2 = new Bar("2", "BB");
        List<Bar> bars = Arrays.asList(bar1, bar2);
        arrangeCollections(mongoTemplate, new DocCollection<>(Bar.class, bars));
        // Act
        Map<String, List<Map<String, Object>>> dataSet = mongoDataExport.read();
        // Assert
        assertThat(dataSet.get(Bar.class.getCanonicalName())).hasSize(2)
                                                             .extracting(m -> m.get("id"),
                                                                         m -> m.get("data"))
                                                             .containsOnly(Tuple.tuple("1", "B"),
                                                                           Tuple.tuple("2", "BB"));
    }

    @Test
    void readMultipleDocumentCollections() {
        // Arrange
        Bar bar1 = new Bar("1", "B");
        Bar bar2 = new Bar("2", "BB");
        List<Bar> bars = Arrays.asList(bar1, bar2);
        Foo foo1 = new Foo("1", new Date(12345), 1);
        Foo foo2 = new Foo("2", new Date(12345), 2);
        List<Foo> foos = Arrays.asList(foo1, foo2);
        arrangeCollections(mongoTemplate,
                           new DocCollection<>(Bar.class, bars),
                           new DocCollection<>(Foo.class, foos));
        MongoDataExport mongoDataExport = new MongoDataExport(mongoTemplate);
        // Act
        Map<String, List<Map<String, Object>>> dataSet = mongoDataExport.read();
        // Assert
        assertThat(dataSet.get(Bar.class.getCanonicalName())).hasSize(2)
                                                             .extracting(m -> m.get("id"),
                                                                         m -> m.get("data"))
                                                             .containsOnly(Tuple.tuple("1", "B"),
                                                                           Tuple.tuple("2", "BB"));

        assertThat(dataSet.get(Foo.class.getCanonicalName())).hasSize(2)
                                                             .extracting(m -> m.get("id"),
                                                                         m -> m.get("time"),
                                                                         m -> m.get("counter"))
                                                             .containsOnly(Tuple.tuple("1", 12345L, 1),
                                                                           Tuple.tuple("2", 12345L, 2));
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
    private class DocCollection<T> {
        private Class<T> type;
        private List<T> data;
    }
}