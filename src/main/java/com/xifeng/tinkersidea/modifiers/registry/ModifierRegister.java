package com.xifeng.tinkersidea.modifiers.registry;

import c4.conarm.lib.utils.RecipeMatchHolder;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.items.ItemRegistry;
import com.xifeng.tinkersidea.modifiers.modifier.conarm.ModMagicShield;
import com.xifeng.tinkersidea.modifiers.modifier.conarm.ModifierLuck;
import com.xifeng.tinkersidea.modifiers.modifier.tcon.ModifierSweepEdge;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ModifierRegister {

    private static final ModifierSweepEdge sweepEdge = new ModifierSweepEdge();
    private static final ModifierLuck modifierLuck = new ModifierLuck();
    private static final ModMagicShield modMagicShield = new ModMagicShield();

    public static void initModifiers() {
        if(!ModConfig.General.enableGreatSword) return;
        sweepEdge.addItem(Item.getByNameOrId(ModConfig.Modifiers.itemSweepEdge));
        TinkerRegistry.addTrait(sweepEdge);
    }

    public static void initArmorMod() {
        RecipeMatchHolder.addItem(modifierLuck, Blocks.LAPIS_BLOCK, 1);
        RecipeMatchHolder.addItem(modMagicShield, ItemRegistry.magicPlate);
    }

}
