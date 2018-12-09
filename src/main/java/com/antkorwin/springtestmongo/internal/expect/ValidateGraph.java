package com.antkorwin.springtestmongo.internal.expect;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 10.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class ValidateGraph {

    private final Graph graph;

    public ValidateGraph(Graph graph) {
        this.graph = graph;
    }

    public void validate() {

        Set<Integer> matchedIndexes = new HashSet<>();
        Set<Integer> patternIndexes = new HashSet<>();

        boolean[][] matrix = graph.calculate();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    matchedIndexes.add(i);
                    patternIndexes.add(j);
                }
            }
        }

        assertThat(matchedIndexes.size()).isEqualTo(graph.matcherCount());
        assertThat(patternIndexes.size()).isEqualTo(graph.patternCount());
    }
}
