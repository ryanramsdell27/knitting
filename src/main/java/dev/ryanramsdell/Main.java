package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.data.YardageEstimator;
import dev.ryanramsdell.interpreter.TreeInterpreter;
import dev.ryanramsdell.io.NumpyWriter;
import dev.ryanramsdell.jjtree.Knit;
import dev.ryanramsdell.jjtree.SimpleNode;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern;
        Reader reader = new StringReader("co40 (k3 m1 k10)10 (k2tog k3 k2tog)6");
        Knit knit = new Knit(reader);

        try{
            SimpleNode node = knit.Start();
            node.dump("");

            TreeInterpreter<KnittingPattern> interpreter = new TreeInterpreter<>(node, KnittingPattern.class);
            pattern = interpreter.interpret();

            NumpyWriter numpyWriter = new NumpyWriter(pattern);
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