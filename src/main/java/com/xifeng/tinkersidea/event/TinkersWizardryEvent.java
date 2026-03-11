package com.xifeng.tinkersidea.event;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.TinkerCraftingEvent;
import slimeknights.tconstruct.library.utils.TagUtil;

public class TinkersWizardryEvent {
    //保留原有的魔力及魔力容量
    @SubscribeEvent
    public static void toolCraftingEvent(TinkerCraftingEvent.ToolCraftingEvent event) {

    }

    @SubscribeEvent
    public static void toolModifyEvent(TinkerCraftingEvent.ToolModifyEvent event) {
        ItemStack old = event.getToolBeforeModification();
        NBTTagCompound tag = TagUtil.getToolTag(old);
        /*int oldMana = tag.getInteger("mana");
        int oldMaxMana = tag.getInteger("maxMana");
        float oldPotency = tag.getFloat("spellPotency");
        ItemStack stack = event.getItemStack();
        NBTTagCompound tag1 = TagUtil.getToolTag(stack);
        tag1.setInteger("mana", oldMana);
        tag1.setInteger("maxMana", oldMaxMana);
        tag1.setFloat("spellPotency", oldPotency);
        TagUtil.setToolTag(stack, tag1);

         */
        syncWizardryData(tag, event.getItemStack());
    }

    @SubscribeEvent
    public static void partReplaceEvent(TinkerCraftingEvent.ToolPartReplaceEvent event) {

    }

    private static void syncWizardryData(NBTTagCompound oldStats, ItemStack newStack) {
        int oldMana = oldStats.getInteger("mana");
        int oldMaxMana = oldStats.getInteger("maxMana");
        float oldPotency = oldStats.getFloat("spellPotency");
        NBTTagCompound tag1 = TagUtil.getToolTag(newStack);
        tag1.setInteger("mana", oldMana);
        tag1.setInteger("maxMana", oldMaxMana);
        tag1.setFloat("spellPotency", oldPotency);
        TagUtil.setToolTag(newStack, tag1);
    }

}
