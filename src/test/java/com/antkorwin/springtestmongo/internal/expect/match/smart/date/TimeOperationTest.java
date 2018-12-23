package com.antkorwin.springtestmongo.internal.expect.match.smart.date;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
class TimeOperationTest {

    @Test
    void dayPlusOne() {
        // Arrange
        Date now = new Date();
        TimeOperation plusDay = new TimeOperation(TimeDirection.PLUS, TimeUnit.DAYS, 1);
        // Act
        Date tomorrow = plusDay.evaluate(now);
        // Asserts
        long duration = tomorrow.getTime() - now.getTime();
        assertThat(duration).isEqualTo(TimeUnit.DAYS.toMillis(1));
    }

    @Test
    void dayMinusOne() {
        // Arrange
        Date now = new Date();
        TimeOperation minusDay = new TimeOperation(TimeDirection.MINUS, TimeUnit.DAYS, 1);
        // Act
        Date yesterday = minusDay.evaluate(now);
        // Asserts
        long duration = now.getTime() - yesterday.getTime();
        assertThat(duration).isEqualTo(TimeUnit.DAYS.toMillis(1));
    }

    @Test
    void nop() {
        // Arrange
        Date now = new Date();
        TimeOperation nop = new TimeOperation(TimeDirection.UNDEFINED, null, 0);
        // Act
        Date result = nop.evaluate(now);
        // Asserts
        long duration = result.getTime() - now.getTime();
        assertThat(duration).isEqualTo(0);
    }

    @Test
    void dayPlusTenDays() {
        // Arrange
        Date now = new Date();
        TimeOperation plusTenDays = new TimeOperation(TimeDirection.PLUS, TimeUnit.DAYS, 10);
        // Act
        Date tomorrow = plusTenDays.evaluate(now);
        // Asserts
        long duration = tomorrow.getTime() - now.getTime();
        assertThat(duration).isEqualTo(TimeUnit.DAYS.toMillis(10));
    }
}