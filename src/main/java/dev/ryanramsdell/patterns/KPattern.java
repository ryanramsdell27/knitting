package dev.ryanramsdell.patterns;

import dev.ryanramsdell.data.Stitch;
import dev.ryanramsdell.enums.DecreaseType;
import dev.ryanramsdell.enums.IncreaseType;
import dev.ryanramsdell.enums.StitchType;

/** Knitting pattern API used to represent a knitted object during the
 * execution of a knitting pattern
 */
public interface KPattern {

    /**
     * Method for initial cast on of a project
     * @param numStitches Number of stitches to initially cast on
     * @param join Whether to join in the round. This is currently unsupported by the parser
     */
    void init(int numStitches, boolean join);
    /**
     * Performs a stitch operation of StitchType type onto the current knitting pattern
     * ex. knit, purl, cast on
     * @param type StitchType
     * @return the stitch object performed by the pattern
     */
    Stitch basicStitch(StitchType type);
    /**
     * Perform a decrease that combines num stitches together
     * @param type StitchType of the new stitch
     * @param num number of parent stitches to combine
     */
    void decrease(StitchType type, int num, DecreaseType decreaseType);
    /**
     * Perform an increase of num stitches
     * @param type StitchType of the new stitch
     * @param num number of new stitches to create
     */
    void increase(StitchType type, int num, IncreaseType increaseType);

}
