package org.simplegraph.impl;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.LinkedList;

import org.simplegraph.Graph;

abstract class BaseUndirectedDenseGraph<V, E> extends BaseDenseGraph<V> {
    protected ArrayList<E> edges;

    public BaseUndirectedDenseGraph() {
        initialize(DEFAULT_SIZE);
    }

    public BaseUndirectedDenseGraph(int size) {
        initialize(size);
    }

    protected void copy(BaseUndirectedDenseGraph<V, E> graph) {
        initialize(graph.size);
        copyVertices(graph);

        int edgesSize = graph.getEdgesSize(size);
        for (int i = 0; i < edgesSize; i++) {
            edges.add(i, graph.edges.get(i));
        }
    }

    /**
     * Initialize all attributes for storing graph data.
     * @param startSize Number of vertices to store initially in the graph.
     */
    protected void initialize(int startSize) {
        super.initialize(startSize);

        int edgesSize = getEdgesSize(startSize);
        edges = new ArrayList<E>(edgesSize);

        // Fill edges array with null
        for (int i = 0; i != edgesSize; ++i) {
            edges.add(i, null);
        }
    }

    /**
     * Grow the graph size to the specified size
     * @param newSize New size for the graph.
     */
    public void grow(int newSize) {
        int oldEdgesSize = getEdgesSize(size);
        super.grow(newSize);
        int edgesSize = getEdgesSize(size);
        edges.ensureCapacity(edgesSize);

        for (int i = oldEdgesSize; i != edgesSize; ++i) {
            edges.add(null);
        }
    }

    /**
     * Get how much edges there are for the size passed.
     * @param verticesSize Number of vertices.
     * @return Number of edges based on the number of vertices.
     */
    protected int getEdgesSize(int verticesSize) {
        if (verticesSize == 0) {
            return 0;
        }

        return verticesSize * (verticesSize - 1)  / 2;
    }

    /**
     * Remove a vertex from the graph
     * @param  vertex the vertex to remove
     * @return        true if the graph has been modified
     */
    public boolean removeVertex(V vertex) {
        Integer vertexIndex = verticesMap.get(vertex);

        if (!super.removeVertex(vertex)) {
            return false;
        }

        // The last vertex has been removed
        // and verticesCount has already been decreased
        if (verticesCount == 0) {
            return true;
        }

        // If the one to remove is already the last
        if (verticesCount == vertexIndex) {
            int first = getEdgesSize(vertexIndex);
            int last = first + verticesCount;
            for (int i = first; i != last; i++) {
                edges.set(i, null);
            }

            return true;
        }

        int rowLastVertex = getEdgesSize(verticesCount);

        int i = 0;
        int row = getEdgesSize(vertexIndex);

        // Swap edges too
        while (i != vertexIndex) {
            edges.set(row + i, edges.get(rowLastVertex + i));
            edges.set(rowLastVertex + i, null);
            i++;
        }

        row += vertexIndex;
        i++;

        while (i != verticesCount) {
            row += vertexIndex;
            edges.set(row, edges.get(rowLastVertex + i));
            edges.set(rowLastVertex + i, null);
            i++;
        }

        return true;
    }

    protected boolean addSingleEdge(V v1, V v2, E edge) {
        // null is not a valid vertex nor a valid edge
        if (v1 == null || v2 == null || edge == null) {
            return false;
        }

        if (v1.equals(v2)) {
            return false;
        }

        addVertex(v1);
        addVertex(v2);
        // edge is not null, hence we can call a method on it
        return !edge.equals(edges.set(getEdgeIndex(v1, v2), edge));
    }

    protected E getSingleEdge(V v1, V v2) {
        return edges.get(getEdgeIndex(v1, v2));
    }

    /**
     * Check the existence of an edge between the two vertices
     * @param  v1 first vertex
     * @param  v2 second vertex
     * @return    true if an edge between v1 and v2
     */
    public boolean existsEdge(V v1, V v2) {
        return edges.get(getEdgeIndex(v1, v2)) != null;
    }

    /**
     * Get index of the edge between v1 and v2.
     * @param v1 first vertex
     * @param v2 second vertex
     * @return   index of the edge between v1 and v2
     */
    protected int getEdgeIndex(V v1, V v2) {
        Integer i1 = getVertexIndex(v1);
        Integer i2 = getVertexIndex(v2);

        if (i1 == null || i2 == null) {
            return -1;
        }

        if (i1.equals(i2)) {
            return -2;
        }

        if (i1 > i2) {
            return i1 * (i1 - 1) / 2 + i2;
        } else {
            return i2 * (i2 - 1) / 2 + i1;
        }
    }

