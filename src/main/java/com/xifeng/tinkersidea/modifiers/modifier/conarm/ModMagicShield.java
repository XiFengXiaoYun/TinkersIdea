package com.xifeng.tinkersidea.modifiers.modifier.conarm;

import c4.conarm.common.armor.modifiers.ModResistantType;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class ModMagicShield extends ArmorModifierTrait {

    public ModMagicShield() {
        super("magic_shield", 0x75FA8D, 4, 1);
        this.aspects.clear();
        this.addAspects(new ModifierAspect.LevelAspect(this, 4), new ModifierAspect.FreeFirstModifierAspect(this, 1), new ModifierAspect.DataAspect(this));
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        float reduction = 0.0f;
        NBTTagCompound tag = TinkerUtil.getModifierTag(armor, this.identifier);
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(tag);
        if(source.isMagicDamage()) {
            reduction = 0.05f;
        }
        //使用damage，则四件护甲效果线性叠加；使用newDamage，则效果相乘
        newDamage -= reduction * data.level * damage;
        return newDamage;
    }
    @Override
    public boolean canApplyTogether(IToolMod mod) {
        return !(mod instanceof ModResistantType);
    }
}
