package dev.ryanramsdell.io;

import mdsj.MDSJ;

public class MDS {
    public static double[][] computeMDS(double[][] dissimilarity) {
        return MDSJ.classicalScaling(dissimilarity, 3);
    }
}
