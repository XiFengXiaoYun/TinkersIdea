package com.xifeng.tinkersidea.items;

import net.minecraft.item.Item;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ItemMagicPlate extends Item {
    public ItemMagicPlate() {
        setRegistryName("magic_plate");
        setTranslationKey("magic_plate");
        setCreativeTab(TinkerRegistry.tabGeneral);
    }
}
