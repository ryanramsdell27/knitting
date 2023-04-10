package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.io.Blender;
import dev.ryanramsdell.io.Deserializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        KnittingPattern pattern = new KnittingPattern(42);
        pattern.knit(160);
        pattern.purl(4);
        System.out.println(Blender.toBlenderApi(pattern));
//        System.out.println(pattern);
//        KnittingPattern kp = Deserializer.deserialize("co 4\nknit 5\nk2\np3");
//        System.out.println(kp);
    }
}