package dev.ryanramsdell.data;

import dev.ryanramsdell.enums.StitchType;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.StringJoiner;

public class Stitch {
    private final StitchType type;
    private int orderId;
    private Stitch predecessor;

    private Stitch successor;
    private Set<Stitch> parents;
    private Set<Stitch> children;

    private Point vertex;

    public Stitch(StitchType type, Stitch predecessor, Set<Stitch> parents, int orderId) {
        this.type = type;
        this.predecessor = predecessor;
        this.parents = parents;
        this.orderId = orderId;
    }
    public Stitch(int orderId) {
        this.type = StitchType.KNIT;
        this.parents = null;
        this.orderId = orderId;
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setSuccessor(Stitch successor) {
        this.successor = successor;
    }

    public Stitch getSuccessor() {
        return this.successor;
    }

    public Set<Stitch> getParents() {
        return parents;
    }

    public void setPredecessor(Stitch predecessor) {
        this.predecessor = predecessor;
    }

    public Stitch getPredecessor() {
        return predecessor;
    }

    public void setParents(Set<Stitch> parents) {
        this.parents = parents;
    }

    public void setChildren(Set<Stitch> children) {
        this.children = children;
    }

    public Set<Stitch> getChildren() {
        return this.children;
    }

    public void setVertex(Point vertex) {
        this.vertex = vertex;
    }

    public Point getVertex() {
        return vertex;
    }

    public Iterator<Stitch> iterator() {
        final Stitch[] curr = {this};
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return curr[0].successor != null;
            }

            @Override
            public Stitch next() {
                curr[0] = curr[0].successor;
                return curr[0];
            }
        };
    }

    @Override
    public String toString() {
        String pred = predecessor == null ? "null" : String.valueOf(predecessor.getOrderId());
        String succ = successor == null ? "null" : String.valueOf(successor.getOrderId());
        StringJoiner pars = new StringJoiner(",");
        pars.setEmptyValue("null");
        if(parents != null) {
            for(Stitch parent : parents)
                pars.add(String.valueOf(parent.getOrderId()));
        }

        StringJoiner chil = new StringJoiner(",");
        chil.setEmptyValue("null");
        if(children != null) {
            for(Stitch child : children)
                chil.add(String.valueOf(child.getOrderId()));
        }
        return "type=" + type +
                ",\t id=" + this.getOrderId() +
                ",\t pred=" +  pred +
                ",\t succ=" + succ +
                ",\t parents=" + pars +
                ",\t children=" + chil +
                ",\t vertex=" + vertex
                ;
    }

    public static Comparator<Stitch> getComparator() {
        return Comparator.comparingInt(o -> o.orderId);
    }
}
