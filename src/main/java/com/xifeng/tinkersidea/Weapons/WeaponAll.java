package com.xifeng.tinkersidea.Weapons;

import com.xifeng.tinkersidea.Registry;
import com.xifeng.tinkersidea.TinkersIdea;
import com.xifeng.tinkersidea.Weapons.common.GreatSword;
import com.xifeng.tinkersidea.Weapons.common.ThrustingLance;
import com.xifeng.tinkersidea.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;

public class WeaponAll {
    public static GreatSword greatSword;
    public static ThrustingLance lance;
    public static ToolPart longBlade;

    public static void initWeapon(RegistryEvent.Register<Item> event) {
        if(!ModConfig.General.enableGreatSword) return;

        longBlade = new ToolPart(576);
        longBlade.setRegistryName("long_blade").setTranslationKey("long_blade");
        event.getRegistry().register(longBlade);
        TinkerRegistry.registerToolPart(longBlade);
        TinkersIdea.proxy.registerToolPartModel(longBlade);
        TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), longBlade));

        greatSword = new GreatSword();
        Registry.initForgeTool(greatSword, event);
        lance = new ThrustingLance();
        Registry.initForgeTool(lance, event);

    }
}
