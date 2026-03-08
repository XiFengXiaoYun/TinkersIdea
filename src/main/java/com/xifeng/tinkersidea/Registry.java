package com.xifeng.tinkersidea;

import com.xifeng.tinkersidea.Weapons.WeaponAll;
import com.xifeng.tinkersidea.materials.MagicMaterials;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class Registry {
    //register tools here
    @SubscribeEvent
    public static void registerTools(RegistryEvent.Register<Item> event) {
        MagicMaterials.initMagicMaterials();
        WeaponAll.initWeapon(event);
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
}
