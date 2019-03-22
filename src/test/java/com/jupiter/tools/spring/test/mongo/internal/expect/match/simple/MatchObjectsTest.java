package com.jupiter.tools.spring.test.mongo.internal.expect.match.simple;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchObjectsTest {

    private static Stream<Arguments> matchTestData() {
        return Stream.of(Arguments.of("123", "123"),
                         Arguments.of(123, 123),
                         Arguments.of("abcd", "abcd"),
                         Arguments.of(10001L, 10001L));
    }

    private static Stream<Arguments> notMatchTestData() {
        return Stream.of(Arguments.of("123", "1234"),
                         Arguments.of(123, 12),
                         Arguments.of("abcd", "eeee"),
                         Arguments.of(10001L, 100L),
                         Arguments.of(10001L, 123),
                         Arguments.of("123", 123));
    }

    @ParameterizedTest
    @MethodSource("matchTestData")
    void match(Object first, Object second) {
        boolean match = new MatchObjects().match(first, second);
        assertThat(match).isTrue();
    }

    @ParameterizedTest
    @MethodSource("notMatchTestData")
    void notMatch(Object first, Object second) {
        boolean match = new MatchObjects().match(first, second);
        assertThat(match).isFalse();
    }
}