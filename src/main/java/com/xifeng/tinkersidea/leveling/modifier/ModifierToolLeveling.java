package com.xifeng.tinkersidea.leveling.modifier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

//copy from tinker's tool leveling
public class ModifierToolLeveling extends ModifierTrait {
    public static final String ID = "leveling_tool";
    public static ModifierToolLeveling instance = new ModifierToolLeveling();
    public ModifierToolLeveling() {
        super("leveling_tool", 0x114514);
        this.aspects.clear();
        this.addAspects(new ModifierAspect.DataAspect(this));
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return true;
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        LevelNBT nbt = LevelNBT.get(modifierTag);
        NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
        NBTTagCompound statsBonus = nbt.statsBonus;
        for(String key : statsBonus.getKeySet()) {
            NBTBase base = statsBonus.getTag(key);
            modifyTag(toolTag, base, key);
        }
    }

    private static void modifyTag(NBTTagCompound toolTag, NBTBase base, String key) {
        if(!toolTag.hasKey(key)) return;
        if(base instanceof NBTPrimitive) {
            NBTPrimitive primitive = (NBTPrimitive) base;
            int type = primitive.getId();
            if(type == 3) {
                int bonus = primitive.getInt();
                int amount = bonus + toolTag.getInteger(key);
                toolTag.setInteger(key, amount);
            } else if(type == 5) {
                float bonus = primitive.getFloat();
                float amount = bonus + toolTag.getFloat(key);
                toolTag.setFloat(key, amount);
            }
        }
    }
}
