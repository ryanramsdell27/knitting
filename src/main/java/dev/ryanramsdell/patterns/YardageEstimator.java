package dev.ryanramsdell.patterns;

import dev.ryanramsdell.data.Stitch;
import dev.ryanramsdell.enums.DecreaseType;
import dev.ryanramsdell.enums.IncreaseType;
import dev.ryanramsdell.enums.StitchType;

public class YardageEstimator implements KPattern{

    private int count;
    private double yardage;
    private double yardsPerKnit;
    @Override
    public void init(int numStitches, boolean join) {
        basicStitch(StitchType.CAST_ON, numStitches);
    }

    @Override
    public Stitch basicStitch(StitchType type) {
        double weight = 1.0;
        switch(type) {
            case KNIT:
            case PURL:
            case CAST_ON:
            default:
                weight = 1.0;
        }
        this.count += 1;
        this.yardage += weight * yardsPerKnit;
        return null;
    }

    private void basicStitch(StitchType type, int numStitches) {
        for(int i = 0; i < numStitches; i++) {
            basicStitch(StitchType.CAST_ON);
        }
    }

    @Override
    public void decrease(StitchType type, int num, DecreaseType decreaseType) {
        basicStitch(type, 1);
    }

    @Override
    public void increase(StitchType type, int num, IncreaseType increaseType) {
        basicStitch(type, num);
    }

    public int getCount() {
        return this.count;
    }

    public double getYardage() {
        return this.yardage;
    }

    public void setYardsPerKnit(double length) {
        this.yardsPerKnit = length;
    }
}
