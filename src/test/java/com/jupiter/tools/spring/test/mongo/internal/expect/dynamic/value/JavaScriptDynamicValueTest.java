package com.jupiter.tools.spring.test.mongo.internal.expect.dynamic.value;

import javax.script.ScriptException;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 18.01.2019.
 *
 * @author Korovin Anatoliy
 */
class JavaScriptDynamicValueTest {

    @Nested
    class NecessaryTests {

        @Test
        void isNecessary() {
            // Act
            boolean necessary = new JavaScriptDynamicValue().isNecessary("js: some scripts");
            // Assert
            assertThat(necessary).isTrue();
        }

        @Test
        void notNecessary() {
            // Act
            boolean necessary = new JavaScriptDynamicValue().isNecessary("1<2<3");
            // Assert
            assertThat(necessary).isFalse();
        }

        @Test
        void notStringValue() {
            // Act
            boolean necessary = new JavaScriptDynamicValue().isNecessary(123);
            // Assert
            assertThat(necessary).isFalse();
        }
    }

    @Nested
    class EvaluateTests {

        @Test
        void doubleInversion() {
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: !!undefined");
            // Assert
            assertThat(result).isEqualTo(false);
        }

        @Test
        void doubleInversionOfStrings() {
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: !!'false'  == !!'true'");
            // Assert
            assertThat(result).isEqualTo(true);
        }


        @Test
        void typeOfNull() {
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: typeof null === 'object'");
            // Assert
            assertThat(result).isEqualTo(true);
        }

        @Test
        void banana() {
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: 'b' + 'a' + + 'a' + 'a'");
            // Assert
            assertThat(result).isEqualTo("baNaNa");
        }

        @Test
        void nanNotNan() {
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: NaN!==NaN");
            // Assert
            assertThat(result).isEqualTo(true);
        }

        @Test
        void functionCall() {
            // Arrange
            // Act
            Object result = new JavaScriptDynamicValue().evaluate("js: (function (){return 123})()");
            // Assert
            assertThat(result).isEqualTo(123);
        }
    }

    @Nested
    class CheckFailedCases {

        @Test
        void wrongScript() {
            // Arrange
            JavaScriptDynamicValue dynamicValue = new JavaScriptDynamicValue();
            // Act
            InternalException exception =
                    Assertions.assertThrows(InternalException.class,
                                            () -> dynamicValue.evaluate("js: wrong javascript expression"));
            // Assert
            assertThat(exception.getCause()).isInstanceOf(ScriptException.class);
        }
    }
}