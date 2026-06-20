package com.xifeng.tinkersidea.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {
    public static ItemMagicPlate magicPlate;
    public static void initItems(RegistryEvent.Register<Item> event) {
        magicPlate = new ItemMagicPlate();
        event.getRegistry().register(magicPlate);
    }
}
