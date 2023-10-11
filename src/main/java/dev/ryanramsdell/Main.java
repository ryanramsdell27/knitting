package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.data.MeshData;
import dev.ryanramsdell.data.Point;
import dev.ryanramsdell.io.Blender;
import org.javacc.generated.Knit;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern;
        Reader reader = new StringReader("co20k6p2 knit2 k2tog p2k2pkpk k2tog k4 k2tog k2 k2tog k6 p3 k2tog k3 k2tog k3 k2tog k3 k2tog k3 k7");//  (k6k2tog)3"); //"co8k6p2 knit2 p2k2pkpk"
        Knit knit = new Knit(reader);
        try{
            pattern = knit.Start();
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
            System.out.println(sb);
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("mds/out.py"), "utf-8"))) {
                writer.write(sb.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}