package com.antkorwin.springtestmongo.internal.expect;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class MatchGraph implements Graph {

    private final List<Map<String, Object>> matched;
    private final List<Map<String, Object>> pattern;

    public MatchGraph(List<Map<String, Object>> matched,
                      List<Map<String, Object>> pattern) {
        this.matched = matched;
        this.pattern = pattern;
    }

    @Override
    public int dataCount() {
        return this.matched.size();
    }

    @Override
    public int patternCount() {
        return this.pattern.size();
    }

    @Override
    public boolean[][] calculate() {

        int matchedSize = dataCount();
        int patternSize = patternCount();

        boolean[][] matrix = new boolean[matchedSize][patternSize];

        for (int i = 0; i < matchedSize; i++) {
            ObjectMatcher actualItem = new ObjectMatcher(matched.get(i));
            for (int j = 0; j < patternSize; j++) {
                matrix[i][j] = actualItem.match(pattern.get(j));
            }
        }

        return matrix;
    }
}
