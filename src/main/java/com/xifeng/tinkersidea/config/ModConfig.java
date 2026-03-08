package com.xifeng.tinkersidea.config;

import com.xifeng.tinkersidea.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID)
public final class ModConfig {
    @Config.Comment("test")
    public static boolean enableWizardryCompact = true;
    @Config.Comment("test1")
    public static double manaCapacityIncrease = 0.25;
    @Config.Comment("test2")
    public static double spellPotencyIncrease = 0.25;
}
