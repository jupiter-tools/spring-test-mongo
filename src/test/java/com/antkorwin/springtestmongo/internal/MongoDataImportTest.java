package com.antkorwin.springtestmongo.internal;

import com.antkorwin.springtestmongo.Bar;
import com.sun.org.apache.xpath.internal.Arg;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MongoDataImportTest {

    @ParameterizedTest
    @MethodSource("validDataSets")
    void importTest(DataSet dataSet) {
        // Arrange
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        MongoDataImport mongoDataImport = new MongoDataImport(mongoTemplate);
        ArgumentCaptor<Bar> captor = ArgumentCaptor.forClass(Bar.class);
        // Act
        mongoDataImport.importFrom(dataSet);
        // Asserts
        verify(mongoTemplate, times(2)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(2)
                                         .extracting(Bar::getId, Bar::getData)
                                         .containsOnly(Tuple.tuple("101", "data-1"),
                                                       Tuple.tuple("102", "data-2"));
    }

    private static Stream<Arguments> validDataSets(){
        return Stream.of(Arguments.of(getDataSetTyped()),
                         Arguments.of(getDataSetUntyped()));
    }

    private static DataSet getDataSetTyped() {
        ImmutableMap<String, String> bar1 = ImmutableMap.of("id", "101","data", "data-1");
        ImmutableMap<String, String> bar2 = ImmutableMap.of("id", "102","data", "data-2");
        Map<String, List<?>> map = ImmutableMap.of(Bar.class.getCanonicalName(),
                                                   Arrays.asList(bar1, bar2));
        return () -> map;
    }

    private static DataSet getDataSetUntyped() {
        Bar bar1 = new Bar("101", "data-1");
        Bar bar2 = new Bar("102", "data-2");
        Map<String, List<?>> map = ImmutableMap.of(Bar.class.getCanonicalName(),
                                                   Arrays.asList(bar1, bar2));
        return () -> map;
    }
}