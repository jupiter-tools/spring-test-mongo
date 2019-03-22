package com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 04.01.2019.
 *
 * @author Korovin Anatoliy
 */
class DocumentClassesTest {

    @Test
    void getDocumentClass() {
        // Arrange
        DocumentClasses documentClasses =
                new DocumentClasses(() -> ImmutableMap.of("bar", Bar.class,
                                                          "foo", Foo.class));
        // Act & Asserts
        assertThat(documentClasses.getDocumentClass("bar")).isEqualTo(Bar.class);
        assertThat(documentClasses.getDocumentClass("foo")).isEqualTo(Foo.class);
    }

    @Test
    void getDocumentClassName() {
        // Arrange
        DocumentClasses documentClasses =
                new DocumentClasses(() -> ImmutableMap.of("Bar", Bar.class,
                                                          "Foo", Foo.class));
        // Act & Asserts
        assertThat(documentClasses.getDocumentClassName("Bar"))
                .isEqualTo(Bar.class.getCanonicalName());

        assertThat(documentClasses.getDocumentClassName("Foo"))
                .isEqualTo(Foo.class.getCanonicalName());
    }
}