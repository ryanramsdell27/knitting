package dev.ryanramsdell;

import dev.ryanramsdell.data.KnittingPattern;
import dev.ryanramsdell.io.Blender;
import org.javacc.generated.Knit;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        KnittingPattern pattern;
        Reader reader = new StringReader("co64k256p512 knit2 p2k2pkpkk2tog");
        Knit knit = new Knit(reader);
        try{
            pattern = knit.Start();
            System.out.println(Blender.toBlenderApi(pattern));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}