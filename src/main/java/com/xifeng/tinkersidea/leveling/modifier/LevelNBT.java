package com.xifeng.tinkersidea.leveling.modifier;

import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.leveling.LevelMode;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class LevelNBT extends ModifierNBT {
    public int exp = 0;
    public int maxExp = 0;
    public NBTTagCompound statsBonus;

    public LevelNBT(NBTTagCompound tag) {
        super(tag);
        this.read(tag);
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        exp = tag.getInteger("exp");
        maxExp = tag.getInteger("maxExp");
        statsBonus = tag.getCompoundTag("statsBonus");
    }

    @Override
    public void write(NBTTagCompound tag) {
        super.write(tag);
        tag.setInteger("exp", exp);
        tag.setInteger("maxExp", maxExp);
        tag.setTag("statsBonus", statsBonus);
    }

    public int getLevel() {
        return level - 1;
    }

    private void setIntStats(String key, int amount) {
        statsBonus.setInteger(key, amount);
    }

    public void addIntStats(String key, int amount) {
        int old = getIntStats(key);
        setIntStats(key, old + amount);
    }

    private void setFloatStats(String key, float value) {
        statsBonus.setFloat(key, value);
    }

    public void addFloatStats(String key, float value) {
        float old = getFloatStats(key);
        setFloatStats(key, old + value);
    }

    public int getIntStats(String key) {
        return statsBonus.getInteger(key);
    }

    public float getFloatStats(String key) {
        return statsBonus.getFloat(key);
    }

    public boolean add(int exp, LevelMode mode) {
        boolean isLevelUp = false;
        this.exp += exp;
        while (this.exp >= this.maxExp) {
            if (this.getLevel() >= ModConfig.Leveling.maxLevel) {
                this.exp = this.maxExp;
                return false;
            }
            this.exp -= this.maxExp;
            int old = this.maxExp;
            this.level++;
            this.maxExp = calcMaxExp(old, mode);
            isLevelUp = true;
        }
        return isLevelUp;
    }

    public int calcMaxExp(int oldExp, LevelMode mode) {
        switch (mode) {
            case ADD:
                return oldExp + ModConfig.Leveling.addAmount;
            case MULTI:
                return (int) (oldExp * ModConfig.Leveling.multiplier);
            case CONST:
            default:
                return oldExp;
        }
    }

    public static LevelNBT get(ItemStack stack, boolean isArmor) {
        String id = isArmor ? ModifierArmorLeveling.ID : ModifierToolLeveling.ID;
        return get(TinkerUtil.getModifierTag(stack, id));
    }

    public static LevelNBT get(NBTTagCompound modTag) {
        return new LevelNBT(modTag);
    }
}
