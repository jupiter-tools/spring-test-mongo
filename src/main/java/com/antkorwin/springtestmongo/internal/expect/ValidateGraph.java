package com.antkorwin.springtestmongo.internal.expect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.fail;


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
        new AssertGraph(matchedIndexes, patternIndexes).doAssert();
//        assertMatched(matchedIndexes);
//        assertPatterns(patternIndexes);
////        assertThat(matchedIndexes.size()).isEqualTo(graph.instanceCount());
////        assertThat(patternIndexes.size()).isEqualTo(graph.patternCount());
    }

    class AssertGraph {

        private final Set<Integer> instanceIndexes;
        private final Set<Integer> patternIndexes;
        private boolean failed = false;
        private List<String> errors = new ArrayList<>();

        public AssertGraph(Set<Integer> instanceIndexes, Set<Integer> patternIndexes) {
            this.instanceIndexes = instanceIndexes;
            this.patternIndexes = patternIndexes;
        }

        public void doAssert() {
            validateInstances(instanceIndexes);
            validatePatterns(patternIndexes);
            if (failed) {
                fail("ExpectedDataSet error: \n" +
                     errors.stream().collect(Collectors.joining("\n")));
            }
        }

        private void validateInstances(Set<Integer> indexes) {
            if (indexes.size() != graph.instanceCount()) {
                Set<Integer> notFoundIndexes = IntStream.range(0, graph.instanceCount())
                                                        .boxed()
                                                        .collect(Collectors.toSet());
                notFoundIndexes.removeAll(indexes);
                String notFoundMatchers = notFoundIndexes.stream()
                                                         .map(Object::toString)
                                                         .collect(Collectors.joining(","));
                error("Not expected: " + notFoundMatchers);
            }
        }

        private void validatePatterns(Set<Integer> patternIndexes) {
            if (patternIndexes.size() != graph.patternCount()) {
                Set<Integer> indexes = IntStream.range(0, graph.patternCount())
                                                .boxed()
                                                .collect(Collectors.toSet());
                indexes.removeAll(patternIndexes);
                String notFoundPatterns = indexes.stream()
                                                 .map(Object::toString)
                                                 .collect(Collectors.joining(","));
                error("Expected but not found: " + notFoundPatterns);
            }
        }


        private void error(String message) {
            failed = true;
            errors.add(message);
        }
    }

}
