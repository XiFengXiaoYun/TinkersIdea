package com.xifeng.tinkersidea.event;

import com.xifeng.tinkersidea.Weapons.wizardry.SpellBlade;
import com.xifeng.tinkersidea.Weapons.wizardry.SpellBladeHelper;
import com.xifeng.tinkersidea.config.ModConfig;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.ImbuementActivateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.Arrays;
import java.util.List;

public class WizardryEvent {
    //TODO: 使用其他方式实现
    @SubscribeEvent
    public static void imbuementActive(ImbuementActivateEvent event){
        if(event.world.isRemote) return;
        ItemStack input = event.input;
        if(input.isEmpty()) return;
        if(!(input.getItem() instanceof SpellBlade)) return;
        String e = SpellBladeHelper.getElement(input);
        if(!(e.equals(Element.MAGIC.name()))) {
            return;
        }
        Element[] elements = event.receptacleElements;
        if(Arrays.stream(elements).distinct().count() == 1L && elements[0] != null) {
            Element element = elements[0];
            SpellBladeHelper.setElement(input, element.name());
        }
    }

    @SubscribeEvent
    public static void spellBladeCastEvent(LivingHurtEvent event){
        //法术也能触发词条效果, 可通过配置文件启用
        if(!ModConfig.spellTriggerCertainTraits) return;
        DamageSource source = event.getSource();
        System.out.println(source.damageType);
        if(source.getTrueSource() == null || !source.isMagicDamage()) return;
        if(!source.damageType.equals("wizardry_magic") && !source.damageType.equals("indirect_wizardry_magic")) return;
        Entity entity = source.getTrueSource();
        if(entity instanceof EntityLivingBase){
            //仅主手
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            ItemStack stack = livingBase.getHeldItem(EnumHand.MAIN_HAND);
            if(stack.isEmpty() || !(stack.getItem() instanceof SpellBlade)) return;
            float amount = event.getAmount();
            List<ITrait> traits = TinkerUtil.getTraitsOrdered(stack);
            boolean isCritical = false;
            for (ITrait trait : traits){
                isCritical = trait.isCriticalHit(stack, livingBase, event.getEntityLiving());
            }
            if(isCritical){
                //嚯嚯，夸张哦
                amount *= 1.5f;
            }
            float newAmount = amount;
            for(ITrait trait : traits){
                amount =  trait.damage(stack, livingBase, event.getEntityLiving(), amount, newAmount, isCritical);
            }
            event.setAmount(amount);
        }
    }
}
