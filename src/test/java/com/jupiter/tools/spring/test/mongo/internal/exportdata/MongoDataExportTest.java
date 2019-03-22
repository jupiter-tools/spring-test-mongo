package com.jupiter.tools.spring.test.mongo.internal.exportdata;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner.ReflectionsDocumentScanner;
import com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner.DocumentClasses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.groups.Tuple;
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

    private MongoTemplate mongoTemplate;
    private MongoDataExport mongoDataExport;

    @BeforeEach
    void setUp() {
        DocumentClasses documentClasses = new DocumentClasses(new ReflectionsDocumentScanner(""));
        mongoTemplate = mock(MongoTemplate.class, RETURNS_DEEP_STUBS);
        mongoDataExport = new MongoDataExport(mongoTemplate, documentClasses);
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