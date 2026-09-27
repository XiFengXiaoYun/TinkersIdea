package com.xifeng.tinkersidea.leveling;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelingRule {
    private final List<String> tools;
    private final List<StatsModifier> statsModifiers;

    public LevelingRule(List<String> tools, List<StatsModifier> statsModifiers) {
        this.tools = tools;
        this.statsModifiers = statsModifiers;
    }

    public static void loadFromJson(Path jsonDir, List<LevelingRule> ruleLists) {
        //TODO
    }

    public boolean has(String tool) {
        return tools.contains(tool);
    }

    public List<StatsModifier> getStatsModifiers() {
        return Collections.unmodifiableList(statsModifiers);
    }

    public List<String> getTools() {
        return Collections.unmodifiableList(tools);
    }

    public List<StatsModifier> matchModifier(int level) {
        List<StatsModifier> list = new ArrayList<>();
        for(StatsModifier mod : statsModifiers) {
            if(level % mod.threshold == 0) {
                list.add(mod);
            }
        }
        return list;
    }
}
