package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import java.util.concurrent.TimeUnit;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
class TimeDescriptionTest {

    @Nested
    class MatchTests {

        @Test
        void now() {
            // Act
            TimeDescription description = new TimeDescription("[NOW]");
            // Asserts
            assertThat(description.matches()).isTrue();
        }

        @Test
        void plusDays() {
            // Act
            TimeDescription description = new TimeDescription("[NOW]+17(DAYS)");
            // Asserts
            assertThat(description.matches()).isTrue();
        }

        @Test
        void minusDays() {
            // Act
            TimeDescription description = new TimeDescription("[NOW]-17(DAYS)");
            // Asserts
            assertThat(description.matches()).isTrue();
        }

        @Test
        void notValid() {
            // Act
            TimeDescription description = new TimeDescription("NOW+123");
            // Asserts
            assertThat(description.matches()).isFalse();
        }

        @Test
        void notMatch() {
            // Act
            TimeDescription description = new TimeDescription("[NOW]+17DAYS");
            // Asserts
            assertThat(description.matches()).isFalse();
        }
    }


    @Nested
    class GetTimeOpTests {

        @Test
        void now() {
            // Arrange
            TimeOperation now = new TimeOperation(TimeDirection.UNDEFINED, null, 0);
            TimeDescription description = new TimeDescription("[NOW]");
            // Act & Asserts
            assertThat(description.getTimeOperation()).isEqualToComparingFieldByField(now);
        }

        @Test
        void plusDays() {
            // Arrange
            TimeOperation result = new TimeOperation(TimeDirection.PLUS, TimeUnit.DAYS, 17);
            TimeDescription description = new TimeDescription("[NOW]+17(DAYS)");
            // Act & Asserts
            assertThat(description.getTimeOperation()).isEqualToComparingFieldByField(result);
        }

        @Test
        void minusDays() {
            // Arrange
            TimeOperation result = new TimeOperation(TimeDirection.MINUS, TimeUnit.DAYS, 1);
            TimeDescription description = new TimeDescription("[NOW]-1(DAYS)");
            // Act & Asserts
            assertThat(description.getTimeOperation()).isEqualToComparingFieldByField(result);
        }

        @Test
        void notMatch() {
            // Arrange
            // Act
            TimeDescription description = new TimeDescription("NOW -1 DAYS");
            // Asserts
            Assertions.assertThrows(IllegalStateException.class, description::getTimeOperation);
        }
    }

    @Nested
    class ThresholdTests {

        @Test
        void defaultValue() {
            // Act
            long threshold = new TimeDescription("[NOW]").getThreshold();
            // Asserts
            assertThat(threshold).isEqualTo(10_000);
        }

        @Test
        void defaultThrAndTime() {
            // Act
            long threshold = new TimeDescription("[NOW]+3(MINUTES)").getThreshold();
            // Asserts
            assertThat(threshold).isEqualTo(10_000);
        }

        @Test
        void setThreshold() {
            // Act
            long threshold = new TimeDescription("[NOW]{THR=123}").getThreshold();
            // Asserts
            assertThat(threshold).isEqualTo(123);
        }

        @Test
        void setThresholdAndTime() {
            // Act
            long threshold = new TimeDescription("[NOW]+3(MINUTES){THR=1234}").getThreshold();
            // Asserts
            assertThat(threshold).isEqualTo(1234);
        }
    }


}