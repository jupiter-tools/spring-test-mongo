package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import com.antkorwin.springtestmongo.internal.expect.match.smart.date.MatchDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
            boolean necessary = matchDate.isNecessary("[NOW]");
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
        void notNecessaryWrongType() {
            // Act
            boolean necessary = matchDate.isNecessary(123);
            // Asserts
            assertThat(necessary).isFalse();
        }
    }

    @Nested
    class MatchTests {

        @Test
        void nowTime() {
            // Act
            boolean necessary = matchDate.match(new Date(), "[NOW]");
            // Asserts
            assertThat(necessary).isTrue();
        }

        @Test
        void nowPlusOneDay() {
            // Arrange
            Date now = new Date();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());

            Date tomorrow = Date.from(localDateTime.plusDays(1)
                                                   .atZone(ZoneId.systemDefault()).toInstant());
            // Act
            boolean match = matchDate.match(tomorrow, "[NOW]+1(DAYS)");
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
            boolean match = matchDate.match(tomorrow, "[NOW]+10(DAYS)");
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
            boolean match = matchDate.match(tomorrow, "[NOW]-1(DAYS)");
            // Assert
            assertThat(match).isTrue();
        }
    }

}