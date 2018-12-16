package com.antkorwin.springtestmongo.internal.expect.matcher;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 15.12.2018.
 *
 * @author Korovin Anatoliy
 */
class RegexMatcherTest {

    @Nested
    class NecessaryTests {

        @Test
        void necessary() {
            // Act
            boolean necessary = new RegexMatcher().isNecessary("regex: ^123..7$");
            // Asserts
            assertThat(necessary).isTrue();
        }

        @Test
        void notNecessary() {
            // Act
            boolean necessary = new RegexMatcher().isNecessary("^123..7$");
            // Asserts
            assertThat(necessary).isFalse();
        }

        @Test
        void necessaryWithoutSpace() {
            // Act
            boolean necessary = new RegexMatcher().isNecessary("regex:^123..7$");
            // Asserts
            assertThat(necessary).isTrue();
        }
    }

    @Nested
    class MatchTests {

        @Test
        void matchSimpleRegexp() {
            // Arrange
            // Act
            boolean match = new RegexMatcher().match("1987", "regex: 19.7");
            // Asserts
            assertThat(match).isTrue();
        }

        @Test
        void matchUuidRegexp() {
            // Arrange
            String UUID_REGEX = "regex: [a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";
            String id = UUID.randomUUID().toString();
            // Act
            boolean match = new RegexMatcher().match(id, UUID_REGEX);
            // Asserts
            assertThat(match).isTrue();
        }
    }
}