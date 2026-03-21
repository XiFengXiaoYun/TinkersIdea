package com.xifeng.tinkersidea.modifiers.registry;

import com.xifeng.tinkersidea.modifiers.modifier.ModifierSweepEdge;
import net.minecraft.init.Items;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

import java.util.HashSet;
import java.util.Set;

public class ModifierRegister {
    public static Set<ModifierTrait> modifierTraits = new HashSet<>();

    private static final ModifierSweepEdge sweepEdge = new ModifierSweepEdge();

    public static void initModifiers() {
        sweepEdge.addItem(Items.IRON_SWORD);
        TinkerRegistry.addTrait(sweepEdge);
        modifierTraits.add(sweepEdge);
    }

}
