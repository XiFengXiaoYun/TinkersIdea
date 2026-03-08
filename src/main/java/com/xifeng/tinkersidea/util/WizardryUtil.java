package com.xifeng.tinkersidea.util;

import com.xifeng.tinkersidea.Weapons.wizardry.MagicNBT;
import com.xifeng.tinkersidea.Weapons.wizardry.SpellBladeHelper;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.RayTracer;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.TagUtil;

import javax.annotation.Nullable;

public class WizardryUtil {
    public static final int BASE_SPELL_SLOTS = 5;
    /** The number of ticks between each time a continuous spell is added to the player's recently-cast spells. */
    public static final int CONTINUOUS_TRACKING_INTERVAL = 20;
    /** The increase in progression for casting spells of the matching element. */
    public static final float ELEMENTAL_PROGRESSION_MODIFIER = 1.5f;
    /** The increase in progression for casting an undiscovered spell (can only happen once per spell for each player). */
    public static final float DISCOVERY_PROGRESSION_MODIFIER = 5f;
    /** The increase in progression for tiers that the player has already reached. */
    public static final float SECOND_TIME_PROGRESSION_MODIFIER = 1.5f;
    /** The fraction of progression lost when all recently-cast spells are the same as the one being cast. */
    public static final float MAX_PROGRESSION_REDUCTION = 0.75f;

    public static int getDistributedCost(int cost, int castingTick){
        int partialCost;
        if(castingTick % 20 == 0){
            partialCost = cost / 2 + cost % 2;
        }else if(castingTick % 10 == 0){
            partialCost = cost/2;
        }else{
            partialCost = 0;
        }
        return partialCost;
    }

    public static SpellModifiers calculateModifiers(ItemStack stack, EntityPlayer player, Spell spell, Tier tier, Element element){

        SpellModifiers modifiers = new SpellModifiers();

        int level = SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.range_upgrade);
        if(level > 0)
            modifiers.set(WizardryItems.range_upgrade, 1.0f + level * Constants.RANGE_INCREASE_PER_LEVEL, true);

        level = SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.duration_upgrade);
        if(level > 0)
            modifiers.set(WizardryItems.duration_upgrade, 1.0f + level * Constants.DURATION_INCREASE_PER_LEVEL, false);

        level = SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.blast_upgrade);
        if(level > 0)
            modifiers.set(WizardryItems.blast_upgrade, 1.0f + level * Constants.BLAST_RADIUS_INCREASE_PER_LEVEL, true);

        level = SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.cooldown_upgrade);
        if(level > 0)
            modifiers.set(WizardryItems.cooldown_upgrade, 1.0f - level * Constants.COOLDOWN_REDUCTION_PER_LEVEL, true);

        float progressionModifier = 1.0f - ((float) WizardData.get(player).countRecentCasts(spell) / WizardData.MAX_RECENT_SPELLS)
                * MAX_PROGRESSION_REDUCTION;

        if(element == spell.getElement()){
            modifiers.set(SpellModifiers.POTENCY, 1.0f + (tier.level + 1) * Constants.POTENCY_INCREASE_PER_TIER, true);
            progressionModifier *= ELEMENTAL_PROGRESSION_MODIFIER;
        }

        if(WizardData.get(player) != null){

            if(!WizardData.get(player).hasSpellBeenDiscovered(spell)){
                progressionModifier *= DISCOVERY_PROGRESSION_MODIFIER;
            }

            if(!WizardData.get(player).hasReachedTier(tier.next())){
                progressionModifier *= SECOND_TIME_PROGRESSION_MODIFIER;
            }
        }

        modifiers.set(SpellModifiers.PROGRESSION, progressionModifier, false);

        return modifiers;
    }

    public static boolean selectMinionTarget(EntityPlayer player, World world){

        RayTraceResult rayTrace = RayTracer.standardEntityRayTrace(world, player, 16, false);

        if(rayTrace != null && EntityUtils.isLiving(rayTrace.entityHit)){

            EntityLivingBase entity = (EntityLivingBase)rayTrace.entityHit;

            if(player.isSneaking() && WizardData.get(player) != null && WizardData.get(player).selectedMinion != null){

                ISummonedCreature minion = WizardData.get(player).selectedMinion.get();

                if(minion instanceof EntityLiving && minion != entity){
                    ((EntityLiving)minion).setAttackTarget(entity);
                    WizardData.get(player).selectedMinion = null;
                    return true;
                }
            }
        }

        return false;
    }

    public static int getMana(ItemStack stack) {
        MagicNBT nbt = MagicNBT.from(stack);
        return nbt.mana;
    }

    public static int getMaxMana(ItemStack stack) {
        MagicNBT nbt =  new MagicNBT(TagUtil.getToolTag(stack));
        return nbt.maxMana;
    }

    public static float getSpellPotency(ItemStack stack) {
        MagicNBT nbt = MagicNBT.from(stack);
        return nbt.spellPotency;
    }

    public static void addMana(ItemStack stack, int mana) {
        MagicNBT nbt = new MagicNBT(TagUtil.getToolTag(stack));
        nbt.mana += mana;
        TagUtil.setToolTag(stack, nbt.get());
    }

    public static void setMana(ItemStack stack, int mana) {
        MagicNBT nbt = new MagicNBT(TagUtil.getToolTag(stack));
        nbt.mana = mana;
        TagUtil.setToolTag(stack, nbt.get());
    }

    public static void setMaxMana(ItemStack stack, int maxMana) {
        MagicNBT nbt = new MagicNBT(TagUtil.getToolTag(stack));
        nbt.maxMana = maxMana;
        TagUtil.setToolTag(stack, nbt.get());
    }

    public static void consumeMana(ItemStack stack, int mana, @Nullable EntityLivingBase wielder) {
        if(!(wielder instanceof EntityPlayer) || !((EntityPlayer) wielder).isCreative()) {
            int amount = getMana(stack) - mana;
            setMana(stack, Math.max(amount, 0));
        }
    }

    public static void setPotency(ItemStack stack, float potency) {
        MagicNBT nbt = new MagicNBT(TagUtil.getToolTag(stack));
        nbt.spellPotency += potency;
        TagUtil.setToolTag(stack, nbt.get());
    }

}
