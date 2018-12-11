package com.antkorwin.springtestmongo.internal.expect;

/**
 * Created on 10.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class ValidateGraph {

    private final Graph graph;

    public ValidateGraph(Graph graph) {
        this.graph = graph;
    }

    public void validate() {
        new AssertGraph(new IndexedGraph(graph)).doAssert();
    }

}
