package dev.ryanramsdell.data;

import dev.ryanramsdell.io.MDS;

import java.util.*;

public class KnittingPattern {
    private int count;
    private Stitch start;
    private Stitch last;
    private Stitch liveStart;
    private Stitch liveEnd;
    private int numCastOn;
    private Set<Stitch> stitches;

    public KnittingPattern(int numStitches) {
        count = 0;
        start = new Stitch(count++);
        liveStart = start;
        last = start;
        stitches = new HashSet<>();
        stitches.add(start);
        castOn(numStitches - 1);
        numCastOn = numStitches;
    }

    public Stitch knit(int numStitches) {
        if(numStitches <= 0) return start;
        for(int i = 0; i < numStitches; i++) {
            knit();
        }
        return last;
    }
    public Stitch knit() {
        return basicStitch(StitchType.KNIT);
    }

    public Stitch purl(int numStitches) {
        if(numStitches <= 0) return start;
        for(int i = 0; i < numStitches; i++) {
            purl();
        }
        return last;
    }

    public Stitch purl() {
        return basicStitch(StitchType.PURL);
    }

    public Stitch basicStitch(StitchType type) {
        Set<Stitch> parents = new HashSet<>(Set.of(liveStart));
        Stitch st = new Stitch(type, start, parents, count++);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
//        st.setParents(Set.of(liveStart));
        liveStart.setChildren(new HashSet<>(Set.of(st)));
        liveStart = liveStart.getSuccessor();
        liveEnd = st;
        return last;
    }

    public Stitch castOn(int numStitches) {
        if(numStitches <= 0) return start;
        for(int i = 0; i < numStitches; i++) {
            castOn();
        }
        return last;
    }
    public Stitch castOn() {
        Stitch st = new Stitch(StitchType.KNIT, start, null, count++);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
        liveEnd = st;
        return last;
    }

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

    public void k2tog() {
        decrease(StitchType.KNIT, 2);
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
//        for( int i = 0; i < out[0].length; i++){
//            for(int j = 0; j < out[0].length; j++){
//                out[i][j] = 10000;
//            }
//        }
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
//        dis[stitch.getOrderId()] = 0.1;
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
//        System.out.println(stitch.getOrderId() + " " + Arrays.toString(Arrays.stream(dis).toArray()));
    }
    private double[][] computeDissBFS() {
        double[][] out = new double [count][count];
        for(Stitch stitch : stitches) {
            double[] dis = new double[count];
            BFS(stitch, dis, 2);
//            for(int i = 0; i < count; i++){
//                out[stitch.getOrderId()][i] = dis[i];
//            }
            out[stitch.getOrderId()] = dis;
            out[stitch.getOrderId()][stitch.getOrderId()] = 0.001;
        }
        return out;
    }

    public double[][] computeDissimilarity() {
//        double[][] out = computeDissImmediateNeighbor();
        double[][] out = computeDissBFS();
//        System.out.println(Arrays.deepToString(out).replace("], ", "],\n"));
        return out;
    }

    public double[][] computeMDS() {
        return MDS.computeMDS(computeDissimilarity());
    }
}
