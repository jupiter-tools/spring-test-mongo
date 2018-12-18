package com.antkorwin.springtestmongo.internal.expect.graph;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class Printer {

    private final Graph graph;

    public Printer(Graph graph) {
        this.graph = graph;
    }

    public void print() {
        boolean[][] matrix = graph.calculate();
        System.out.println("Data / Matchers ---> ");
        for (boolean[] actual : matrix) {
            System.out.print("| ");
            for (boolean matcher : actual) {
                System.out.print((matcher ? "1" : "0") + " ");
            }
            System.out.println();
        }
        System.out.println("V");
    }
}
