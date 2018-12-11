package com.antkorwin.springtestmongo.internal.expect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 10.12.2018.
 * <p>
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
class AssertGraphTest {

    @Test
    void validGraph() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            new AssertGraph(new TestGraph(() -> graph)).doAssert();
        });
    }

    @Test
    void graphWithOneUnusedEntity() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, false},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        List<Map<String, Object>> data = Arrays.asList(
                ImmutableMap.of("id", "1", "value", "BAR-1"),
                ImmutableMap.of("id", "2", "value", "BAR-2"),
                ImmutableMap.of("id", "3", "value", "BAR-3"),
                ImmutableMap.of("id", "4", "value", "BAR-4"),
                ImmutableMap.of("id", "5", "value", "BAR-5"));
        List<Map<String, Object>> patterns = Arrays.asList(
                ImmutableMap.of("value", "BAR-1"),
                ImmutableMap.of("value", "BAR-2"),
                ImmutableMap.of("value", "BAR-5"),
                ImmutableMap.of("value", "BAR-4"),
                ImmutableMap.of("value", "FOO-xxx"));
        // Act & Assert
        Error error = Assertions.assertThrows(Error.class, () -> {
            new AssertGraph(new TestGraph(() -> graph, data, patterns)).doAssert();
        });
        System.out.println(error);
        new Printer(new TestGraph(() -> graph)).print();
    }

    @Test
    void graphWithOneUnmatchedEntity() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, false, false, false}
                // @formatter:on
        };
        List<Map<String, Object>> data = Arrays.asList(
                ImmutableMap.of("id", "1", "value", "BAR-1"),
                ImmutableMap.of("id", "2", "value", "BAR-2"),
                ImmutableMap.of("id", "3", "value", "BAR-3"),
                ImmutableMap.of("id", "4", "value", "BAR-4"),
                ImmutableMap.of("id", "5", "value", "BAR-5"));
        List<Map<String, Object>> patterns = Arrays.asList(
                ImmutableMap.of("value", "BAR-1"),
                ImmutableMap.of("value", "BAR-2"),
                ImmutableMap.of("value", "BAR-???"),
                ImmutableMap.of("value", "BAR-4"),
                ImmutableMap.of("value", "BAR-3"));
        // Act & Assert
        Error error = Assertions.assertThrows(Error.class, () -> {
            new AssertGraph(new TestGraph(() -> graph, data, patterns)).doAssert();
        });
        System.out.println(error);
    }
}