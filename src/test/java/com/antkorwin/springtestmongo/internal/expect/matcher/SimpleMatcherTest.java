package com.antkorwin.springtestmongo.internal.expect.matcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 16.12.2018.
 *
 * @author Korovin Anatoliy
 */
class SimpleMatcherTest {

    private static Stream<Arguments> necessaryTestData() {
        return Stream.of(Arguments.of("123"),
                         Arguments.of(123),
                         Arguments.of("abcd"),
                         Arguments.of(10001L));
    }

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
    @MethodSource("necessaryTestData")
    void necessary(Object value) {
        boolean necessary = new SimpleMatcher().isNecessary(value);
        assertThat(necessary).isTrue();
    }

    @ParameterizedTest
    @MethodSource("matchTestData")
    void match(Object first, Object second) {
        boolean match = new SimpleMatcher().match(first, second);
        assertThat(match).isTrue();
    }

    @ParameterizedTest
    @MethodSource("notMatchTestData")
    void notMatch(Object first, Object second) {
        boolean match = new SimpleMatcher().match(first, second);
        assertThat(match).isFalse();
    }
}