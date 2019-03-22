package com.jupiter.tools.spring.test.mongo.internal.importdata;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.commonutils.validation.GuardCheck;
import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.internal.DataSet;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jupiter.tools.spring.test.mongo.errorinfo.MongoDbErrorInfo.DOCUMENT_RECORD_PARSING_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MongoDataImportTest {

    private MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private MongoDataImport mongoDataImport = new MongoDataImport(mongoTemplate);
    private ArgumentCaptor<Bar> captor = ArgumentCaptor.forClass(Bar.class);

    @Test
    void importTestAllFields() {
        // Act
        mongoDataImport.importFrom(getDataSetFull());
        // Asserts
        verify(mongoTemplate, times(2)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(2)
                                         .extracting(Bar::getId, Bar::getData)
                                         .containsOnly(Tuple.tuple("101", "data-1"),
                                                       Tuple.tuple("102", "data-2"));
    }

    @Test
    void importTestPartial() {
        // Act
        mongoDataImport.importFrom(getDataSetPartial());
        // Asserts
        verify(mongoTemplate, times(2)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(2)
                                         .extracting(Bar::getData)
                                         .containsOnly("data-1", "data-2");
    }

    @Test
    void tryToImportWithWrongFormat() {
        // Arrange
        ImmutableMap<String, Object> notBar = ImmutableMap.of("uniqueField", "05111987");

        DataSet dataSet = () -> ImmutableMap.of(Bar.class.getCanonicalName(),
                                                Collections.singletonList(notBar));
        // Act & Assert
        GuardCheck.check(()-> mongoDataImport.importFrom(dataSet),
                         InternalException.class,
                         DOCUMENT_RECORD_PARSING_ERROR);
    }

    private DataSet getDataSetFull() {
        ImmutableMap<String, Object> bar1 = ImmutableMap.of("id", "101", "data", "data-1");
        ImmutableMap<String, Object> bar2 = ImmutableMap.of("id", "102", "data", "data-2");

        Map<String, List<Map<String, Object>>> map =
                ImmutableMap.of(Bar.class.getCanonicalName(), Arrays.asList(bar1, bar2));

        return () -> map;
    }

    private DataSet getDataSetPartial() {
        ImmutableMap<String, Object> bar1 = ImmutableMap.of("data", "data-1");
        ImmutableMap<String, Object> bar2 = ImmutableMap.of("data", "data-2");

        Map<String, List<Map<String, Object>>> map =
                ImmutableMap.of(Bar.class.getCanonicalName(), Arrays.asList(bar1, bar2));

        return () -> map;
    }
}