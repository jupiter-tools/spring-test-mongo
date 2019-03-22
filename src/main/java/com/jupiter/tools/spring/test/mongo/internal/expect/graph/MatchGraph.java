package com.jupiter.tools.spring.test.mongo.internal.expect.graph;

import com.jupiter.tools.spring.test.mongo.internal.expect.match.MatchAny;

import java.util.List;
import java.util.Map;

/**
 * Created on 09.12.2018.
 * <p>
 * Evaluate the graph of the object matching,
 * try to match all data records to each pattern,
 * and save this in the matrix.
 *
 * @author Korovin Anatoliy
 */
public class MatchGraph implements Graph {

    private final List<Map<String, Object>> dataRecords;
    private final List<Map<String, Object>> patterns;
    private final String documentName;
    private final MatchAny matchAny;

    public MatchGraph(String documentName,
                      List<Map<String, Object>> dataRecords,
                      List<Map<String, Object>> patterns) {
        this.documentName = documentName;
        this.dataRecords = dataRecords;
        this.patterns = patterns;
        this.matchAny = new MatchAny();
    }

    @Override
    public boolean[][] calculate() {

        int matchedSize = dataCount();
        int patternSize = patternCount();

        boolean[][] matrix = new boolean[matchedSize][patternSize];

        for (int i = 0; i < matchedSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                matrix[i][j] = matchAny.match(dataRecords.get(i), patterns.get(j));
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

    @Override
    public String getDocumentName() {
        return this.documentName;
    }

}
