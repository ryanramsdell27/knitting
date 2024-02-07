package dev.ryanramsdell;

import dev.ryanramsdell.enums.DissimilarityAlgorithm;
import dev.ryanramsdell.patterns.KnittingPattern;
import dev.ryanramsdell.patterns.YardageEstimator;
import dev.ryanramsdell.interpreter.TreeInterpreter;
import dev.ryanramsdell.io.NumpyWriter;
import dev.ryanramsdell.jjtree.Knit;
import dev.ryanramsdell.jjtree.SimpleNode;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern;
        String patternString = "co40 (k3 m1:(right) k10)10 (k2tog:(left) k3 k2tog)16";
        int x = 50;
        patternString = String.format("co%d (k%d)%d (k2tog k5)%d", x, x, x/2, x-2);
        Reader reader = new StringReader(patternString);
        Knit knit = new Knit(reader);

        try{
            SimpleNode node = knit.Start();
            node.dump("");

            TreeInterpreter<KnittingPattern> interpreter = new TreeInterpreter<>(node, KnittingPattern.class);
            pattern = interpreter.interpret();

            NumpyWriter numpyWriter = new NumpyWriter(pattern, DissimilarityAlgorithm.DIJKSTRAS);
            numpyWriter.writeToFile("out.py");

            TreeInterpreter<YardageEstimator> interpreter2 = new TreeInterpreter<>(node, YardageEstimator.class);
            YardageEstimator pattern2 = interpreter2.getPattern();
            pattern2.setYardsPerKnit(0.0277778);
            interpreter2.interpret();
            System.out.println("Count: " + pattern2.getCount() + "\nEstimated yardage: " + pattern2.getYardage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}