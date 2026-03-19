package com.xifeng.tinkersidea.modifiers.modifier;

import com.xifeng.tinkersidea.Weapons.wizardry.MagicNBT;
import com.xifeng.tinkersidea.Weapons.wizardry.SpecialCategory;
import com.xifeng.tinkersidea.util.TITagUtil;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

public class ModifierMagicEnhance extends ModifierTrait {

    public ModifierMagicEnhance() {
        super("magic_enhance", 0xe4ea60, 3, 20);
        this.addAspects(SpecialCategory.aspect);
    }


    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT nbt = ModifierNBT.readInteger(modifierTag);
        int current = nbt.current;
        MagicNBT data = TITagUtil.getMagicStats(rootCompound);
        MagicNBT oldData = TITagUtil.getOriginalMagicStats(rootCompound);
        data.spellPotency += (float) (oldData.spellPotency * current * 0.01);
        TagUtil.setToolTag(rootCompound, data.get());
    }

}
