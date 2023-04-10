package dev.ryanramsdell.data;

import java.util.HashSet;
import java.util.Set;

public class Edge {
    public int start;
    public int end;

    public Edge(Stitch start, Stitch end) {
        this.start = start.getOrderId();
        this.end = end.getOrderId();
    }

    public static Set<Edge> generateEdges(Stitch stitch) {
        Set<Edge> edges = new HashSet<>();
        if(stitch.getParents() != null) {
            for (Stitch parent : stitch.getParents()) {
                edges.add(new Edge(stitch, parent));
            }
        }
        if(stitch.getSuccessor() != null)
            edges.add(new Edge(stitch, stitch.getSuccessor()));
        if(stitch.getPredecessor() != null)
            edges.add(new Edge(stitch, stitch.getPredecessor()));
        return edges;
    }

    @Override
    public String toString() {
        return(String.format("(%d, %d)", start, end));
    }
}
