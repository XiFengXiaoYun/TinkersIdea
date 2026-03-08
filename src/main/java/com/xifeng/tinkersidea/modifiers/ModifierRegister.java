package com.xifeng.tinkersidea.modifiers;

import net.minecraft.init.Items;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ModifierRegister {

    public static ModifierSweepEdge sweepEdge = new ModifierSweepEdge();
    public static void initModifiers() {
        sweepEdge.addItem(Items.APPLE);
        TinkerRegistry.addTrait(sweepEdge);
    }
}
