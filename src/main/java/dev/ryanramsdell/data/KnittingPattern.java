package dev.ryanramsdell.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        Stitch st = new Stitch(type, start, Set.of(liveStart), count++);
        last.setSuccessor(st);
        st.setPredecessor(last);
        last = st;
        stitches.add(st);
        st.setParents(Set.of(liveStart));
        liveStart.setChildren(Set.of(st));
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
}
