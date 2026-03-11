package com.xifeng.tinkersidea.modifiers;

import com.xifeng.tinkersidea.modifiers.modifier.ModifierMagic;
import com.xifeng.tinkersidea.modifiers.modifier.ModifierSweepEdge;
import net.minecraft.init.Items;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ModifierRegister {

    public static ModifierSweepEdge sweepEdge = new ModifierSweepEdge();

    public static void initModifiers() {
        sweepEdge.addItem(Items.IRON_SWORD);
        TinkerRegistry.addTrait(sweepEdge);
    }
}
