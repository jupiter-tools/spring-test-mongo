package com.antkorwin.springtestmongo.internal.expect;

import java.util.function.Supplier;

/**
 * Created on 10.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class TestGraph implements Graph {

    private final boolean[][] matrix;

    public TestGraph(Supplier<boolean[][]> matrix) {
        this.matrix = matrix.get();
    }

    @Override
    public boolean[][] calculate() {
        return matrix;
    }

    @Override
    public int matcherCount() {
        return matrix[0].length;
    }

    @Override
    public int patternCount() {
        return matrix.length;
    }
}
