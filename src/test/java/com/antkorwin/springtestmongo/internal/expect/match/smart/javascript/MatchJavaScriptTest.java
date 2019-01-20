package com.antkorwin.springtestmongo.internal.expect.match.smart.javascript;

import com.antkorwin.commonutils.exceptions.InternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.01.2019.
 *
 * @author Korovin Anatoliy
 */
class MatchJavaScriptTest {

    @Nested
    class MatchTests {

        @Test
        void simpleMatch() {
            // Arrange
            MatchJavaScript match = new MatchJavaScript();
            // Act
            boolean result = match.match(5111987, "js-match: value == 5111987");
            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void matchWithJsFunction() {
            // Arrange
            MatchJavaScript match = new MatchJavaScript();
            // Act
            boolean result = match.match(123, "js-match: (function (){return value == 123})()");
            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void notMatch() {
            // Arrange
            MatchJavaScript match = new MatchJavaScript();
            // Act
            boolean result = match.match(123, "js-match: value == 5111987");
            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void matchDiv() {
            // Arrange
            MatchJavaScript match = new MatchJavaScript();
            // Act
            boolean result = match.match(32, "js-match: value % 2 == 0");
            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void wrongScript() {
            // Arrange
            MatchJavaScript matcher = new MatchJavaScript();
            // Act & Assert
            Exception exception =
                    Assertions.assertThrows(Exception.class,
                                            () -> matcher.match("123", "js-match: a+d.df.fr()=>frf"));
            assertThat(exception.getMessage()).isEqualTo("JS engine evaluate error");
        }

        @Test
        void wrongResultType() {
            // Arrange
            MatchJavaScript matcher = new MatchJavaScript();
            // Act & Assert
            Exception exception =
                    Assertions.assertThrows(InternalException.class,
                                            () -> matcher.match("123", "js-match: 4+3"));
            assertThat(exception.getMessage()).isEqualTo("js-match: must return a boolean value instead of {7}");
        }
    }

    @Nested
    class NecessaryTests {

        @Test
        void isNecessary() {
            // Act
            boolean result = new MatchJavaScript().isNecessary("js-match: 1+2");
            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void isNotNecessaryWhenWrongPrefix() {
            // Act
            boolean result = new MatchJavaScript().isNecessary("match: 1+2");
            // Assert
            assertThat(result).isFalse();
        }

        @Test
        void isNotNecessaryWhenWrongType() {
            // Act
            boolean result = new MatchJavaScript().isNecessary(123);
            // Assert
            assertThat(result).isFalse();
        }
    }

}