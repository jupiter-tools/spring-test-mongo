package com.antkorwin.springtestmongo.internal.expect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.fail;


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
