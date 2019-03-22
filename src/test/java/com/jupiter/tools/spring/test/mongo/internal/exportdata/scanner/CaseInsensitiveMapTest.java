package com.jupiter.tools.spring.test.mongo.internal.exportdata.scanner;

import com.jupiter.tools.spring.test.mongo.Bar;
import com.jupiter.tools.spring.test.mongo.Foo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 04.01.2019.
 *
 * @author Korovin Anatoliy
 */
class CaseInsensitiveMapTest {

    @Test
    void put() {
        // Arrange
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        // Act
        map.put("Bar", Bar.class);
        map.put("Foo", Foo.class);
        // Asserts
        assertThat(map).hasSize(2)
                       .containsEntry("bar", Bar.class)
                       .containsEntry("foo", Foo.class);
    }

    @Test
    void get() {
        // Arrange
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        // Act
        map.put("Bar", Bar.class);
        map.put("foo", Foo.class);
        // Asserts
        assertThat(map.get("bar")).isEqualTo(Bar.class);
        assertThat(map.get("Bar")).isEqualTo(Bar.class);
        assertThat(map.get("foo")).isEqualTo(Foo.class);
        assertThat(map.get("Foo")).isEqualTo(Foo.class);
    }
}