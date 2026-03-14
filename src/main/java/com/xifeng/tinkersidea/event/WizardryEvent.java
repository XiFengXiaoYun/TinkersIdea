package com.xifeng.tinkersidea.event;

import com.xifeng.tinkersidea.Weapons.wizardry.SpellBlade;
import com.xifeng.tinkersidea.Weapons.wizardry.SpellBladeHelper;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.ImbuementActivateEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber
public class WizardryEvent {
    @SubscribeEvent
    public static void imbuementActive(ImbuementActivateEvent event){
        if(event.world.isRemote) return;
        ItemStack input = event.input;
        if(input.isEmpty()) return;
        if(!(input.getItem() instanceof SpellBlade)) return;
        SpellBlade spellBlade = (SpellBlade)input.getItem();
        String e = SpellBladeHelper.getElement(input);
        if(!(e.equals(Element.MAGIC.name()))) {
            return;
        }
        Element[] elements = event.receptacleElements;
        if(Arrays.stream(elements).distinct().count() == 1L && elements[0] != null) {
            Element element = elements[0];
            spellBlade.setElement(input, element);
        }
    }
}
