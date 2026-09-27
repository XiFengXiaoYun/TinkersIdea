package com.xifeng.tinkersidea.leveling;

public class StatsModifier {
    public String stats;
    public int threshold;
    public final double base;
    public int type;

    public StatsModifier(String stats, double base, int threshold) {
        this.stats = stats;
        this.threshold = threshold;
        this.base = base;
    }

}
