package com.xifeng.tinkersidea;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.event.WizardryEvent;
import com.xifeng.tinkersidea.materials.MagicMaterials;
import com.xifeng.tinkersidea.modifiers.ModifierRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:tconstruct;after:conarm;after:ebwizardry;after:crafttweaker"
)
public class TinkersIdea {
    public static boolean enableWizardry() {
        return ModConfig.enableWizardryCompact && Loader.isModLoaded("ebwizardry");
    }

    @SidedProxy(serverSide = "com.xifeng.tinkersidea.CommonProxy", clientSide = "com.xifeng.tinkersidea.client.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModifierRegister.initModifiers();
        if(enableWizardry()) {
            MinecraftForge.EVENT_BUS.register(WizardryEvent.class);
            MagicMaterials.initMagicMaterials();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initToolGuis();
        if(enableWizardry()) {
            ModifierRegister.initWizardryModifiers();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.initToolGuis();
        proxy.postInit();
    }
}
