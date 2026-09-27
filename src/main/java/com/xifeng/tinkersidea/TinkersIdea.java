package com.xifeng.tinkersidea;

import com.xifeng.tinkersidea.common.CommonProxy;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.event.EventHandler;
import com.xifeng.tinkersidea.leveling.LevelUpEventHandler;
import com.xifeng.tinkersidea.modifiers.registry.ModifierRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:tconstruct;required-after:conarm;"
)
public class TinkersIdea {
    @Mod.Instance
    public static TinkersIdea INSTANCE;

    @SidedProxy(serverSide = "com.xifeng.tinkersidea.common.CommonProxy", clientSide = "com.xifeng.tinkersidea.client.ClientProxy")
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Pre init start");
        ModifierRegister.initModifiers();
        if(ModConfig.General.enableTweaks) {
            MinecraftForge.EVENT_BUS.register(EventHandler.class);
            proxy.registerAnimation();
        }
        if(ModConfig.General.enableLeveling) {
            logger.info("Init leveling event handler");
            MinecraftForge.EVENT_BUS.register(LevelUpEventHandler.class);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Init start");
        proxy.initToolGuis();
        ModifierRegister.initArmorMod();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post init start");
        proxy.initToolGuis();
        proxy.postInit();
        if(ModConfig.General.enableLeveling) {
            logger.info("Init leveling config");
            LevelUpEventHandler.initConfig();
        }
    }
}
