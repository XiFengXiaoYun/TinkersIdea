package com.xifeng.tinkersidea.modifiers.modifier;

import com.xifeng.tinkersidea.Weapons.wizardry.SpecialCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class ModifierMagicEnhance extends ModifierTrait {

    public ModifierMagicEnhance() {
        super("magic_enhance", 0xe4ea60, 3, 20);
        this.addAspects(SpecialCategory.aspect, new ModifierAspect.FreeModifierAspect(0));
    }

    public boolean canApplyCustom(ItemStack stack) {
        //super.canApplyCustom(stack);
        NBTTagCompound tag = TinkerUtil.getModifierTag(stack, ModifierMagic.INSTANCE.identifier);
        int level = tag.getInteger("level");
        return level >= 4;
    }

    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        int amount = getModifierLevel(modifierTag);
        NBTTagCompound stats = rootCompound.getCompoundTag("Stats");
        float spellPotency = stats.getFloat("spellPotency");
        stats.setFloat("spellPotency", spellPotency * (amount * 0.01f + 1.0f ));
    }

    private static int getModifierLevel(NBTTagCompound mod) {
        return mod.getInteger("current");
    }
}
