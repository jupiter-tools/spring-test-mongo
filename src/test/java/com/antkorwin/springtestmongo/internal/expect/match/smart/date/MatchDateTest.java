package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchDateTest {

    private final MatchDate matchDate = new MatchDate();

    @Nested
    class NecessaryTests {

        @Test
        void necessary() {
            // Act
            boolean necessary = matchDate.isNecessary("date-match:[NOW]");
            // Asserts
            assertThat(necessary).isTrue();
        }

        @Test
        void notNecessary() {
            // Act
            boolean necessary = matchDate.isNecessary("time");
            // Asserts
            assertThat(necessary).isFalse();
        }

        @Test
        void notNecessaryWithoutPrefix() {
            // Act
            boolean necessary = matchDate.isNecessary("[NOW]");
            // Asserts
            assertThat(necessary).isFalse();
        }

        @Test
        void notNecessaryWrongType() {
            // Act
            boolean necessary = matchDate.isNecessary(123);
            // Asserts
            assertThat(necessary).isFalse();
        }

        @Test
        void necessaryWithSpace() {
            // Act
            boolean necessary = matchDate.isNecessary("date-match: [NOW]");
            // Asserts
            assertThat(necessary).isTrue();
        }
    }

    @Nested
    class MatchTests {

        @Test
        void nowTime() {
            // Act
            boolean now = matchDate.match(new Date(), "date-match:[NOW]");
            // Asserts
            assertThat(now).isTrue();
        }

        @Test
        void nowTimeInLong() {
            // Act
            boolean now = matchDate.match(new Date().getTime(), "date-match:[NOW]");
            // Asserts
            assertThat(now).isTrue();
        }

        @Test
        void nowPlusOneDay() {
            // Arrange
            Date now = new Date();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());

            Date tomorrow = Date.from(localDateTime.plusDays(1)
                                                   .atZone(ZoneId.systemDefault()).toInstant());
            // Act
            boolean match = matchDate.match(tomorrow, "date-match:[NOW]+1(DAYS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void nowPlusTenDays() {
            // Arrange
            Date now = new Date();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());

            Date tomorrow = Date.from(localDateTime.plusDays(10)
                                                   .atZone(ZoneId.systemDefault()).toInstant());
            // Act
            boolean match = matchDate.match(tomorrow, "date-match:[NOW]+10(DAYS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void nowMinusOneDay() {

            Date now = new Date();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());

            Date tomorrow = Date.from(localDateTime.minusDays(1)
                                                   .atZone(ZoneId.systemDefault()).toInstant());
            // Act
            boolean match = matchDate.match(tomorrow, "date-match:[NOW]-1(DAYS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void plusOneHour() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+1(HOURS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void plus5Minutes() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(5));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+5(MINUTES)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void plus25Seconds() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void wrongTime() {
            // Arrange
            Date now = new Date();
            Date tooLate = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(5));
            // Act
            boolean match = matchDate.match(tooLate, "date-match:[NOW]+3(MINUTES)");
            // Assert
            assertThat(match).isFalse();
        }

        @Test
        void plusOneHourWithSpaces() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));
            // Act
            boolean match = matchDate.match(feature, "date-match: [NOW] + 1 (HOURS)");
            // Assert
            assertThat(match).isTrue();
        }
    }

    @Nested
    class ThresholdTests {

        @Test
        void defaultThr() {
            // Arrange
            int defaultThreshold = 10; // SEC
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25 + defaultThreshold));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS)");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void moreThanDefaultValue() {
            // Arrange
            int defaultThreshold = 10; // SEC
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25 + defaultThreshold + 1));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS)");
            // Assert
            assertThat(match).isFalse();
        }

        @Test
        void notDefaultThr() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25 + 20));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS){THR=20000}");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void notZeroThr() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25));
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS){THR=100}");
            // Assert
            assertThat(match).isTrue();
        }

        @Test
        void notZeroThrFail() {
            // Arrange
            Date now = new Date();
            Date feature = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(25) + 10);
            // Act
            boolean match = matchDate.match(feature, "date-match:[NOW]+25(SECONDS){THR=0}");
            // Assert
            assertThat(match).isFalse();
        }
    }

}