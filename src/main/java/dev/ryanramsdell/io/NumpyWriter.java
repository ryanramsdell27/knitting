package dev.ryanramsdell.io;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.data.MeshData;
import dev.ryanramsdell.data.Point;

import java.io.*;
import java.util.Arrays;

public class NumpyWriter {
    KnittingPattern pattern;
    public NumpyWriter(KnittingPattern pattern) {
        this.pattern = pattern;
    }
    public void writeToFile(String filename) {
        double[][] diss = pattern.computeDissimilarity();
        StringBuilder sb = new StringBuilder("import numpy as np\n");
        sb.append("stitches = np.array(");
        sb.append(Arrays.deepToString(diss).replace("], ", "],\n"));

        MeshData md = Blender.computeVerticesAndEdges(pattern);
        sb.append(")\ninitial = np.array([");
        for(Point vert : md.getVertices()) {
            sb.append(vert.toStringSquare());
            sb.append(',');
        }
        sb.append("])");

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("mds/" + filename), "utf-8"))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
