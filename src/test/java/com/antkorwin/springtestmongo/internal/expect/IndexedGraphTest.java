package com.antkorwin.springtestmongo.internal.expect;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 11.12.2018.
 *
 * @author Korovin Anatoliy
 */
class IndexedGraphTest {

    @Test
    void evaluatePatternIndexes() {
        // Arrange
        boolean[][] matrix = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act
        Set<Integer> indexes = new IndexedGraph(new TestGraph(() -> matrix)).evaluatePatternIndexes();
        // Asserts
        assertThat(indexes).containsOnly(0, 1, 2, 3, 4);
    }

    @Test
    void evaluateDataIndexes() {
        // Arrange
        boolean[][] matrix = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act
        Set<Integer> indexes = new IndexedGraph(new TestGraph(() -> matrix)).evaluateDataIndexes();
        // Asserts
        assertThat(indexes).containsOnly(0, 1, 2, 3, 4);
    }

    @Test
    void evaluateDataIndexesWithOneUnmatchedDataRecord() {
        // Arrange
        boolean[][] matrix = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, false},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act
        Set<Integer> indexes = new IndexedGraph(new TestGraph(() -> matrix)).evaluateDataIndexes();
        // Asserts
        assertThat(indexes).containsOnly(0, 1, 3, 4);
    }

    @Test
    void evaluatePatternIndexesWithOneUnusedPattern() {
        // Arrange
        boolean[][] matrix = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, false},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act
        Set<Integer> indexes = new IndexedGraph(new TestGraph(() -> matrix)).evaluatePatternIndexes();
        // Asserts
        assertThat(indexes).containsOnly(0, 1, 2, 3);
    }
}