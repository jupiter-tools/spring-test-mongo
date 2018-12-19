package com.antkorwin.springtestmongo.internal.expect.match;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
class MatchNumberTest {

    @Test
    void intVsLong() {
        // Arrange
        long a = 123L;
        int b = 123;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void intVsLongNotEquals() {
        // Arrange
        long a = 123L;
        int b = 1000;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isFalse();
    }

    @Test
    void intVsLongMaxValue() {
        // Arrange
        long a = Integer.MAX_VALUE;
        int b = Integer.MAX_VALUE;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void intVsLongOutOfRange() {
        // Arrange
        long a = (long)(Integer.MAX_VALUE)+1;
        int b = Integer.MAX_VALUE+1;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isFalse();
    }

    @Test
    void longVsLong() {
        // Arrange
        long a = (long)(Integer.MAX_VALUE)+1;
        long b = (long)(Integer.MAX_VALUE)+1;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void longVsInt() {
        // Arrange
        int a = 123;
        long b = 123L;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void longVsIntNotEquals() {
        // Arrange
        int a = 123;
        long b = 1000L;
        // Act
        boolean match = new MatchNumber().match(a, b);
        // Assert
        assertThat(match).isFalse();
    }
}