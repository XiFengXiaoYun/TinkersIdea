package com.xifeng.tinkersidea.event;

import net.minecraftforge.common.MinecraftForge;

public final class ModEventHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(WizardryEvent.class);
    }
}
