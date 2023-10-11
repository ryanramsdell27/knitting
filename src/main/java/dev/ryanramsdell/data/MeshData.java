package dev.ryanramsdell.data;

import java.util.List;

public class MeshData {
    List<Point> vertices;
    List<Edge> edges;

    public MeshData(List<Point> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Point> getVertices() {
        return this.vertices;
    }
    public List<Edge> getEdges() {
        return edges;
    }
}
