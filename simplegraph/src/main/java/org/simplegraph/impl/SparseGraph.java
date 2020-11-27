package org.simplegraph.impl;

import org.simplegraph.Graph;

/**
 Sparse graph implementation.
 */
public class SparseGraph<V> extends BaseSparseGraph<V, Boolean> implements Graph<V> {
    /**
     * Default constructor
     */
    public SparseGraph() {
        super();
    }

    /**
     * Create a graph with a starting size.
     * @param size starting size
     */
    public SparseGraph(int size) {
        super(size);
    }

    /**
     * Add an edge between two vertices.
     * Add the two vertices in the graph if they don't exists
     * @param  v1 first vertex
     * @param  v2 second vertex
     * @return    true if the graph has been modified
     */
    public boolean addEdge(V v1, V v2) {
        boolean a = addSingleEdge(v1, v2, true);
        boolean b = addSingleEdge(v2, v1, true);
        return a || b;
    }

    public Graph<V> getSpanningTree() {
        return new SparseGraph<V>();
    }
}
