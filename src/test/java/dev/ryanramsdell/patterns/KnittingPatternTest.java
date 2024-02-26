package dev.ryanramsdell.patterns;

import dev.ryanramsdell.enums.DissimilarityAlgorithm;
import dev.ryanramsdell.interpreter.TreeInterpreter;
import dev.ryanramsdell.jjtree.Knit;
import dev.ryanramsdell.jjtree.ParseException;
import dev.ryanramsdell.jjtree.SimpleNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Disabled
public class KnittingPatternTest {

    @Test
    void shortestPathAlgos() throws ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DissimilarityAlgorithm[] algos = {DissimilarityAlgorithm.DIJKSTRAS, DissimilarityAlgorithm.FLOYD_WARSHALL, DissimilarityAlgorithm.IMMEDIATE_NEIGHBOR};
        for(int x = 10; x < 100; x+=5) {
            Map<DissimilarityAlgorithm, Long> runtimes = new HashMap<>();
            String patternString = String.format("co%d (k%d)%d (k2tog k5)%d", x, x, x / 2, x - 2);
            KnittingPattern pattern;
            Reader reader = new StringReader(patternString);
            Knit knit = new Knit(reader);

            SimpleNode node = knit.Start();

            TreeInterpreter<YardageEstimator> interpreter2 = new TreeInterpreter<>(node, YardageEstimator.class);
            YardageEstimator pattern2 = interpreter2.getPattern();
            pattern2.setYardsPerKnit(0.0277778);
            interpreter2.interpret();
            int count = pattern2.getCount();

            for (DissimilarityAlgorithm algo : algos) {
                TreeInterpreter<KnittingPattern> interpreter = new TreeInterpreter<>(node, KnittingPattern.class);
                pattern = interpreter.interpret();

                long startTime = System.nanoTime();
                double[][] diss = pattern.computeDissimilarity(algo);
                long duration = System.nanoTime() - startTime;
                runtimes.put(algo, duration);
            }

            System.out.printf("N = %d x = %d%n", count, x);
            for (DissimilarityAlgorithm algo : runtimes.keySet()) {
                System.out.printf("%-20s %fms%n", algo, runtimes.get(algo) / 1000000.0);
            }
        }
    }

}
