package com.jupiter.tools.spring.test.mongo.internal.expect.graph;

import org.junit.jupiter.api.Test;

/**
 * Created on 09.12.2018.
 *
 * @author Korovin Anatoliy
 */
class ReachabilityGraphTest {

    @Test
    void singleTransposition() {
        // Arrange
        boolean[][] sourceGraph = {
                // @formatter:off
                {true,  false, false},
                {false, true,  true},
                {true,  true,  true}
                // @formatter:on
        };

        boolean[][] expectedGraph = {
                // @formatter:off
                {true,  false, false},
                {false, true,  true},
                {false, true,  true}
                // @formatter:on
        };

        // Act
        ReachabilityGraph resultGraph = new ReachabilityGraph(new TestGraph(() -> sourceGraph));

        // Asserts
        //new Printer(resultGraph).print();
        new AssertGraphEquals(resultGraph, new TestGraph(() -> expectedGraph)).check();
    }

    @Test
    void doubleTranspositions() {
        // Arrange
        boolean[][] sourceGraph = {
                // @formatter:off
                {true,  false, false, true},
                {false, true,  true,  false},
                {true,  true,  true,  false},
                {false, false, false, true}
                // @formatter:on
        };

        boolean[][] expectedGraph = {
                // @formatter:off
                {true,  false, false, false},
                {false, true,  true,  false},
                {false, true,  true,  false},
                {false, false, false, true}
                // @formatter:on
        };

        // Act
        ReachabilityGraph resultGraph = new ReachabilityGraph(new TestGraph(() -> sourceGraph));

        // Asserts
        new AssertGraphEquals(resultGraph, new TestGraph(() -> expectedGraph)).check();
    }

    @Test
    void multipleTranspositions() {
        // Arrange
        boolean[][] sourceGraph = {
                // @formatter:off
                {true,  false, false, true,  false},
                {false, true,  true,  false, false},
                {true,  true,  true,  false, false},
                {false, false, false, true,  false},
                {false, false, true,  true,  false}
                // @formatter:on
        };


        boolean[][] expectedGraph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, false},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        new Printer(new TestGraph(() -> sourceGraph)).print();
        // Act
        ReachabilityGraph resultGraph = new ReachabilityGraph(new TestGraph(() -> sourceGraph));

        // Asserts
        new AssertGraphEquals(resultGraph, new TestGraph(() -> expectedGraph)).check();
    }

    @Test
    void multipleTranspositionsSecond() {
        // Arrange
        boolean[][] sourceGraph = {
                // @formatter:off
                {true,  false, false, true,  false},
                {false, true,  true,  false, false},
                {true,  true,  true,  false, true},
                {false, false, false, true,  false},
                {false, false, true,  true,  false}
                // @formatter:on
        };


        boolean[][] expectedGraph = {
                // @formatter:off
                {true,  false, false, false, false},
                {false, true,  false, false, false},
                {false, false, false, false, true},
                {false, false, false, true,  false},
                {false, false, true,  false, false}
                // @formatter:on
        };
        // Act
        ReachabilityGraph resultGraph = new ReachabilityGraph(new TestGraph(() -> sourceGraph));

        // Asserts
        new AssertGraphEquals(resultGraph, new TestGraph(() -> expectedGraph)).check();
    }
}