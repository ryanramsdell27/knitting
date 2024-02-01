package dev.ryanramsdell.data;

public class BfsItem {
    public Stitch stitch;
    public int order;
    public BfsItem(Stitch stitch, int order){
        this.stitch = stitch;
        this.order = order;
    }
}
