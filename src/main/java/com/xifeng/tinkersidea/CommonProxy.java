package com.xifeng.tinkersidea;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;

//Let's do this stupid proxy
public class CommonProxy {

    public void initToolGuis() {
    }

    public void registerToolModel(ToolCore tc) {
    }

    public <T extends Item & IToolPart> void registerToolPartModel(T part) {
    }

    public void registerSubscriptions() {
    }

    public void renderScreenFullColor(int color, boolean bool) {
    }

    public void registerModifierModel(IModifier mod, ResourceLocation rl) {
    }

}
