package org.simplegraph.impl;

import org.junit.jupiter.api.BeforeEach;

import org.simplegraph.impl.*;
import org.simplegraph.*;

public class TestDirectedDenseGraph extends TestDirectedGraph {
    @BeforeEach
    public void init() {
        g = new DirectedDenseGraph<String>();
    }
}
