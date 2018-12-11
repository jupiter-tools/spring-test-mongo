package com.antkorwin.springtestmongo.internal.expect;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.fail;

public class AssertGraph {

    private final IndexedGraph indexGraph;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean failed = false;
    private List<String> errors = new ArrayList<>();


    public AssertGraph(Graph graph) {
        this.indexGraph = new IndexedGraph(graph);
    }

    public void doAssert() {
        validateDataRecords(indexGraph.evaluateDataIndexes());
        validatePatterns(indexGraph.evaluatePatternIndexes());
        if (failed) {
            fail("ExpectedDataSet\n" +
                 errors.stream().collect(Collectors.joining("\n")));
        }
    }

    private void validateDataRecords(Set<Integer> indexes) {
        if (indexes.size() != indexGraph.dataCount()) {

            String notFoundDataRecords = IntStream.range(0, indexGraph.dataCount())
                                                  .boxed()
                                                  .filter(i -> !indexes.contains(i))
                                                  .map(indexGraph::getDataRecord)
                                                  .map(this::mapToString)
                                                  .collect(Collectors.joining("\n"));

            error("Not expected: \n" + notFoundDataRecords + "\n");
        }
    }


    private void validatePatterns(Set<Integer> indexes) {
        if (indexes.size() != indexGraph.patternCount()) {

            String notFoundPatterns = IntStream.range(0, indexGraph.patternCount())
                                                  .boxed()
                                                  .filter(i -> !indexes.contains(i))
                                                  .map(indexGraph::getPattern)
                                                  .map(this::mapToString)
                                                  .collect(Collectors.joining("\n"));

            error("Expected but not found: \n" + notFoundPatterns + "\n");
        }
    }

    private String mapToString(Map<String, Object> stringObjectMap) {
        try {
            return objectMapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }

    private void error(String message) {
        failed = true;
        errors.add(message);
    }
}