package com.antkorwin.springtestmongo.internal.expect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created on 10.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
class ValidateGraphTest {

    @Test
    void validGraph() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            new ValidateGraph(new TestGraph(() -> graph)).validate();
        });
    }

    @Test
    void graphWithOneUnusedEntity() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, false},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act & Assert
        Error error = Assertions.assertThrows(Error.class, () -> {
            new ValidateGraph(new TestGraph(() -> graph)).validate();
        });
        System.out.println(error);
        new Printer(new TestGraph(()->graph)).print();
    }

    @Test
    void graphWithOneUnmatchedEntity() {
        // Arrange
        boolean[][] graph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, false, false, false}
                // @formatter:on
        };
        // Act & Assert
        Error error = Assertions.assertThrows(Error.class, () -> {
            new ValidateGraph(new TestGraph(() -> graph)).validate();
        });
        System.out.println(error);
    }
}