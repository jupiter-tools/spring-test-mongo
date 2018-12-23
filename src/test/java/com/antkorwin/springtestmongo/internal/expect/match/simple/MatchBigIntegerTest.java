package com.antkorwin.springtestmongo.internal.expect.match.simple;

import com.antkorwin.springtestmongo.internal.expect.match.simple.MatchBigInteger;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.12.2018.
 *
 * @author Korovin Anatoliy
 */
class MatchBigIntegerTest {

    @Test
    void bigWithBig() {
        // Arrange
        BigInteger a = BigInteger.valueOf(1234);
        BigInteger b = BigInteger.valueOf(1234);
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void bigInteger() {
        // Arrange
        BigInteger a = BigInteger.valueOf(Long.MAX_VALUE);
        a = a.add(a);
        BigInteger b = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void bigWithLong() {
        // Arrange
        BigInteger a = BigInteger.valueOf(1234);
        Long b = 1234L;
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void longWithBig() {
        // Arrange
        Long a = 1234L;
        BigInteger b = BigInteger.valueOf(1234);
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void bigWithInt() {
        // Arrange
        BigInteger a = BigInteger.valueOf(1234);
        int b = 1234;
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void bigWithInteger() {
        // Arrange
        BigInteger a = BigInteger.valueOf(1234);
        Integer b = 1234;
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void intWithBig() {
        // Arrange
        int a = 1234;
        BigInteger b = BigInteger.valueOf(1234);
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isTrue();
    }

    @Test
    void bigWithFloatSame() {
        // Arrange
        BigInteger a = BigInteger.valueOf(12345);
        float b = 12345f;
        // Act
        boolean match = new MatchBigInteger().match(a, b);
        // Assert
        assertThat(match).isFalse();
    }
}