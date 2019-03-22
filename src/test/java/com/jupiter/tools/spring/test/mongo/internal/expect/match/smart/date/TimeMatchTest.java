package com.jupiter.tools.spring.test.mongo.internal.expect.match.smart.date;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 22.12.2018.
 *
 * @author Korovin Anatoliy
 */
class TimeMatchTest {

    @Test
    void matchPositive() {
        // Arrange
        Date now = new Date();
        Date expected = new Date(now.getTime() + TimeUnit.DAYS.toMillis(1));

        TimeOperation operation = new TimeOperation(TimeDirection.PLUS, TimeUnit.DAYS, 1);
        // Act
        boolean match = new TimeMatch(now, operation, 0).match(expected);
        // Asserts
        assertThat(match).isTrue();
    }

    @Test
    void matchNegative() {
        // Arrange
        Date now = new Date();
        Date expected = new Date(now.getTime() - TimeUnit.DAYS.toMillis(1));

        TimeOperation operation = new TimeOperation(TimeDirection.MINUS, TimeUnit.DAYS, 1);
        // Act
        boolean match = new TimeMatch(now, operation, 0).match(expected);
        // Asserts
        assertThat(match).isTrue();
    }

    @Test
    void matchWithThreshold() {
        // Arrange
        Date now = new Date();
        Date expected = new Date(now.getTime() + TimeUnit.DAYS.toMillis(1) + 100);

        TimeOperation operation = new TimeOperation(TimeDirection.PLUS, TimeUnit.DAYS, 1);
        // Act
        boolean match = new TimeMatch(now, operation, 100).match(expected);
        // Asserts
        assertThat(match).isTrue();
    }

    @Test
    void matchMock() {
        // Arrange
        Date now = new Date();
        TimeOperation operation = mock(TimeOperation.class);
        when(operation.evaluate(any())).thenReturn(now);
        // Act
        boolean match = new TimeMatch(now, operation, 0).match(now);
        // Asserts
        assertThat(match).isTrue();
    }
}