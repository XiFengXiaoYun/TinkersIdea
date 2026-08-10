package com.xifeng.tinkersidea.event;

import com.xifeng.tinkersidea.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.harvest.TinkerHarvestTools;
import slimeknights.tconstruct.tools.melee.TinkerMeleeWeapons;

public class EventHandler {
    public static final int maxCooldown = ModConfig.Tweaks.maxCooldown;
    //make battle sign not so OP
    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        if(event.isCanceled()) return;

        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;

        if(event.getSource().isUnblockable() ||
                event.getSource().isMagicDamage() ||
                event.getSource().isExplosion() ||
                event.getSource().isProjectile()) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack battlesign = player.getActiveItemStack();
        if(battlesign.isEmpty() || battlesign.getItem() != TinkerMeleeWeapons.battleSign || ToolHelper.isBroken(battlesign)) return;

        Entity attacker = event.getSource().getTrueSource();
        if(!(attacker instanceof EntityLivingBase)) return;

        ItemStack weapon = ((EntityLivingBase) attacker).getActiveItemStack();
        if(canDisableShield(weapon, (EntityLivingBase) attacker)) {
            float durability = battlesign.getMaxDamage() -  battlesign.getItemDamage();
            float damage = event.getAmount();
            int coolDown = (int) (Math.log(damage + 1) - 0.25 * Math.log10(durability + 1)) * 10;
            if(coolDown <= 0) return;
            int actualCooldown = Math.min(coolDown, maxCooldown);
            player.getCooldownTracker().setCooldown(battlesign.getItem(), actualCooldown);
        }
    }

    private static boolean canDisableShield(ItemStack weapon, EntityLivingBase attacker) {
        if(weapon == null || weapon.isEmpty()) return false;
        return canEntityDisableShield(attacker) || canWeaponDisableShield(weapon);
    }

    private static boolean canEntityDisableShield(EntityLivingBase attacker) {
        ResourceLocation rl = EntityList.getKey(attacker);
        if(rl == null) return false;
        for(String entityId : ModConfig.Tweaks.strongEntities) {
            if(entityId.equals(rl.toString())) return true;
        }
        return false;
    }

    private static boolean canWeaponDisableShield(ItemStack weapon) {
        if(weapon.getItem() instanceof ItemAxe) return true;
        if(Math.random() <= ModConfig.Tweaks.chance) {
            return weapon.getItem() == TinkerMeleeWeapons.cleaver || weapon.getItem() == TinkerHarvestTools.hatchet;
        }
        return false;
    }
}
