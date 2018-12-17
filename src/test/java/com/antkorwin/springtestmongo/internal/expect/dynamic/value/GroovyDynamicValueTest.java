package com.antkorwin.springtestmongo.internal.expect.dynamic.value;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 17.12.2018.
 *
 * @author Korovin Anatoliy
 */
class GroovyDynamicValueTest {

    @Test
    void isNecessary() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        boolean result = groovyDynamicValue.isNecessary("groovy: 1+2");
        // Asserts
        Assertions.assertTrue(result);
    }

    @Test
    void isNecessaryToo() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        boolean result = groovyDynamicValue.isNecessary("groovy:1+2");
        // Asserts
        Assertions.assertTrue(result);
    }

    @Test
    void notNecessary() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        boolean result = groovyDynamicValue.isNecessary("1+2");
        // Asserts
        Assertions.assertFalse(result);
    }

    @Test
    void notNecessaryWrongType() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        boolean result = groovyDynamicValue.isNecessary(12345);
        // Asserts
        Assertions.assertFalse(result);
    }

    @Test
    void evaluateSum() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        int sum = (int) groovyDynamicValue.evaluate("groovy: 1+2");
        // Asserts
        Assertions.assertEquals(sum, 3);
    }

    @Test
    void evaluateSumOfRange() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        int sum = (int) groovyDynamicValue.evaluate("groovy: (1..10).sum()");
        // Asserts
        Assertions.assertEquals(sum, 55);
    }

    @Test
    void evaluateWithError() {
        // Arrange
        GroovyDynamicValue groovyDynamicValue = new GroovyDynamicValue();
        // Act
        InternalException exception = Assertions.assertThrows(InternalException.class, () -> {
            groovyDynamicValue.evaluate("groovy: ()+()=( () )");
        });
        assertThat(exception.getMessage()).contains("Groovy engine evaluate error");
    }
}