package com.antkorwin.springtestmongo.internal.exportdata.scanner;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 03.01.2019.
 *
 * @author Korovin Anatoliy
 */
class ReflectionsDocumentScannerTest {

    @Test
    void scanCurrentPackage() {
        // Arrange
        String basePackage = this.getClass().getPackage().getName();
        // Act
        Map<String, Class<?>> map = new ReflectionsDocumentScanner(basePackage).scan();
        // Asserts
        assertThat(map).hasSize(3)
                       .containsEntry("antkorwintestdocfirst", AntkorwinTestDocFirst.class)
                       .containsEntry("antkorwin-test-doc-second", AntkorwinTestDocSecond.class)
                       .containsEntry("antkorwin-test-doc-third", AntkorwinTestDocThird.class);
    }

    @Test
    void scanWholeProjectPackage() {
        // Act
        Map<String, Class<?>> map = new ReflectionsDocumentScanner("").scan();
        // Asserts
        assertThat(map.size()).isGreaterThan(3);
    }

    @Document
    private class AntkorwinTestDocFirst {

    }

    @Document(value = "antkorwin-test-doc-second")
    private class AntkorwinTestDocSecond {

    }

    @Document(collection = "antkorwin-test-doc-THIRD")
    private class AntkorwinTestDocThird {

    }
}