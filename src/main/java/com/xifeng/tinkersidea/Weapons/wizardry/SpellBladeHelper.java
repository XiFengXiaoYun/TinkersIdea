package com.xifeng.tinkersidea.Weapons.wizardry;

import com.xifeng.tinkersidea.util.WizardryUtil;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ItemCrystal;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.NBTExtras;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public final class SpellBladeHelper {

    public static final String SPELL_ARRAY_KEY = "spells";
    public static final String SELECTED_SPELL_KEY = "selectedSpell";
    public static final String COOLDOWN_ARRAY_KEY = "cooldown";
    public static final String MAX_COOLDOWN_ARRAY_KEY = "maxCooldown";
    public static final String UPGRADES_KEY = "upgrades";
    public static final String PROGRESSION_KEY = "progression";
    public static final String WIZARDRY_DATA = "WizardryData";
    public static final String TIER = "tier";
    public static final String ELEMENT = "element";

    private static final String DefaultTier = Tier.NOVICE.name();
    private static final String DefaultElement = Element.MAGIC.name();

    private static final HashMap<Item, String> upgradeMap = new HashMap<>();

    public static void addUpgrade() {
        upgradeMap.put(WizardryItems.condenser_upgrade, "condenser");
        upgradeMap.put(WizardryItems.storage_upgrade, "storage");
        upgradeMap.put(WizardryItems.siphon_upgrade, "siphon");
        upgradeMap.put(WizardryItems.range_upgrade, "range");
        upgradeMap.put(WizardryItems.duration_upgrade, "duration");
        upgradeMap.put(WizardryItems.cooldown_upgrade, "cooldown");
        upgradeMap.put(WizardryItems.blast_upgrade, "blast");
        upgradeMap.put(WizardryItems.attunement_upgrade, "attunement");
        upgradeMap.put(WizardryItems.melee_upgrade, "melee");
    }

    public static NBTTagCompound getWizardryData(ItemStack stack) {
        NBTTagCompound wizardryTag = new NBTTagCompound();
        if(stack.getTagCompound() != null) {
            if(!stack.getTagCompound().hasKey(WIZARDRY_DATA)) {
                wizardryTag.setString(ELEMENT, DefaultElement);
                wizardryTag.setString(TIER, DefaultTier);
                stack.getTagCompound().setTag(WIZARDRY_DATA, wizardryTag);
            }
            return stack.getTagCompound().getCompoundTag(WIZARDRY_DATA);
        }
        return wizardryTag;
    }

    public static String getTier(ItemStack stack) {
        NBTTagCompound nbt = getWizardryData(stack);
        if(nbt.hasKey(TIER)) return nbt.getString(TIER);
        nbt.setString(TIER, DefaultTier);
        return DefaultTier;
    }

    public static void setTier(ItemStack stack, String tierName) {
        NBTTagCompound nbt = getWizardryData(stack);
        nbt.setString(TIER, tierName);
    }

    public static String getElement(ItemStack stack) {
        NBTTagCompound nbt = getWizardryData(stack);
        if(nbt.hasKey(ELEMENT)) return nbt.getString(ELEMENT);
        nbt.setString(ELEMENT, DefaultElement);
        return DefaultElement;
    }

    public static void setElement(ItemStack stack, String element) {
        NBTTagCompound nbt = getWizardryData(stack);
        nbt.setString(ELEMENT, element);
    }

    public static Spell[] getSpells(ItemStack stack) {
        Spell[] spells = new Spell[0];
        NBTTagCompound nbt  = getWizardryData(stack);
        if(!nbt.isEmpty()){
            if(!nbt.hasKey(SPELL_ARRAY_KEY)) {
                nbt.setIntArray(SPELL_ARRAY_KEY, new int[0]);
            }
            int[] spellIDs = nbt.getIntArray(SPELL_ARRAY_KEY);
            spells = new Spell[spellIDs.length];
            for(int i = 0; i < spellIDs.length; i++){
                spells[i] = Spell.byMetadata(spellIDs[i]);
            }
        }
        return spells;
    }

    public static void setSpells(ItemStack wand, Spell[] spells){
        int[] spellIDs = new int[spells.length];
        for(int i = 0; i < spells.length; i++){
            spellIDs[i] = spells[i] != null ? spells[i].metadata() : Spells.none.metadata();
        }
        getWizardryData(wand).setIntArray(SPELL_ARRAY_KEY, spellIDs);
    }

    public static Spell getCurrentSpell(ItemStack wand){
        Spell[] spells = getSpells(wand);
            int selectedSpell = getWizardryData(wand).getInteger(SELECTED_SPELL_KEY);
            if(selectedSpell >= 0 && selectedSpell < spells.length){
                return spells[selectedSpell];
            }
        return Spells.none;
    }

    public static Spell getNextSpell(ItemStack wand){
        Spell[] spells = getSpells(wand);
        int index = getNextSpellIndex(wand);
        if(index >= 0 && index < spells.length){
            return spells[index];
        }
        return Spells.none;
    }

    private static int getNextSpellIndex(ItemStack wand){
        int numberOfSpells = getSpells(wand).length;
        int spellIndex = getWizardryData(wand).getInteger(SELECTED_SPELL_KEY);
        if(spellIndex >= numberOfSpells - 1){
            spellIndex = 0;
        }else{
            spellIndex++;
        }
        return spellIndex;
    }

    public static Spell getPreviousSpell(ItemStack wand){
        Spell[] spells = getSpells(wand);
        int index = getPreviousSpellIndex(wand);
        if(index >= 0 && index < spells.length){
            return spells[index];
        }
        return Spells.none;
    }

    public static void selectNextSpell(ItemStack wand){
        getWizardryData(wand).setInteger(SELECTED_SPELL_KEY, getNextSpellIndex(wand));
    }

    public static void selectPreviousSpell(ItemStack wand){
        getWizardryData(wand).setInteger(SELECTED_SPELL_KEY, getPreviousSpellIndex(wand));
    }

    public static boolean selectSpell(ItemStack wand, int index){
        if(index < 0 || index > getSpells(wand).length) return false;
        getWizardryData(wand).setInteger(SELECTED_SPELL_KEY, index);
        return true;
    }

    private static int getPreviousSpellIndex(ItemStack wand){
        int numberOfSpells = getSpells(wand).length;
        int spellIndex = getWizardryData(wand).getInteger(SELECTED_SPELL_KEY);
        if(spellIndex <= 0){
            spellIndex = Math.max(0, numberOfSpells - 1);
        }else{
            spellIndex--;
        }
        return spellIndex;
    }

    //CoolDown

    public static int[] getCooldowns(ItemStack wand){
        int[] cooldowns = new int[0];
        NBTTagCompound nbt = getWizardryData(wand);
        if(!nbt.hasKey(COOLDOWN_ARRAY_KEY)){
            nbt.setIntArray(COOLDOWN_ARRAY_KEY, cooldowns);
            return cooldowns;
        }
        return nbt.getIntArray(COOLDOWN_ARRAY_KEY);
    }

    public static void setCooldowns(ItemStack wand, int[] cooldowns){
        getWizardryData(wand).setIntArray(COOLDOWN_ARRAY_KEY, cooldowns);
    }

    public static void decrementCooldowns(ItemStack wand){
        int[] cooldowns = getCooldowns(wand);
        if(cooldowns.length == 0) return;
        for(int i = 0; i < cooldowns.length; i++){
            if(cooldowns[i] > 0) cooldowns[i]--;
            if(cooldowns[i] < 0) cooldowns[i] = 0;
        }
        setCooldowns(wand, cooldowns);
    }

    public static int getCurrentCooldown(ItemStack wand){
        int[] cooldowns = getCooldowns(wand);
        int selectedSpell  = getWizardryData(wand).getInteger(SELECTED_SPELL_KEY);
        if(selectedSpell < 0 || cooldowns.length <= selectedSpell) return 0;
        return cooldowns[selectedSpell];
    }

    public static int getNextCooldown(ItemStack wand){
        int[] cooldowns = getCooldowns(wand);
        int nextSpell = getNextSpellIndex(wand);
        if(nextSpell < 0 || cooldowns.length <= nextSpell) return 0;
        return cooldowns[nextSpell];
    }

    public static int getPreviousCooldown(ItemStack wand){
        int[] cooldowns = getCooldowns(wand);
        int previousSpell = getPreviousSpellIndex(wand);
        if(previousSpell < 0 || cooldowns.length <= previousSpell) return 0;
        return cooldowns[previousSpell];
    }

    public static void setCurrentCooldown(ItemStack wand, int cooldown){
        NBTTagCompound nbt = getWizardryData(wand);
        int[] cooldowns = getCooldowns(wand);
        int selectedSpell = nbt.getInteger(SELECTED_SPELL_KEY);
        int spellCount = getSpells(wand).length;
        if(spellCount <= selectedSpell) return;
        if(cooldowns.length <= selectedSpell) cooldowns = new int[spellCount];
        if(cooldown <= 0) cooldown = 1;
        cooldowns[selectedSpell] = cooldown;
        setCooldowns(wand, cooldowns);
        int[] maxCooldowns = getMaxCooldowns(wand);
        if(maxCooldowns.length <= selectedSpell) maxCooldowns = new int[spellCount];
        maxCooldowns[selectedSpell] = cooldown;
        setMaxCooldowns(wand, maxCooldowns);
    }

    public static int[] getMaxCooldowns(ItemStack wand){
        int[] cooldowns = new int[0];
        NBTTagCompound nbt = getWizardryData(wand);
        if(nbt.hasKey(MAX_COOLDOWN_ARRAY_KEY)){
            return getWizardryData(wand).getIntArray(MAX_COOLDOWN_ARRAY_KEY);
        }
        return cooldowns;
    }

    public static void setMaxCooldowns(ItemStack wand, int[] cooldowns){
        getWizardryData(wand).setIntArray(MAX_COOLDOWN_ARRAY_KEY, cooldowns);
    }

    public static int getCurrentMaxCooldown(ItemStack wand){
        int[] cooldowns = getMaxCooldowns(wand);
        int selectedSpell = getWizardryData(wand).getInteger(SELECTED_SPELL_KEY);
        if(selectedSpell < 0 || cooldowns.length <= selectedSpell) return 0;
        return cooldowns[selectedSpell];
    }

    //Progression
    public static void setProgression(ItemStack wand, int progression){
        getWizardryData(wand).setInteger(PROGRESSION_KEY, progression);
    }

    public static int getProgression(ItemStack wand){
        NBTTagCompound nbt = getWizardryData(wand);
        if(!nbt.hasKey(PROGRESSION_KEY)) {
            nbt.setInteger(PROGRESSION_KEY, 0);
            return 0;
        }
        return getWizardryData(wand).getInteger(PROGRESSION_KEY);
    }

    public static void addProgression(ItemStack wand, int progression){
        setProgression(wand, getProgression(wand) + progression);
    }

    //Upgrade
    public static int getUpgradeLevel(ItemStack wand, Item upgrade){
        String key = upgradeMap.get(upgrade);
        NBTTagCompound nbt = getWizardryData(wand);
        if(nbt.hasKey(UPGRADES_KEY) && key != null){
            return nbt.getCompoundTag(UPGRADES_KEY).getInteger(key);
        }
        return 0;
    }

    public static int getTotalUpgrades(ItemStack wand){
        int totalUpgrades = 0;
        for(Item item : upgradeMap.keySet()){
            totalUpgrades += getUpgradeLevel(wand, item);
        }
        return totalUpgrades;
    }

    public static void applyUpgrade(ItemStack wand, Item upgrade){
        NBTTagCompound nbt = getWizardryData(wand);
        if(!nbt.hasKey(UPGRADES_KEY)) NBTExtras.storeTagSafely(nbt, UPGRADES_KEY, new NBTTagCompound());
        NBTTagCompound upgrades = nbt.getCompoundTag(UPGRADES_KEY);
        String key = upgradeMap.get(upgrade);
        if(key != null) upgrades.setInteger(key, upgrades.getInteger(key) + 1);
        NBTExtras.storeTagSafely(nbt, UPGRADES_KEY, upgrades);
    }

    public static boolean isWandUpgrade(Item upgrade){
        return upgradeMap.containsKey(upgrade);
    }

    public static Set<Item> getSpecialUpgrades(){
        return Collections.unmodifiableSet(SpellBladeHelper.upgradeMap.keySet());
    }

    public static boolean isManaFull(ItemStack stack) {
        return WizardryUtil.getMaxMana(stack) == WizardryUtil.getMana(stack);
    }

    public static void rechargeMana(ItemStack stack, int mana) {
        WizardryUtil.addMana(stack, mana);
    }

    public static boolean rechargeManaOnApplyButtonPressed(Slot centre, Slot crystals) {
        boolean changed = false;
        if (!(centre.getStack().getItem() instanceof SpellBlade)) {
            return false;
        }
        ItemStack item = centre.getStack();
        if (crystals.getStack() != ItemStack.EMPTY && !isManaFull(item)) {
            int chargeDepleted = WizardryUtil.getMaxMana(item) - WizardryUtil.getMana(item);
            int manaPerItem = crystals.getStack().getItem() instanceof IManaStoringItem ?
                    ((IManaStoringItem) crystals.getStack().getItem()).getMana(crystals.getStack()) :
                    crystals.getStack().getItem() instanceof ItemCrystal ? Constants.MANA_PER_CRYSTAL : Constants.MANA_PER_SHARD;

            if (crystals.getStack().getItem() == WizardryItems.crystal_shard) {manaPerItem = Constants.MANA_PER_SHARD;}
            if (crystals.getStack().getItem() == WizardryItems.grand_crystal) {manaPerItem = Constants.GRAND_CRYSTAL_MANA;}

            if (crystals.getStack().getCount() * manaPerItem < chargeDepleted) {
                rechargeMana(item, crystals.getStack().getCount() * manaPerItem);
                crystals.decrStackSize(crystals.getStack().getCount());
            } else {
                int max = WizardryUtil.getMaxMana(item);
                WizardryUtil.setMana(item, max);
                crystals.decrStackSize((int) Math.ceil(((double) chargeDepleted) / manaPerItem));
            }
            changed = true;
        }
        return changed;
    }
}
