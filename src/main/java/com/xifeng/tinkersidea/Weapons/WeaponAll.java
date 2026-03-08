package com.xifeng.tinkersidea.Weapons;

import com.xifeng.tinkersidea.Registry;
import com.xifeng.tinkersidea.Tags;
import com.xifeng.tinkersidea.TinkersIdea;
import com.xifeng.tinkersidea.Weapons.wizardry.SpellBlade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;

public class WeaponAll {
    public static GreatSword greatSword;
    public static SpellBlade spellBlade;

    public static ToolPart magicFocus;

    public static void initWeapon(RegistryEvent.Register<Item> event) {
        greatSword = new GreatSword();
        Registry.initForgeTool(greatSword, event);
        if(TinkersIdea.enableWizardry()) {
            System.out.println("Wizardry Start!");

            magicFocus = new ToolPart(288);
            magicFocus.setTranslationKey("magic_focus").setRegistryName(Tags.MOD_ID, "magic_focus");
            event.getRegistry().register(magicFocus);

            TinkerRegistry.registerToolPart(magicFocus);
            TinkersIdea.proxy.registerToolPartModel(magicFocus);
            TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), magicFocus));

            spellBlade = new SpellBlade();
            Registry.initTool(spellBlade, event);
        }
    }
}
