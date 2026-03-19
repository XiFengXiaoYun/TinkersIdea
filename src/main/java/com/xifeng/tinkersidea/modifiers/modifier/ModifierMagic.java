package com.xifeng.tinkersidea.modifiers.modifier;

import com.xifeng.tinkersidea.Weapons.wizardry.MagicNBT;
import com.xifeng.tinkersidea.Weapons.wizardry.SpecialCategory;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.util.TITagUtil;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModifierMagic extends ToolModifier {
    public static ModifierMagic INSTANCE = new ModifierMagic();

    public ModifierMagic() {
        this("magic_data", 0xcb3ff7);
    }

    public ModifierMagic(String identifier, int color) {
        super(identifier, color);
        this.addAspects(SpecialCategory.aspect, new ModifierAspect.LevelAspect(this, 4), new ModifierAspect.DataAspect(this));
    }

    @Override
    public void applyEffect(NBTTagCompound root, NBTTagCompound modifierTag) {
        int level = ModifierNBT.readInteger(modifierTag).level - 1;
        if(level > 0){
            MagicNBT nbt = TITagUtil.getMagicStats(root);
            MagicNBT original = TITagUtil.getOriginalMagicStats(root);
            int maxMana = original.maxMana;
            float potency = original.spellPotency;
            nbt.maxMana += (int) (maxMana * (ModConfig.manaCapacityIncrease * level));
            nbt.spellPotency += (float) (potency * ModConfig.spellPotencyIncrease * level);
            TagUtil.setToolTag(root, nbt.get());
        }
    }
}
