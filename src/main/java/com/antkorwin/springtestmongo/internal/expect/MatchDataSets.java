package com.antkorwin.springtestmongo.internal.expect;

import com.antkorwin.springtestmongo.internal.DataSet;
import com.antkorwin.springtestmongo.internal.expect.graph.AssertGraph;
import com.antkorwin.springtestmongo.internal.expect.graph.IndexedGraph;
import com.antkorwin.springtestmongo.internal.expect.graph.MatchGraph;
import com.antkorwin.springtestmongo.internal.expect.graph.ReachabilityGraph;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 09.12.2018.
 * <p>
 * Match two data sets, evaluate a combination of full matching.
 * <p>
 * Each data record match at least to one pattern
 * and each pattern applies at least to one data record.
 *
 * @author Korovin Anatoliy
 */
public class MatchDataSets {

    private final DataSet matched;
    private final DataSet pattern;

    public MatchDataSets(DataSet matched, DataSet pattern) {
        this.matched = matched;
        this.pattern = pattern;
    }

    public void check() {

        Map<String, List<Map<String, Object>>> patternMap = pattern.read();
        Map<String, List<Map<String, Object>>> matchedMap = matched.read();

        assertSetsOfDocumentsAreIdentical(matchedMap.keySet(),
                                          patternMap.keySet());

        patternMap.keySet().forEach(documentName -> {
            checkOneCollection(documentName, matchedMap.get(documentName), patternMap.get(documentName));
        });
    }

    private void checkOneCollection(String documentName,
                                    List<Map<String, Object>> matched,
                                    List<Map<String, Object>> pattern) {

        assertDocumentsCountAreEquals(documentName, matched, pattern);

        new AssertGraph(
                new IndexedGraph(
                        new ReachabilityGraph(
                                new MatchGraph(documentName, matched, pattern)
                        )
                )
        ).doAssert();
    }

    private void assertDocumentsCountAreEquals(String documentName, List<Map<String, Object>> matched,
                                               List<Map<String, Object>> pattern) {
        assertThat(matched.size())
                .describedAs("expected %d but found %d - %s entities",
                             pattern.size(), matched.size(), documentName)
                .isEqualTo(pattern.size());
    }

    private void assertSetsOfDocumentsAreIdentical(Set<String> documentNamesFirst,
                                                   Set<String> documentNamesSecond) {
        Assertions.assertEquals(documentNamesFirst,
                                documentNamesSecond,
                                () -> String.format("Not equal document collections:\n expected:\n[%s],\n actual: \n[%s]",
                                                    documentNamesFirst.stream().collect(Collectors.joining(", ")),
                                                    documentNamesSecond.stream().collect(Collectors.joining(", "))));
    }
}
