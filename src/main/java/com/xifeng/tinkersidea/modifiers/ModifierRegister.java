package com.xifeng.tinkersidea.modifiers;

import com.xifeng.tinkersidea.modifiers.modifier.ModifierMagicEnhance;
import com.xifeng.tinkersidea.modifiers.modifier.ModifierSweepEdge;
import electroblob.wizardry.registry.WizardryItems;
import net.minecraft.init.Items;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

import java.util.HashSet;
import java.util.Set;

public class ModifierRegister {
    public static Set<ModifierTrait> modifierTraits = new HashSet<>();

    public static ModifierSweepEdge sweepEdge = new ModifierSweepEdge();
    public static ModifierMagicEnhance magicEnhance = new ModifierMagicEnhance();

    public static void initModifiers() {
        sweepEdge.addItem(Items.IRON_SWORD);
        TinkerRegistry.addTrait(sweepEdge);
        modifierTraits.add(sweepEdge);
    }

    public static void initWizardryModifiers() {
        magicEnhance.addItem(WizardryItems.magic_crystal);
        TinkerRegistry.addTrait(magicEnhance);
        modifierTraits.add(magicEnhance);
    }
}
