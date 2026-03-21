package com.xifeng.tinkersidea.Weapons;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.Set;

public class WeaponRegister {
    public static void registerWeapon(RegistryEvent.Register<Item> event, Set<ToolCore> tools) {
        WeaponAll.initWeapon(event, tools);
    }
}
