package com.xifeng.tinkersidea.Weapons.common;

import com.google.common.collect.Multimap;
import com.xifeng.tinkersidea.entity.EntityLance;
import com.xifeng.tinkersidea.util.DamageUtil;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import slimeknights.tconstruct.tools.traits.TraitEnderference;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

//TODO：突刺长枪：长距离攻击，小范围横扫，右键蓄力，松开后可以发射并穿透目标进行攻击
//TODO: 新的强化：穿透：增加穿透目标的数量；反弹：碰到方块后可以反射一次；忠诚：射到方块上可以自动回来；超蓄能：大幅降低蓄力时间
public class ThrustingLance extends ProjectileCore {
    private static final UUID uuid = UUID.fromString("8d60ef90-ad99-4e77-8276-d8b0f76691bf");
    public ThrustingLance() {
        super(
                PartMaterialType.extra(TinkerTools.toughToolRod),
                PartMaterialType.head(TinkerTools.swordBlade),
                PartMaterialType.handle(TinkerTools.toughToolRod)
        );
        this.durabilityPerAmmo = 3;
        this.addCategory(Category.WEAPON);
        setTranslationKey("thrusting_lance").setRegistryName("thrusting_lance");
    }

    @Override
    public float damagePotential() {
        return 0.75f;
    }

    @Override
    public double attackSpeed() {
        return 1.2f;
    }

    @Override
    public ProjectileNBT buildTagData(List<Material> list) {
        HeadMaterialStats head = list.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = list.get(1).getStatsOrUnknown(MaterialTypes.EXTRA);
        HandleMaterialStats handle = list.get(2).getStatsOrUnknown(MaterialTypes.HANDLE);

        ProjectileNBT tool = new ProjectileNBT();
        tool.head(head);
        tool.extra(binding);
        tool.handle(handle);

        tool.attack += 1.0f;
        return tool;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if(slot == EntityEquipmentSlot.MAINHAND && !ToolHelper.isBroken(stack)) {
            multimap.put("generic.reachDistance", new AttributeModifier(uuid, "thrusting_lance", 2.0, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ToolHelper.getActualAttack(stack), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ToolHelper.getActualAttackSpeed(stack) - 4d, 0));
        }
        TinkerUtil.getTraitsOrdered(stack).forEach((trait) -> trait.getAttributeModifiers(slot, stack, multimap));
        return multimap;
    }


    @Override
    public int[] getRepairParts() {
        return new int[]{1};
    }

    @Override
    public EntityProjectileBase getProjectile(@Nonnull ItemStack itemStack, @Nonnull ItemStack launcher, World world, EntityPlayer entityPlayer, float speed, float inaccuracy, float power, boolean usedAmmo) {
        return new EntityLance(world, entityPlayer, speed, inaccuracy, this.getProjectileStack(itemStack, world, entityPlayer, usedAmmo), launcher);
    }

    @MethodsReturnNonnullByDefault
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return !ToolHelper.isBroken(stack) ? EnumAction.BOW : EnumAction.NONE;
    }

    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit =  super.dealDamage(stack, player, entity, damage);
        if(hit && !ToolHelper.isBroken(stack)) {
            double d0 = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            boolean flag = true;
            double reach = player.getDistanceSq(entity) + 1.0;
            if(player instanceof EntityPlayer) {
                flag = ((EntityPlayer) player).getCooledAttackStrength(0.5F) > 0.75f;
            }
            boolean flag2 = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
            if(flag && !player.isSprinting() && !flag2 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
                for(EntityLivingBase entitylivingbase : DamageUtil.getEntitiesInRange(entity, player.getEntityWorld(), 1.5)) {

                    if(entitylivingbase != player && entitylivingbase != entity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) <= reach) {
                        entitylivingbase.knockBack(player, 0.5F, MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        super.dealDamage(stack, player, entitylivingbase, damage * 0.125f);
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
    public boolean dealDamageRanged(ItemStack stack, Entity projectile, EntityLivingBase player, Entity entity, float damage) {
        DamageSource damageSource = new EntityDamageSourceIndirect(DAMAGE_TYPE_PROJECTILE, projectile, player).setProjectile();
        int pierce = 0;
        double decay = 0.0;
        if(projectile instanceof EntityLance) {
            pierce = ((EntityLance) projectile).piercingEntity;
            decay = ((EntityLance) projectile).decay;
        }
        if(entity instanceof EntityEnderman && ((EntityEnderman) entity).getActivePotionEffect(TraitEnderference.Enderference) != null) {
            damageSource = new DamageSourceProjectileForEndermen(DAMAGE_TYPE_PROJECTILE, projectile, player);
        }
        float newDamage = (float) (damage * (Math.pow(1 - decay, pierce)));
        return entity.attackEntityFrom(damageSource, newDamage);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return ToolHelper.attackEntity(stack, this, player, entity);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        float speed = ToolHelper.getActualAttackSpeed(stack);
        int time = Math.round(20f / speed);
        if (time < target.hurtResistantTime / 2) {
            target.hurtResistantTime = (target.hurtResistantTime + time) / 2;
            target.hurtTime = (target.hurtTime + time) / 2;
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return !ToolHelper.isBroken(stack) ? 7200 : 0;
    }

    @MethodsReturnNonnullByDefault
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!ToolHelper.isBroken(itemStackIn)) {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase living, int timeLeft) {
        if(ToolHelper.isBroken(stack) || !(living instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) living;
        ForgeEventFactory.onArrowNock(stack, world, player, player.getActiveHand(), this.getCurrentAmmo(stack) > 0);
        int useTime = this.getMaxItemUseDuration(stack) - timeLeft;
        useTime = ForgeEventFactory.onArrowLoose(stack, world, player, useTime, this.getCurrentAmmo(stack) > 0);
        if(useTime <= 10) return;
        float progress = getProgress(useTime);
        float power = getPower(progress);
        float speed = getSpeed(progress, stack);
        if(!world.isRemote) {
            boolean usedAmmo = !player.capabilities.isCreativeMode && useAmmo(stack, living);
            EntityProjectileBase projectile = this.getProjectile(stack, stack, world, player, speed, 0f,power, usedAmmo);
            world.spawnEntity(projectile);
        }
        TinkerRangedWeapons.proxy.updateEquippedItemForRendering(living.getActiveHand());
        TagUtil.setResetFlag(stack, true);
    }

    private static float getPower(float progress) {
        return ItemBow.getArrowVelocity((int)(progress * 20f)) * progress * 1.6f;
    }

    private static float getSpeed(float progress, ItemStack stack) {
        int durability = TagUtil.getToolStats(stack).durability + 1;
        float maxSpeed = Math.max(1.0f, (float) (1.75 - Math.log10(durability) * 0.125));
        return maxSpeed * progress;
    }

    private static float getProgress(int useTicks) {
        return Math.min(1f, useTicks / (float) 20);
    }
}
