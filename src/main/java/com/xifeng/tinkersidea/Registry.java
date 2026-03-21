package com.xifeng.tinkersidea;

import com.google.common.collect.ImmutableSet;
import com.xifeng.tinkersidea.Weapons.WeaponRegister;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class Registry {
    private static final Set<ToolCore> tools = new HashSet<>();
    //register tools here
    @SubscribeEvent
    public static void registerTools(RegistryEvent.Register<Item> event) {
        WeaponRegister.registerWeapon(event, tools);
        WeaponRegister.registerModWeapon(event, tools, TinkersIdea.enableWizardry());
    }

    public static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolForgeCrafting(core);
        TinkersIdea.proxy.registerToolModel(core);
    }

    public static void initTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolCrafting(core);
        TinkersIdea.proxy.registerToolModel(core);
    }

    public static Set<ToolCore> getTools() {
        return ImmutableSet.copyOf(tools);
    }
}
