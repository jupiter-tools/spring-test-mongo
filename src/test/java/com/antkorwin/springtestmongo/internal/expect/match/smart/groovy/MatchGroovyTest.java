package com.antkorwin.springtestmongo.internal.expect.match.smart.groovy;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.antkorwin.springtestmongo.internal.expect.match.smart.groovy.MatchGroovy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchGroovyTest {

    private final MatchGroovy matchGroovy = new MatchGroovy();

    @Nested
    class MatchTests {

        @Test
        void simpleValueComparing() {
            // Act
            boolean match = matchGroovy.match(123, "groovy-match: value == 123");
            // Asserts
            assertThat(match).isEqualTo(true);
        }

        @Test
        void compareValueWithSum() {
            // Act
            boolean match = matchGroovy.match(55, "groovy-match: value == (1..10).sum()");
            // Asserts
            assertThat(match).isEqualTo(true);
        }

        @Test
        void checkDateAndTime() {
            // Act
            boolean match = matchGroovy.match(new Date(),
                                              "groovy-match: new Date().getTime() - value.getTime() < 10000");
            // Asserts
            assertThat(match).isEqualTo(true);
        }
    }

    @Nested
    class NecessaryTests {

        @Test
        void necessary() {
            boolean necessary = matchGroovy.isNecessary("groovy-match: value == 123");
            assertThat(necessary).isEqualTo(true);
        }

        @Test
        void notNecessary() {
            boolean necessary = matchGroovy.isNecessary("groovy: value == 123");
            assertThat(necessary).isFalse();
        }

        @Test
        void withoutGroovyPrefix() {
            boolean necessary = matchGroovy.isNecessary("value == 123");
            assertThat(necessary).isFalse();
        }

        @Test
        void notGroovy() {
            boolean necessary = matchGroovy.isNecessary("123");
            assertThat(necessary).isFalse();
        }

        @Test
        void wrongTypeOfValue() {
            boolean necessary = matchGroovy.isNecessary(1024);
            assertThat(necessary).isFalse();
        }
    }

    @Nested
    class CornerCaseTests {

        @Test
        void notBooleanResult() {
            // Act
            InternalException exception = Assertions.assertThrows(InternalException.class, () -> {
                matchGroovy.match(55, "groovy-match: (1..10).sum()");
            });

            assertThat(exception.getMessage()).contains("groovy-match: must return a boolean value instead of {55}");
        }

        @Test
        void wrongGroovyScript() {
            // Act
            InternalException exception = Assertions.assertThrows(InternalException.class, () -> {
                matchGroovy.match(55, "groovy-match: *p = (1..10).sum() ");
            });
            assertThat(exception.getMessage()).contains("Groovy engine evaluate error");
        }
    }
}