    /**
     * Remove an edge that goes from the first vertex to the second.
     * @param  v1 first vertex
     * @param  v2 second vertex
     * @return    true if the graph has been changed
     */
    public boolean removeEdge(V v1, V v2) {
        return edges.set(getEdgeIndex(v1, v2), null) != null;
    }

    /**
     * Get the neighbors of a vertex, both incident and outer vertices.
     * @param  vertex the specified vertex
     * @return        the list of vertices that are neighbors of vertex
     *                null if the vertex is not contained in the graph
     */
    public List<V> getNeighbors(V vertex)
    {
        if (!containsVertex(vertex)) {
            return null;
        }

        int i = 0;
        ArrayList<V> neighbor = new ArrayList<V>(verticesCount);
        int vertexIndex = verticesMap.get(vertex);
        int row = getEdgesSize(vertexIndex);


        while (i != vertexIndex) {
            if (edges.get(row + i) != null) {
                neighbor.add(verticesArray.get(i));
            }

            i++;
        }

        row += i; // i == vertexIndex

        i++;

        while (i != verticesCount) {
            if (edges.get(row + vertexIndex) != null) {
                neighbor.add(verticesArray.get(i));
            }

            row += i;

            i++;
        }

        return new LinkedList<V>(neighbor);
    }

    /**
     * Get the number of neighbors for a vertex.
     * @param  vertex the spefied vertex
     * @return        number of neighbors, -1 if vertex does not exists
     */
    public int countNeighbors(V vertex) {
        if (!containsVertex(vertex)) {
            return -1;
        }

        int i = 0;
        int count = 0;
        int vertexIndex = verticesMap.get(vertex);
        int row = getEdgesSize(vertexIndex);

        while (i != vertexIndex) {
            if (edges.get(row + i) != null) {
                count++;
            }

            i++;
        }

        row += vertexIndex;

        while (++i != verticesCount) {
            row += vertexIndex;
            if (edges.get(row) != null) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get the number of edges in the graph.
     * @return number of edges
     */
    public int countEdges() {
        int count = 0;
        int lastEdge = verticesCount * (verticesCount - 1) / 2;

        for (int i = 0; i != lastEdge; i++) {
            if (edges.get(i) != null) {
                count++;
            }
        }

        return count;
    }
    /**
     * Get a path between a source and a destination
     * @param source      source vertex
     * @param destination destination vertex
     * @return            a list containing the vertices that
     *                    compose the path, in order; an empty list if
     *                    there is no path, null if the source and the
     *                    destination are equals or are not contained in the
     *                    graph
     */
    public List<V> getPath(V source, V destination) {
        int i1 = getVertexIndex(source);
        int i2 = getVertexIndex(destination);

        if (i1 == -1 || i2 == -1) {
            return null;
        }

        if (i1 == i2) {
            return null;
        }

        int parent[] = new int[verticesCount];
        Arrays.fill(parent, -1);
        ArrayDeque<Integer> queue = new ArrayDeque<Integer>(verticesCount * verticesCount / 2);
        queue.add(i1);

        Integer current = -1;
        int row;

        while ((current = queue.pollFirst()) != null) {

            if (current == i2) {
                break;
            }

            row = getEdgesSize(current);
            int k = 0;

            while (k != current) {
                if (edges.get(row + k) != null && parent[k] == -1) {
                    queue.add(k);
                    parent[k] = current;
                }

                k++;
            }

            row += k;

            k++;

            while (k != verticesCount) {
                if (edges.get(row + current) != null && parent[k] == -1) {
                    queue.add(k);
                    parent[k] = current;
                }

                row += k;

                k++;
            }
        }

        LinkedList<V> path = new LinkedList<V>();

        if (current != null && current == i2) {

            do {
                path.addFirst(verticesArray.get(current));
            } while ((current = parent[current]) != i1);

            path.addFirst(source);
            return path;
        }

        return path;
    }

    /**
     * Does a path exists between source and destination.
     * @param source      source vertex
     * @param destination destination vertex
     * @return            true if a path exists
     */
    public boolean existsPath(V source, V destination) {
        List<V> path = getPath(source, destination);
        if (path == null) {
            return false;
        }

        return !path.isEmpty();
    }

    protected BaseUndirectedDenseGraph<V,E> _getSpanningTree() {
        return null;
    }

    public Graph<V> getShortestPath() {
        return null;
    }
}
