package com.xifeng.tinkersidea.config;

import com.xifeng.tinkersidea.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID)
public class ModConfig {
    @Config.Name("General")
    @Config.Comment({"General settings"})
    public static General general;

    @Config.Name("Modifiers")
    @Config.Comment({"Modifier settings"})
    public static Modifiers modifiers;

    @Config.Name("Tweaks")
    @Config.Comment("Tweaks about battlesign")
    public static Tweaks tweaks;

    public static class General {
        @Config.Comment("Set false to disable great sword")
        public static boolean enableGreatSword = true;

        @Config.Comment("Set false to disable tweaks about battlesign")
        public static boolean enableTweaks = true;
    }

    public static class Tweaks {
        @Config.Comment("The maximum cooldown of battlesign in ticks")
        public static int maxCooldown = 100;

        @Config.Comment("The chance of shield-break when attacker is able to break shield")
        public static double chance = 0.5;

        @Config.Comment("The list of entities that can always break shield, no matter what they're holding")
        public static String[] strongEntities = new String[]{
                "minecraft:iron_golem"
        };
    }

    public static class Modifiers {
        @Config.Comment("Item for sweep edge modifier")
        public static String itemSweepEdge = "minecraft:iron_sword";

        @Config.Comment("The luck attribute bonus of the luck_armor modifier")
        public static double luckAmount = 1.0;

        @Config.Comment("Damage reduction per level of the magic shield modifier")
        public static double magicDamageReduction = 0.05;
    }
}
