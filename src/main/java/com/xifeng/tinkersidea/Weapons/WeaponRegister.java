package com.xifeng.tinkersidea.Weapons;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class WeaponRegister {
    public static void registerWeapon(RegistryEvent.Register<Item> event) {
        WeaponAll.initWeapon(event);
    }
}
