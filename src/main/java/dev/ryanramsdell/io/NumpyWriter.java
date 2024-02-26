package dev.ryanramsdell.io;

import dev.ryanramsdell.patterns.KnittingPattern;
import dev.ryanramsdell.data.MeshData;
import dev.ryanramsdell.data.Point;
import dev.ryanramsdell.enums.DissimilarityAlgorithm;

import java.io.*;
import java.util.Arrays;

public class NumpyWriter {
    KnittingPattern pattern;
    DissimilarityAlgorithm dissimilarityAlgorithm = DissimilarityAlgorithm.DEFAULT;
    public NumpyWriter(KnittingPattern pattern) {
        this.pattern = pattern;
    }
    public NumpyWriter(KnittingPattern pattern, DissimilarityAlgorithm dissimilarityAlgorithm) {
        this.pattern = pattern;
        this.dissimilarityAlgorithm = dissimilarityAlgorithm;
    }
    public void writeToFile(String filename) {
        double[][] diss = pattern.computeDissimilarity(this.dissimilarityAlgorithm);
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

    public void setDissimilarityAlgorithm(DissimilarityAlgorithm dissimilarityAlgorithm) {
        this.dissimilarityAlgorithm = dissimilarityAlgorithm;
    }
}
