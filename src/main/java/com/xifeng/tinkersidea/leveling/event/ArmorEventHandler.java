package com.xifeng.tinkersidea.leveling.event;

import c4.conarm.lib.events.ArmoryEvent;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.leveling.modifier.ModifierArmorLeveling;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public final class ArmorEventHandler {

    @SubscribeEvent
    public static void onArmorBuild(ArmoryEvent.OnItemBuilding event) {
        if(!TinkerUtil.hasModifier(event.tag, ModifierArmorLeveling.ID)) {
            ModifierArmorLeveling.instance.apply(event.tag);
            NBTTagList tagList = TagUtil.getModifiersTagList(event.tag);
            int index = TinkerUtil.getIndexInCompoundList(tagList, ModifierArmorLeveling.ID);
            NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);
            if(!modifierTag.hasKey("maxExp")) {
                modifierTag.setInteger("maxExp", ModConfig.Leveling.baseMaxExpArmor);
            }
        }
    }
}
