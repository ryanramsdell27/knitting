package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.io.Blender;
import org.javacc.generated.Knit;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern = new KnittingPattern(64);
        pattern.knit(128);
        for(int i = 0; i < 4; i++) {
            pattern.k2tog();
            pattern.knit(14);
        }
        pattern.knit(120);
        for(int i = 0; i < 6; i++) {
            pattern.k2tog();
            pattern.knit(8);
        }
        pattern.knit(108);
        for(int i = 0; i < 6; i++) {
            pattern.k2tog();
            pattern.knit(7);
        }
        pattern.knit(48);
        for(int i = 0; i < 8; i++) {
            pattern.k2tog();
            pattern.knit(4);
        }
        pattern.knit(40);
        for(int i = 0; i < 10; i++) {
            pattern.k2tog();
            pattern.knit(2);
        }
        pattern.knit(30);
        System.out.println(Blender.toBlenderApi(pattern));

        Reader reader = new StringReader("co5k5p3 knit2 p2k2pkpk");
        Knit knit = new Knit(reader);
        try{
            knit.Start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}