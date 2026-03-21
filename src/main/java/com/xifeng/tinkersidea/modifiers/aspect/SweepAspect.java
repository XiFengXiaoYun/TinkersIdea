package com.xifeng.tinkersidea.modifiers.aspect;

import com.xifeng.tinkersidea.Weapons.common.GreatSword;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public final class SweepAspect extends ModifierAspect {
    public static final SweepAspect sweepAspect = new SweepAspect();

    @Override
    public boolean canApply(ItemStack itemStack, ItemStack itemStack1) {
        return itemStack.getItem() instanceof GreatSword;
    }

    @Override
    public void updateNBT(NBTTagCompound nbtTagCompound, NBTTagCompound nbtTagCompound1) {

    }
}
