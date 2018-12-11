package com.antkorwin.springtestmongo.internal.expect;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchGraph implements Graph {

    private final List<Map<String, Object>> dataRecords;
    private final List<Map<String, Object>> patterns;

    public MatchGraph(List<Map<String, Object>> dataRecords,
                      List<Map<String, Object>> patterns) {
        this.dataRecords = dataRecords;
        this.patterns = patterns;
    }

    @Override
    public boolean[][] calculate() {

        int matchedSize = dataCount();
        int patternSize = patternCount();

        boolean[][] matrix = new boolean[matchedSize][patternSize];

        for (int i = 0; i < matchedSize; i++) {
            ObjectMatcher actualItem = new ObjectMatcher(dataRecords.get(i));
            for (int j = 0; j < patternSize; j++) {
                matrix[i][j] = actualItem.match(patterns.get(j));
            }
        }

        return matrix;
    }

    @Override
    public int dataCount() {
        return this.dataRecords.size();
    }

    @Override
    public int patternCount() {
        return this.patterns.size();
    }

    @Override
    public Map<String, Object> getDataRecord(int index) {
        return dataRecords.get(index);
    }

    @Override
    public Map<String, Object> getPattern(int index) {
        return patterns.get(index);
    }

}
