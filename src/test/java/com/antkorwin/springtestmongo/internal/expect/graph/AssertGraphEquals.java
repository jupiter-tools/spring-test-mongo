package com.antkorwin.springtestmongo.internal.expect.graph;

import com.antkorwin.springtestmongo.internal.expect.graph.Graph;
import com.antkorwin.springtestmongo.internal.expect.graph.Printer;
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
            new Printer(first).print();
            System.out.println("Expected graph: ");
            new Printer(second).print();
            Assertions.fail("Graphs are not equal.");
        }
    }
}