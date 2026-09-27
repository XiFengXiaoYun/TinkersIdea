package com.xifeng.tinkersidea;

import com.xifeng.tinkersidea.Weapons.WeaponRegister;
import com.xifeng.tinkersidea.client.render.RenderLance;
import com.xifeng.tinkersidea.entity.EntityLance;
import com.xifeng.tinkersidea.items.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class Registry {
    //register tools here
    @SubscribeEvent
    public static void registerTools(RegistryEvent.Register<Item> event) {
        WeaponRegister.registerWeapon(event);
    }

    public static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolForgeCrafting(core);
        TinkersIdea.proxy.registerToolModel(core);
    }

    /*public static void initTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolCrafting(core);
        TinkersIdea.proxy.registerToolModel(core);
    }
     */

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        ItemRegistry.initItems(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        TinkersIdea.proxy.registerItemModel(ItemRegistry.magicPlate);
        RenderingRegistry.registerEntityRenderingHandler(EntityLance.class, RenderLance::new);
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> event) {
        EntityRegistry.registerModEntity(new ResourceLocation(Tags.MOD_ID, "thrusting_lance"), EntityLance.class, "lance", 1145, TinkersIdea.INSTANCE, 64, 1, false);
    }

}
