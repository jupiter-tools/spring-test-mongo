package com.antkorwin.springtestmongo.internal.expect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.fail;

public class AssertGraph {

    private final IndexedGraph indexGraph;

    private boolean failed = false;
    private List<String> errors = new ArrayList<>();


    public AssertGraph(Graph graph) {
        this.indexGraph = new IndexedGraph(graph);
    }

    public void doAssert() {
        validateDataRecords(indexGraph.evaluateDataIndexes());
        validatePatterns(indexGraph.evaluatePatternIndexes());
        if (failed) {
            fail("ExpectedDataSet error: \n" +
                 errors.stream().collect(Collectors.joining("\n")));
        }
    }

    private void validateDataRecords(Set<Integer> indexes) {
        if (indexes.size() != indexGraph.dataCount()) {
            Set<Integer> notFoundIndexes = IntStream.range(0, indexGraph.dataCount())
                                                    .boxed()
                                                    .filter(i -> !indexes.contains(i))
                                                    .collect(Collectors.toSet());

            String notFoundMatchers = notFoundIndexes.stream()
                                                     .map(Object::toString)
                                                     .collect(Collectors.joining(","));
            error("Not expected: " + notFoundMatchers);
        }
    }

    private void validatePatterns(Set<Integer> patternIndexes) {
        if (patternIndexes.size() != indexGraph.patternCount()) {
            Set<Integer> indexes = IntStream.range(0, indexGraph.patternCount())
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