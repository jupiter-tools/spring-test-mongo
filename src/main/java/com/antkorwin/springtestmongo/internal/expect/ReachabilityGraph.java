package com.antkorwin.springtestmongo.internal.expect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * Remove from a graph all unreachable combination
 * of objects matching.
 *
 * @author Korovin Anatoliy
 */
public class ReachabilityGraph implements Graph {

    private final Graph graph;
    private final Map<Integer, Integer> finalMatchers;
    private boolean[][] matrix;

    public ReachabilityGraph(Graph graph) {
        this.graph = graph;
        this.finalMatchers = new HashMap<>();
    }

    @Override
    public boolean[][] calculate() {
        matrix = graph.calculate();
        for (int i = 0; i < matrix.length; i++) {
            processLine(i);
        }
        return matrix;
    }

    @Override
    public int dataCount() {
        return graph.dataCount();
    }

    @Override
    public int patternCount() {
        return graph.patternCount();
    }

    @Override
    public Map<String, Object> getDataRecord(int index) {
        return graph.getDataRecord(index);
    }

    @Override
    public Map<String, Object> getPattern(int index) {
        return graph.getPattern(index);
    }

    @Override
    public String getDocumentName() {
        return graph.getDocumentName();
    }

    /**
     * Processing of one line in the graph.
     * Find unique matcher(applied only for one line(entity) in graph)
     * and remove it from all other lines(thus removed unreachable states).
     *
     * @param lineNumber selected number of line in the graph
     */
    private void processLine(int lineNumber) {
        List<Integer> indexes = getCorrectMatcherIndexes(lineNumber);
        if (indexes.size() == 1) {
            int indexOfUniqueMatcher = indexes.get(0);
            if (finalMatchers.keySet().contains(indexOfUniqueMatcher)) {
                if (finalMatchers.get(indexOfUniqueMatcher) != lineNumber) {
                    matrix[lineNumber][indexOfUniqueMatcher] = false;
                }
            } else {
                finalMatchers.put(indexOfUniqueMatcher, lineNumber);
                removeThisMatcherFromOtherLines(lineNumber, indexOfUniqueMatcher);
            }
        }
    }

    /**
     * Collect indexes of matcher which are relevant to this line(entity)
     *
     * @param lineNumber line in the graph.
     *
     * @return list of indexes with applying matcher for this line of the graph.
     */
    private List<Integer> getCorrectMatcherIndexes(int lineNumber) {
        List<Integer> result = new ArrayList<>();
        int length = matrix[lineNumber].length;
        for (int matcherIndex = 0; matcherIndex < length; matcherIndex++) {
            if (matrix[lineNumber][matcherIndex]) result.add(matcherIndex);
        }
        return result;
    }

    /**
     * Remove all matchers in a column (matcherIndex) besides the matcher for lineNumber,
     * and run processing for each line where removed a matcher.
     *
     * @param lineNumber   number of line in graph where placed unique(final) matcher.
     * @param matcherIndex number of matcher index in graph.
     */
    private void removeThisMatcherFromOtherLines(int lineNumber, int matcherIndex) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][matcherIndex] && i != lineNumber) {
                matrix[i][matcherIndex] = false;
                processLine(i);
            }
        }
    }


}
