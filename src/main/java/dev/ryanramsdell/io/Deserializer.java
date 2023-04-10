package dev.ryanramsdell.io;

import dev.ryanramsdell.data.KnittingPattern;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deserializer {

    static Pattern castOnPattern = Pattern.compile("(?:cast on|co)\\s?[0-9]+");
    static Pattern knitPattern = Pattern.compile("(?:knit|k)\\s?[0-9]+");
    static Pattern purlPattern = Pattern.compile("(?:purl|p)\\s?[0-9]+");

    public static KnittingPattern deserialize(String pattern) {
        Iterator<String> itt = pattern.lines().iterator();
        KnittingPattern knittingPattern = null;
        while(itt.hasNext()) {
            String line = itt.next();
            Matcher knit = knitPattern.matcher(line);
            Matcher purl = purlPattern.matcher(line);
            Matcher castOn = castOnPattern.matcher(line);

            if(castOn.matches()) {
                int t = Integer.parseInt(line.replaceAll("(?:cast on|co)\\s?", ""));
                System.out.println("Cast On " + t);
                if(knittingPattern == null) {
                    knittingPattern = new KnittingPattern(t);
                }
                else {
                    knittingPattern.castOn(t);
                }
            }
            if(knit.matches()) {
                int t = Integer.parseInt(line.replaceAll("(?:knit|k)\\s?", ""));
                System.out.println("Knit " + t);
                assert knittingPattern != null;
                knittingPattern.knit(t);
            }
            else if(purl.matches()) {
                int t = Integer.parseInt(line.replaceAll("(?:purl|p)\\s?", ""));
                System.out.println("Purl " + t);
                assert knittingPattern != null;
                knittingPattern.purl(t);
            }

        }
        return knittingPattern;
    }
}
