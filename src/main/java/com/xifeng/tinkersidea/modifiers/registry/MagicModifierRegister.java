package com.xifeng.tinkersidea.modifiers.registry;

import com.xifeng.tinkersidea.modifiers.modifier.ModifierMagicEnhance;
import electroblob.wizardry.registry.WizardryItems;
import slimeknights.tconstruct.library.TinkerRegistry;

public class MagicModifierRegister extends ModifierRegister {
    private static final ModifierMagicEnhance magicEnhance =  new ModifierMagicEnhance();

    public static void initModifiers() {
        magicEnhance.addItem(WizardryItems.magic_crystal);
        TinkerRegistry.addTrait(magicEnhance);
        modifierTraits.add(magicEnhance);
    }
}
