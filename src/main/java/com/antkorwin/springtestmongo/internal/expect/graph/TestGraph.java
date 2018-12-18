package com.antkorwin.springtestmongo.internal.expect.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created on 10.12.2018.
 *
 * Stub implementation of the {@link Graph} interface
 *
 * @author Korovin Anatoliy
 */
public class TestGraph implements Graph {

    private final boolean[][] matrix;
    private final List<Map<String, Object>> dataRecords;
    private final List<Map<String, Object>> patterns;

    public TestGraph(Supplier<boolean[][]> matrix) {
        this.matrix = matrix.get();
        this.dataRecords = new ArrayList<>();
        this.patterns = new ArrayList<>();
    }

    public TestGraph(Supplier<boolean[][]> matrix,
                     List<Map<String, Object>> dataRecords,
                     List<Map<String, Object>> patterns) {
        this.matrix = matrix.get();
        this.dataRecords = dataRecords;
        this.patterns = patterns;
    }

    @Override
    public boolean[][] calculate() {
        return matrix;
    }

    @Override
    public int dataCount() {
        return matrix.length;
    }

    @Override
    public int patternCount() {
        return matrix[0].length;
    }

    @Override
    public Map<String, Object> getDataRecord(int index) {
        return dataRecords.get(index);
    }

    @Override
    public Map<String, Object> getPattern(int index) {
        return patterns.get(index);
    }

    @Override
    public String getDocumentName() {
        return "TestDocument";
    }
}
