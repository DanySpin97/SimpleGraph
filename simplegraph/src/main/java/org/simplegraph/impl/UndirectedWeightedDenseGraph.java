package org.simplegraph.impl;

import java.util.List;

import org.simplegraph.WeightedGraph;
import org.simplegraph.util.Algorithms;

/**
 * Dense graph implemention for weighted graphs.
 */
public class UndirectedWeightedDenseGraph<V> extends BaseUndirectedDenseGraph<V, Double> implements WeightedGraph<V> {
    public UndirectedWeightedDenseGraph() {
        super();
    }

    public UndirectedWeightedDenseGraph(int size) {
        super(size);
    }

    public UndirectedWeightedDenseGraph(UndirectedWeightedDenseGraph<V> graph) {
        copy(graph);
    }

    /**
     * Add an edge between two vertices.
     * Add the two vertices in the graph if they don't exists
     * @param  v1     first vertex
     * @param  v2     second vertex
     * @param  weight weight to add
     * @return      true if the graph has been modified
     */
    public boolean addEdge(V v1, V v2, Double weight) {
        return addSingleEdge(v1, v2, weight);
    }

    /**
     * Get the edge between two vertices
     * @param  v1   first vertex
     * @param  v2   second vertex
     * @return      the edge between v1 and v2, if it exists, null otherwise
     */
    public Double getWeight(V v1, V v2) {
        return getSingleEdge(v1, v2);
    }

    public WeightedGraph<V> getSpanningTree() {
        // TODO
        return null;
    }

    public Double getMinimumDistance(V source, V destination) {
        return Algorithms.<V>getMinimumDistance(this, source, destination);
    }

    public List<V> getShortestPath(V source, V destination) {
        return Algorithms.<V>getShortestPath(this, source, destination);
    }

    public WeightedGraph<V> getMinimumSpanningTree() {
        return getMinimumSpanningTree();
    }
}