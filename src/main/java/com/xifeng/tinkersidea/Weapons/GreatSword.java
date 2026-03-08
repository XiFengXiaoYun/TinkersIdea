package com.xifeng.tinkersidea.Weapons;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class GreatSword extends SwordCore {
    public GreatSword() {
        super(PartMaterialType.handle(TinkerTools.toughToolRod),
                PartMaterialType.head(TinkerTools.swordBlade),
                PartMaterialType.head(TinkerTools.swordBlade),
                PartMaterialType.extra(TinkerTools.wideGuard));
        this.addCategory(Category.WEAPON);

        setTranslationKey("greatsword").setRegistryName("greatsword");
    }

    @Override
    public float damagePotential() {
        return 0.875f;
    }

    @Override
    public double attackSpeed() {
        return 1.0f;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@ParametersAreNonnullByDefault World worldIn, EntityPlayer playerIn, @ParametersAreNonnullByDefault EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public int[] getRepairParts() {
        return new int[]{1, 2};
    }

    //Copy from tconstruct
    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit =  super.dealDamage(stack, player, entity, damage);
        if(hit && !ToolHelper.isBroken(stack)) {
            double d0 = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            boolean flag = true;
            float sweepLevel = 0;
            if (Enchantments.SWEEPING != null) {
                sweepLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
            }
            if(player instanceof EntityPlayer) {
                flag = ((EntityPlayer) player).getCooledAttackStrength(0.5F) > 0.75f;
            }
            boolean flag2 = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
            if(flag && !player.isSprinting() && !flag2 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
                for(EntityLivingBase entitylivingbase : player.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(3.0 + 0.75 * sweepLevel))) {
                    if(entitylivingbase != player && entitylivingbase != entity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D) {
                        entitylivingbase.knockBack(player, 0.75F, MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        super.dealDamage(stack, player, entitylivingbase, damage * (0.25f + 0.25f * sweepLevel));
                    }
                }

                player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 2.0F, 0.875F);
                if(player instanceof EntityPlayer) {
                    ((EntityPlayer) player).spawnSweepParticles();
                }
            }
        }
        return hit;
    }

    @Override
    protected ToolNBT buildTagData(List<Material> list) {
        HandleMaterialStats handle = list.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
        HeadMaterialStats head0 = list.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats head1 = list.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = list.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

        ToolNBT data = new ToolNBT();
        data.head(head0, head1);
        data.extra(binding);
        data.handle(handle);

        data.attack += 4;
        data.durability *= 1.0;
        return data;
    }

}
