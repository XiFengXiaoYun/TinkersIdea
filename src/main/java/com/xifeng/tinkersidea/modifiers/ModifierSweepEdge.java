package com.xifeng.tinkersidea.modifiers;

import net.minecraft.init.Enchantments;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.ToolBuilder;

public class ModifierSweepEdge extends ModifierTrait {
    public ModifierSweepEdge() {
        super("sweep_edge", 0xffca18, 3, 1);
        this.addAspects(ModifierAspect.weaponOnly);
    }

    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        int level = modifierTag.getInteger("level");
        if (Enchantments.SWEEPING != null) {
            while (level > ToolBuilder.getEnchantmentLevel(rootCompound, Enchantments.SWEEPING)) {
                ToolBuilder.addEnchantment(rootCompound, Enchantments.SWEEPING);
            }
        }
    }
}
