package com.xifeng.tinkersidea.Weapons.wizardry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;

public class MagicNBT extends ToolNBT {
    public float spellPotency;
    public int mana;
    public int maxMana;
    public MagicNBT() {
        this.spellPotency = 1.0f;
        this.mana = 0;
        this.maxMana = 100;
    }

    public MagicNBT(NBTTagCompound nbt) {
        super(nbt);
    }

    public MagicNBT magic(MagicMaterialStats stats) {
        float spell = 0.0f;
        int mana = 0;
        int maxMana = 100;
        if(stats != null) {
            spell = stats.spellPotency;
            mana = stats.mana;
            maxMana = stats.maxMana;
        }
        this.spellPotency = spell;
        this.mana = mana;
        this.maxMana = maxMana;
        return this;
    }

    public void read(NBTTagCompound nbt) {
        super.read(nbt);
        this.spellPotency = nbt.getFloat("spellPotency");
        this.mana = nbt.getInteger("mana");
        this.maxMana = nbt.getInteger("maxMana");
    }

    public void write(NBTTagCompound nbt) {
        super.write(nbt);
        nbt.setFloat("spellPotency", this.spellPotency);
        nbt.setInteger("mana", this.mana);
        nbt.setInteger("maxMana", this.maxMana);
    }

    public static MagicNBT from(ItemStack itemStack) {
        return new MagicNBT(TagUtil.getToolTag(itemStack));
    }
}
