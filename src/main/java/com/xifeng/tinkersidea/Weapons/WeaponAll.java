package com.xifeng.tinkersidea.Weapons;

import com.xifeng.tinkersidea.Registry;
import com.xifeng.tinkersidea.Weapons.common.GreatSword;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.Set;

public class WeaponAll {
    public static GreatSword greatSword;

    public static void initWeapon(RegistryEvent.Register<Item> event, Set<ToolCore> tools) {
        greatSword = new GreatSword();
        Registry.initForgeTool(greatSword, event);
        tools.add(greatSword);
    }
}
