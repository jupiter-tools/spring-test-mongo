package com.jupiter.tools.spring.test.mongo.internal.expect.graph;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchGraphTest {

    @Test
    void calculate() {
        // Arrange
        List<Map<String, Object>> matchedDocuments =
                Arrays.asList(ImmutableMap.of("id", "1",
                                              "firstField", "1A",
                                              "secondField", "1B"),
                              ImmutableMap.of("id", "2",
                                              "firstField", "2A",
                                              "secondField", "2B"));

        List<Map<String, Object>> patternDocuments =
                Arrays.asList(ImmutableMap.of("firstField", "1A",
                                              "secondField", "1B"),
                              ImmutableMap.of("firstField", "2A",
                                              "secondField", "2B"));
        // Act
        MatchGraph matchGraph = new MatchGraph("test", matchedDocuments, patternDocuments);

        // Asserts
        boolean[][] expected = {
                {true, false},
                {false, true}
        };

        new AssertGraphEquals(matchGraph, new TestGraph(() -> expected)).check();
    }

    @Test
    void calculateWithoutMatch() {
        // Arrange
        List<Map<String, Object>> matchedDocuments =
                Arrays.asList(ImmutableMap.of("id", "1",
                                              "firstField", "1A",
                                              "secondField", "1B"),
                              ImmutableMap.of("id", "2",
                                              "firstField", "2A",
                                              "secondField", "2B"));

        List<Map<String, Object>> patternDocuments =
                Arrays.asList(ImmutableMap.of("firstField", "x",
                                              "secondField", "y"),
                              ImmutableMap.of("firstField", "z",
                                              "secondField", "w"));
        // Act
        MatchGraph matchGraph = new MatchGraph("test", matchedDocuments, patternDocuments);

        // Asserts
        boolean[][] expected = {
                {false, false},
                {false, false}
        };

        new AssertGraphEquals(matchGraph, new TestGraph(() -> expected)).check();
    }

    @Test
    void matchTwoEntitiesByOnePattern() {
        // Arrange
        List<Map<String, Object>> matchedDocuments =
                Arrays.asList(ImmutableMap.of("id", "1",
                                              "field", "A"),
                              ImmutableMap.of("id", "2",
                                              "field", "A"));

        List<Map<String, Object>> patternDocuments =
                Arrays.asList(ImmutableMap.of("field", "A"));
        // Act
        MatchGraph matchGraph = new MatchGraph("test", matchedDocuments, patternDocuments);

        // Asserts
        boolean[][] expected = {
                {true},
                {true}
        };

        new AssertGraphEquals(matchGraph, new TestGraph(() -> expected)).check();
    }

    @Test
    void matchOneEntityByTwoPatterns() {
        // Arrange
        List<Map<String, Object>> matchedDocuments =
                Arrays.asList(ImmutableMap.of("id", "1",
                                              "firstField", "A",
                                              "secondField", "B"));

        List<Map<String, Object>> patternDocuments =
                Arrays.asList(ImmutableMap.of("firstField", "A"),
                              ImmutableMap.of("secondField", "B"));
        // Act
        MatchGraph matchGraph = new MatchGraph("test", matchedDocuments, patternDocuments);

        // Asserts
        boolean[][] expected = {
                {true, true}
        };

        new AssertGraphEquals(matchGraph, new TestGraph(() -> expected)).check();
    }

    @Test
    void patternWithMoreFieldsAsInMatched() {
        // Arrange
        List<Map<String, Object>> matchedDocuments =
                Arrays.asList(ImmutableMap.of("id", "1",
                                              "firstField", "1A"),
                              ImmutableMap.of("id", "2",
                                              "firstField", "2A"));

        List<Map<String, Object>> patternDocuments =
                Arrays.asList(ImmutableMap.of("firstField", "1A",
                                              "secondField", "1B"),
                              ImmutableMap.of("firstField", "2A",
                                              "secondField", "2B"));
        // Act
        MatchGraph matchGraph = new MatchGraph("test", matchedDocuments, patternDocuments);

        // Asserts
        boolean[][] expected = {
                {false, false},
                {false, false}
        };

        new AssertGraphEquals(matchGraph, new TestGraph(() -> expected)).check();
    }
}