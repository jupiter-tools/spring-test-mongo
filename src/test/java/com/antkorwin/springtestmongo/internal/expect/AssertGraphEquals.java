package com.antkorwin.springtestmongo.internal.expect;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public class AssertGraphEquals {
    private final Graph first;
    private final Graph second;

    public AssertGraphEquals(Graph first, Graph second) {
        this.first = first;
        this.second = second;
    }

    public void check() {
        boolean[][] firstMatrix = first.calculate();
        boolean[][] secondMatrix = second.calculate();
        boolean result = Arrays.deepEquals(firstMatrix, secondMatrix);
        if (!result) {
            System.out.println("Actual graph: ");
            new Printer(firstMatrix).print();
            System.out.println("Expected graph: ");
            new Printer(secondMatrix).print();
            Assertions.fail("Graphs are not equal.");
        }
    }
}