package com.xifeng.tinkersidea.modifiers.modifier;

import com.xifeng.tinkersidea.Weapons.wizardry.SpecialCategory;
import com.xifeng.tinkersidea.config.ModConfig;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModifierMagic extends ToolModifier {
    public static ModifierMagic INSTANCE = new ModifierMagic();

    public ModifierMagic() {
        this("magic_data", 0x114514);
    }

    public ModifierMagic(String identifier, int color) {
        super(identifier, color);
        this.addAspects(SpecialCategory.aspect, new ModifierAspect.LevelAspect(this, 4), new ModifierAspect.DataAspect(this));
    }

    @Override
    public void applyEffect(NBTTagCompound root, NBTTagCompound modifierTag) {
        syncWizardryData(root, modifierTag);
    }

    private static int getWandLevel(NBTTagCompound modifierTag) {
        return modifierTag.getInteger("level");
    }

    private static void syncWizardryData(NBTTagCompound root, NBTTagCompound modifierTag) {
        NBTTagCompound stats = root.getCompoundTag("Stats");
        NBTTagCompound oldStats = root.getCompoundTag("StatsOriginal");
        int oldMaxMana = oldStats.getInteger("maxMana");
        float oldPotency = oldStats.getFloat("spellPotency");
        int level = getWandLevel(modifierTag) - 1;
        int maxMana = (int) ((level * ModConfig.manaCapacityIncrease + 1.0) * oldMaxMana);
        float spellPotency = (float) ((level * ModConfig.spellPotencyIncrease + 1.0) * oldPotency);
        stats.setInteger("maxMana", maxMana);
        stats.setFloat("spellPotency", spellPotency);
    }
}
