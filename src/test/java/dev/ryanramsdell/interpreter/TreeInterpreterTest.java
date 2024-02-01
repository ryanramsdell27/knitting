package dev.ryanramsdell.interpreter;

import dev.ryanramsdell.patterns.YardageEstimator;
import dev.ryanramsdell.jjtree.Knit;
import dev.ryanramsdell.jjtree.ParseException;
import dev.ryanramsdell.jjtree.SimpleNode;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeInterpreterTest {
    @Test
    void countStitchesLinear() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reader reader = new StringReader("co40 k10 p5");
        YardageEstimator pattern = getYardageEstimator(reader);
        assertEquals(pattern.getCount(), 55);
    }

    @Test
    void countStitchesLinearDecreaseAndIncreases() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reader reader = new StringReader("co40 k10 p5 m1 m1:(left) m1:(right) k2tog k2tog:(left) k2tog:(right)");
        YardageEstimator pattern = getYardageEstimator(reader);
        assertEquals(pattern.getCount(), 61);
    }
    @Test
    void countStitchesSingleRepeats() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reader reader = new StringReader("co10 (k2 m2 k1)10 (k2tog k3 k2tog)10");
        YardageEstimator pattern = getYardageEstimator(reader);
        assertEquals(pattern.getCount(), 110);
    }
    @Test
    void countStitchesNestedRepeats() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reader reader = new StringReader("co10 (k3 m2 k1 (k2tog k3 m1)10 )10");
        YardageEstimator pattern = getYardageEstimator(reader);
        assertEquals(pattern.getCount(), 570);
    }

    private static YardageEstimator getYardageEstimator(Reader reader) throws ParseException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Knit knit = new Knit(reader);

        SimpleNode node = knit.Start();
        node.dump("");

        TreeInterpreter<YardageEstimator> interpreter = new TreeInterpreter<>(node, YardageEstimator.class);
        return interpreter.interpret();
    }

}
