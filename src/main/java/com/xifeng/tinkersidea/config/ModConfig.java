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

    @Config.Name("Leveling")
    @Config.Comment("Settings about tool leveling system")
    public static Leveling leveling;

    public static class General {
        @Config.Comment("Set false to disable great sword")
        public static boolean enableGreatSword = true;

        @Config.Comment("Set false to disable thrusting lance")
        public static boolean enableThrustingLance = true;

        @Config.Comment("Set false to disable tweaks about battlesign")
        public static boolean enableTweaks = true;

        @Config.Comment("Enable a custom tool leveling system, which is compatible with other tinker tool leveling mods")
        public static boolean enableLeveling = true;
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

    public static class Leveling {
        @Config.Comment("Max level of all the tools and armors")
        @Config.RangeInt(min = 1)
        public static int maxLevel = 20;

        @Config.Comment("Base max tool exp")
        @Config.RangeInt(min = 1)
        public static int baseMaxExp = 100;

        @Config.Comment("Base max armor exp")
        @Config.RangeInt(min = 1)
        public static int baseMaxExpArmor = 100;

        @Config.Comment("The ratio of mob max health convert to tool exp when it dies")
        @Config.RangeDouble(min = 0.0, max = 10.0)
        public static double ratio = 0.2;

        @Config.Comment("Damage of player get convert to armor exp")
        @Config.RangeDouble(min = 0.0, max = 10.0)
        public static double damageToExp = 1.0;

        @Config.Comment("Max exp get by kill a mob")
        @Config.RangeInt(min = 1)
        public static int maxExpGet = 200;

        @Config.Comment("Max exp get by break a block")
        @Config.RangeInt(min = 1)
        public static int maxExpBlock = 50;

        @Config.Comment("Max exp gained by get hurt")
        @Config.RangeInt(min = 1)
        public static int maxExpArmor = 50;

        @Config.Comment("The exp growth mode required for each level \n" +
                        "Three modes available, ADD, MULTI and CONST \n" +
                        "Let a_n equals to max exp needed for level n, a_(n+1) equals to max exp needed for level n + 1 \n" +
                        "ADD: a_(n+1) = a_n + addAmount \n" +
                        "MULTI: a_(n+1) = a_n * multiplier \n" +
                        "CONST: a_(n+1) = a_n "
        )
        public static String mode = "ADD";

        @Config.Comment("The addAmount of ADD mode")
        public static int addAmount = 100;

        @Config.Comment("The multiplier of MULTI mode")
        @Config.RangeDouble(min = 1.0, max = 10.0)
        public static double multiplier = 1.25;

        @Config.Comment("Tool groups, format is tool1,tool2,tool3...")
        public static String[] toolGroups = new String[] {
                "tconstruct:broadsword,tconstruct:rapier,tconstruct:cleaver,tconstruct:longsword,tconstruct:scythe,tinkersidea:greatsword,tinkersidea:thrusting_lance",
                "tconstruct:shovel,tconstruct:hatchet,tconstruct:mattock,tconstruct:kama,tconstruct:hammer,tconstruct:lumberaxe",
                "tconstruct:shortbow,tconstruct:longbow,tconstruct:crossbow",
                "tconstruct:arrow,tconstruct:bolt",
                "conarm:helmet,conarm:chestplate,conarm:leggings,conarm:boots"
        };

        @Config.Comment("Leveling rule, format is stats:type:threshold:amount,stats1:type:threshold:amount... type = 1, int stats, type = 2, float stats")
        public static String[] rules = new String[] {
                "Durability:1:1:50,Attack:2:4:1.5,FreeModifiers:1:4:1",
                "Durability:1:1:50,Attack:2:4:1.5,FreeModifiers:1:4:1",
                "Durability:1:1:50,Attack:2:4:1.5,FreeModifiers:1:4:1",
                "Durability:1:1:50,Attack:2:4:1.5,FreeModifiers:1:4:1",
                "Durability:1:1:20,Defense:2:2:0.25,Toughness:2:4:0.25,FreeModifiers:1:4:1"
        };

        @Config.Comment("Set true to use json to define leveling rule, and the config toolGroups and rules will be ignored")
        public static boolean loadFromJson = false;

    }
}
