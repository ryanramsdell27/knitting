package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.interpreter.TreeInterpreter;
import dev.ryanramsdell.io.NumpyWriter;
import dev.ryanramsdell.jjtree.Knit;
import dev.ryanramsdell.jjtree.SimpleNode;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern;
        Reader reader = new StringReader("co4 (k3 m1)10 (k2tog k3 k2tog)6");
        Knit knit = new Knit(reader);

        try{
            SimpleNode node = knit.Start();
            node.dump("");
            TreeInterpreter interpreter = new TreeInterpreter(node);
            pattern = interpreter.interpret();

            NumpyWriter numpyWriter = new NumpyWriter(pattern);
            numpyWriter.writeToFile("out.py");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}