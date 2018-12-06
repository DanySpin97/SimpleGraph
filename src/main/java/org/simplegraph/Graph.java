package org.simplegraph;

import java.util.List;

public interface Graph<V> extends GraphVertices<V> {
    boolean addEdge(V v1, V v2);

    int countEdges();

    Graph<V> getSpanningTree();
}

