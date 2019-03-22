package com.jupiter.tools.spring.test.mongo.internal.expect.match.simple;

import com.jupiter.tools.spring.test.mongo.internal.expect.match.MatchData;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchDataFactoryTest {

    private MatchDataFactory matchDataFactory = new MatchDataFactory();

    private static Stream<Arguments> aliases() {
        return Stream.of(Arguments.of(JsonNodeType.STRING, MatchString.class),
                         Arguments.of(JsonNodeType.OBJECT, MatchMap.class),
                         Arguments.of(JsonNodeType.ARRAY, MatchList.class),
                         Arguments.of(JsonNodeType.NUMBER, MatchNumber.class),
                         Arguments.of(JsonNodeType.BOOLEAN, MatchObjects.class));
    }

    @ParameterizedTest
    @MethodSource("aliases")
    void get(JsonNodeType jsonType, Class<?> matchType) {
        // Act
        MatchData matchData = matchDataFactory.get(jsonType);
        // Asserts
        assertThat(matchData).isInstanceOf(matchType);
    }
}