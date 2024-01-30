package dev.ryanramsdell.data;

import dev.ryanramsdell.enums.DissimilarityAlgorithm;
import dev.ryanramsdell.enums.StitchType;
import dev.ryanramsdell.io.MDS;

import java.util.*;

/**
 * Implementation of the KPattern interface
 * Structure of a knitted object is stored as a set of Stitches
 * Knitting operations defined by KPattern interface determine how this
 * set is constructed and modified
 */
public class KnittingPattern implements KPattern {
    private int count;
    private Stitch start;
    private Stitch last;
    private Stitch liveStart;
    private Stitch liveEnd;
    private int numCastOn;
    private Set<Stitch> stitches;


    /**
     * Casts on numStitches. This class implicitly joins in the round
     * @param numStitches Number of stitches to cast on
     * @param join Whether to join in the round
     */
    @Override
    public void init(int numStitches, boolean join) {
        this.count = 0;
        this.start = new Stitch(this.count++);
        this.liveStart = this.start;
        this.last = this.start;
        this.stitches = new HashSet<>();
        this.stitches.add(this.start);
        this.castOn(numStitches - 1);
        this.numCastOn = numStitches;
    }
    @Override
    public Stitch basicStitch(StitchType type) {
        Set<Stitch> parents = new HashSet<>(Set.of(liveStart));
        Stitch st = new Stitch(type, start, parents, count++);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
        liveStart.setChildren(new HashSet<>(Set.of(st)));
        liveStart = liveStart.getSuccessor();
        liveEnd = st;
        return last;
    }

    private Stitch castOn(int numStitches) {
        if(numStitches <= 0) return start;
        for(int i = 0; i < numStitches; i++) {
            castOn();
        }
        return last;
    }
    private Stitch castOn() {
        Stitch st = new Stitch(StitchType.KNIT, start, null, count++);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
        liveEnd = st;
        return last;
    }
    @Override
    public void decrease(StitchType type, int num) {
        Set<Stitch> parents = new HashSet<>();
        Stitch st = new Stitch(type, start, null, count++);
        for(int i = 0; i < num; i++) {
            parents.add(liveStart);
            liveStart.setChildren(Set.of(st));
            liveStart = liveStart.getSuccessor();
        }
        st.setParents(parents);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
        liveEnd = st;
    }
    @Override
    public void increase(StitchType type, int num) {
        Set<Stitch> parents = new HashSet<>(Set.of(liveStart));
        Set<Stitch> children = new HashSet<>();
        for(int i = 0; i < num + 1; i++){
            Stitch st = new Stitch(type, start, parents, count++);
            last.setSuccessor(st);
            st.setPredecessor(last);
            last = st;
            stitches.add(st);
            children.add(st);
        }

        liveStart.setChildren(children);
        liveStart = liveStart.getSuccessor();
        liveEnd = last;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(start.toString());
        Iterator<Stitch> iterator = start.iterator();
        while(iterator.hasNext()){
            sb.append("\n");
            sb.append(iterator.next());
        }
        return sb.toString();
    }

    public Stitch getStart() {
        return start;
    }

    public int getNumCastOn() {
        return numCastOn;
    }

    public Set<Stitch> getStitches() {
        return stitches;
    }

    private double[][] computeDissImmediateNeighbor() {
        double[][] out = new double [count][count];
        for(Stitch stitch : stitches) {
            Stitch pred = stitch.getPredecessor();
            Stitch succ = stitch.getSuccessor();
            Set<Stitch> children = stitch.getChildren();
            Set<Stitch> parents = stitch.getParents();
            Set<Stitch> neighbors = new HashSet<>();
            if(children != null) neighbors.addAll(children);
            if(parents != null) neighbors.addAll(parents);
            if(pred != null) out[stitch.getOrderId()][pred.getOrderId()] = 1;
            if(succ != null) out[stitch.getOrderId()][succ.getOrderId()] = 1;
            for (Stitch parent : neighbors) out[stitch.getOrderId()][parent.getOrderId()] = 1;
            out[stitch.getOrderId()][stitch.getOrderId()] = 0.01;
        }
        return out;
    }


    private void BFS(Stitch stitch, double[] dis, int maxOrder) {
        LinkedList<BfsItem> queue = new LinkedList<>();

        queue.add(new BfsItem(stitch, 0));
        while(queue.size() > 0) {
            BfsItem bs = queue.removeFirst();
            Stitch s = bs.stitch;
            if(dis[s.getOrderId()] != 0 && dis[s.getOrderId()] < bs.order){

            }
            else {
                if (dis[s.getOrderId()] == 0 || dis[s.getOrderId()] > bs.order) dis[s.getOrderId()] = bs.order;
                Stitch pred = s.getPredecessor();
                Stitch succ = s.getSuccessor();
                Set<Stitch> children = s.getChildren();
                Set<Stitch> parents = s.getParents();
                Set<Stitch> neighbors = new HashSet<>();
                if (children != null) neighbors.addAll(children);
                if (parents != null) neighbors.addAll(parents);
                if (pred != null) neighbors.add(pred);
                if (succ != null) neighbors.add(succ);
                for (Stitch neighbor : neighbors) {
                    if (dis[neighbor.getOrderId()] == 0 && neighbor != stitch && (bs.order < maxOrder || maxOrder == 0)) {
                        queue.add(new BfsItem(neighbor, bs.order + 1));
                    }
                }
            }

        }
    }
    private double[][] computeDissBFS() {
        double[][] out = new double [count][count];
        for(Stitch stitch : stitches) {
            double[] dis = new double[count];
            BFS(stitch, dis, 2);
            out[stitch.getOrderId()] = dis;
            out[stitch.getOrderId()][stitch.getOrderId()] = 0.001;
        }
        return out;
    }


    public double[][] computeDissimilarity(DissimilarityAlgorithm algorithm) {
        switch (algorithm) {
            case IMMEDIATE_NEIGHBOR:
                return computeDissImmediateNeighbor();
            case DISS_BFS:
            default:
                return computeDissBFS();
        }
    }

    public double[][] computeMDS() {
        return MDS.computeMDS(computeDissimilarity(DissimilarityAlgorithm.DEFAULT));
    }
}
