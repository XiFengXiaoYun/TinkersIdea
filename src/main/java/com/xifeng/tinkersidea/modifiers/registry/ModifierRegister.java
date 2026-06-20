package com.xifeng.tinkersidea.modifiers.registry;

import c4.conarm.lib.utils.RecipeMatchHolder;
import com.xifeng.tinkersidea.items.ItemRegistry;
import com.xifeng.tinkersidea.modifiers.modifier.conarm.ModMagicShield;
import com.xifeng.tinkersidea.modifiers.modifier.conarm.ModifierLuck;
import com.xifeng.tinkersidea.modifiers.modifier.tcon.ModifierSweepEdge;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.Modifier;

import java.util.HashSet;
import java.util.Set;

public class ModifierRegister {
    public static Set<Modifier> modifierTraits = new HashSet<>();
    public static Set<Modifier> armorModifierTraits = new HashSet<>();

    private static final ModifierSweepEdge sweepEdge = new ModifierSweepEdge();
    private static final ModifierLuck modifierLuck = new ModifierLuck();
    private static final ModMagicShield modMagicShield = new ModMagicShield();

    public static void initModifiers() {
        sweepEdge.addItem(Items.IRON_SWORD);
        TinkerRegistry.addTrait(sweepEdge);
        modifierTraits.add(sweepEdge);
    }

    public static void initArmorMod() {
        RecipeMatchHolder.addItem(modifierLuck, Blocks.LAPIS_BLOCK, 1);
        armorModifierTraits.add(modifierLuck);
        RecipeMatchHolder.addItem(modMagicShield, ItemRegistry.magicPlate);
        armorModifierTraits.add(modMagicShield);
    }

}
