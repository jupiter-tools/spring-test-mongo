package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 26.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
class DateDynamicValueTest {

    @Nested
    class NecessaryTests {

        @Test
        void necessary() {
            // Act
            boolean necessary = new DateDynamicValue().isNecessary("date:[NOW]");
            // Assert
            assertThat(necessary).isTrue();
        }

        @Test
        void notNecessary() {
            // Act
            boolean necessary = new DateDynamicValue().isNecessary("[NOW]");
            // Assert
            assertThat(necessary).isFalse();
        }

        @Test
        void notNecessaryForMatcher() {
            // Act
            boolean necessary = new DateDynamicValue().isNecessary("date-match:[NOW]");
            // Assert
            assertThat(necessary).isFalse();
        }
    }

    @Nested
    class EvaluateTests {

        @Test
        void currentTime() {
            Date before = new Date();
            Date now = (Date) new DateDynamicValue().evaluate("date:[NOW]");
            assertThat(now).isAfterOrEqualsTo(before);
        }

        @Test
        void plus3Minutes() {
            Date expected = new Date(new Date().getTime() + TimeUnit.MINUTES.toMillis(3));
            Date evaluate = (Date) new DateDynamicValue().evaluate("date:[NOW]+3(MINUTES)");
            assertThat(evaluate).isAfterOrEqualsTo(expected);
        }
    }

}