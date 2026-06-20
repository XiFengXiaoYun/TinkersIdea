package com.xifeng.tinkersidea.modifiers.modifier.conarm;

import c4.conarm.lib.modifiers.ArmorModifierTrait;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ModifierLuck extends ArmorModifierTrait {
    private static final UUID MODIFIER = UUID.nameUUIDFromBytes("ModifierLuck".getBytes());

    public ModifierLuck() {
        super("luck", 0x75FA8D);
        this.addAspects(new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this), ModifierAspect.freeModifier);
    }

    @Override
    public void getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack, Multimap<String, AttributeModifier> attributeMap) {
        if(slot == EntityLiving.getSlotForItemStack(stack)) {
            attributeMap.put(SharedMonsterAttributes.LUCK.getName(), new AttributeModifier(MODIFIER, "Luck modifier", 1.5, 0));
        }
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD && super.canApplyCustom(stack);
    }
}
