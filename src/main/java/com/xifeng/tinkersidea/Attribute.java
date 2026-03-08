package com.xifeng.tinkersidea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*Something to do*/
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class Attribute {
    public static final IAttribute MAGIC = new RangedAttribute(null, "ti.magicPotency", 1.0, 0.0, 10.0).setShouldWatch(false);

    @SubscribeEvent
    public static void entityInit(EntityEvent.EntityConstructing event) {
        final Entity e = event.getEntity();
        if (e instanceof EntityPlayer) {
            final EntityLivingBase living = (EntityLivingBase) e;
            living.getAttributeMap().registerAttribute(MAGIC);
        }
    }

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        if(!event.getSource().isMagicDamage()) return;
        if(event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            double modifier = player.getEntityAttribute(MAGIC).getAttributeValue();
            event.setAmount((float) (event.getAmount() * modifier));
        }
    }
}
