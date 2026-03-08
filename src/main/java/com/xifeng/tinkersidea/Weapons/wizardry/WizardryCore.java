package com.xifeng.tinkersidea.Weapons.wizardry;

import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.util.WizardryUtil;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.data.SpellGlyphData;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryAdvancementTriggers;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.SpellProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.tools.SwordCore;

import java.util.Arrays;
import java.util.List;

public class WizardryCore {
    private final IManaStoringItem manaStoringItem;
    private final Element element;
    private final SwordCore core;
    public WizardryCore(ISpellCastingItem spellCastingItem, Element element, SwordCore core) {
        this.manaStoringItem = (IManaStoringItem) spellCastingItem;
        this.core=core;
        //this.tier= tier;
        this.element=element;
    }

    public Tier getTier(ItemStack stack) {
        return Tier.valueOf(SpellBladeHelper.getTier(stack));
    }

    public void setTier(ItemStack stack, String tierId) {
        //Tier tier = Tier.valueOf(tierId);
        SpellBladeHelper.setTier(stack, tierId);
    }

    public Spell getCurrentSpell(ItemStack stack){
        return SpellBladeHelper.getCurrentSpell(stack);
    }

    public Spell getNextSpell(ItemStack stack){
        return SpellBladeHelper.getNextSpell(stack);
    }

    public Spell getPreviousSpell(ItemStack stack){
        return SpellBladeHelper.getPreviousSpell(stack);
    }

    public Spell[] getSpells(ItemStack stack){
        return SpellBladeHelper.getSpells(stack);
    }

    public void selectNextSpell(ItemStack stack){
        SpellBladeHelper.selectNextSpell(stack);
    }

    public void selectPreviousSpell(ItemStack stack){
        SpellBladeHelper.selectPreviousSpell(stack);
    }

    public boolean selectSpell(ItemStack stack, int index){
        return SpellBladeHelper.selectSpell(stack, index);
    }

    public int getCurrentCooldown(ItemStack stack){
        return SpellBladeHelper.getCurrentCooldown(stack);
    }

    public int getCurrentMaxCooldown(ItemStack stack){
        return SpellBladeHelper.getCurrentMaxCooldown(stack);
    }

    public void addMana(ItemStack stack, int mana){
        WizardryUtil.addMana(stack, mana);
    }

    public int getMana(ItemStack stack){
        return WizardryUtil.getMana(stack);
    }

    public int getManaCapacity(ItemStack stack){
        return WizardryUtil.getMaxMana(stack);
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeldInMainhand){

        boolean isHeld = isHeldInMainhand || entity instanceof EntityLivingBase && ItemStack.areItemStacksEqual(stack, ((EntityLivingBase) entity).getHeldItemOffhand());
        if (!Wizardry.settings.wandsMustBeHeldToDecrementCooldown || isHeld) {
            SpellBladeHelper.decrementCooldowns(stack);
        }

        if(!world.isRemote && !SpellBladeHelper.isManaFull(stack) && world.getTotalWorldTime() % Constants.CONDENSER_TICK_INTERVAL == 0){
            int baseAmount = SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.condenser_upgrade);
            int amount = (int)(baseAmount * Wizardry.settings.condenserAmountMultiplier);
            SpellBladeHelper.rechargeMana(stack, amount);
        }
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack){
        if(ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack)) return true;
        return core.canContinueUsing(oldStack, newStack);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
        if(!oldStack.isEmpty() || !newStack.isEmpty()){
            if(oldStack.getItem() == newStack.getItem() && !slotChanged && oldStack.getItem() instanceof SpellBlade
                    && newStack.getItem() instanceof SpellBlade
                    && SpellBladeHelper.getCurrentSpell(oldStack) == SpellBladeHelper.getCurrentSpell(newStack))
                return false;
        }
        return false;
    }

    public EnumAction getItemUseAction(ItemStack itemstack){
        return SpellBladeHelper.getCurrentSpell(itemstack).action;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> text, net.minecraft.client.util.ITooltipFlag advanced){
        EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
        if (player == null) { return; }

        Tier tier = getTier(stack);

        if(element != null) text.add(Wizardry.proxy.translate("item." + Wizardry.MODID + ":wand.buff",
                new Style().setColor(TextFormatting.DARK_GRAY),
                (int)((tier.level + 1) * Constants.POTENCY_INCREASE_PER_TIER * 100 + 0.5f), element.getDisplayName()));
        Spell spell = SpellBladeHelper.getCurrentSpell(stack);
        boolean discovered = !Wizardry.settings.discoveryMode || player.isCreative() || WizardData.get(player) == null
                || WizardData.get(player).hasSpellBeenDiscovered(spell);
        text.add(Wizardry.proxy.translate("item." + Wizardry.MODID + ":wand.spell", new Style().setColor(TextFormatting.GRAY),
                discovered ? spell.getDisplayNameWithFormatting() : "#" + TextFormatting.BLUE + SpellGlyphData.getGlyphName(spell, player.world)));
        if(advanced.isAdvanced()){
            text.add(Wizardry.proxy.translate("item." + Wizardry.MODID + ":wand.mana", new Style().setColor(TextFormatting.BLUE),
                    this.getMana(stack), this.getManaCapacity(stack)));

            text.add(Wizardry.proxy.translate("item." + Wizardry.MODID + ":wand.progression", new Style().setColor(TextFormatting.GRAY),
                    SpellBladeHelper.getProgression(stack), tier.level < Tier.MASTER.level ? tier.next().getProgression() : 0));
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        ItemStack stack = player.getHeldItem(hand);
        if(WizardryUtil.selectMinionTarget(player, world)) return new ActionResult<>(EnumActionResult.SUCCESS, stack);

        Spell spell = SpellBladeHelper.getCurrentSpell(stack);
        Tier tier = getTier(stack);
        SpellModifiers modifiers = WizardryUtil.calculateModifiers(stack, player, spell, tier, element);

        if(canCast(stack, spell, player, hand, 0, modifiers)){
            int chargeup = (int)(spell.getChargeup() * modifiers.get(SpellModifiers.CHARGEUP));

            if(spell.isContinuous || chargeup > 0){
                if(!player.isHandActive()){
                    player.setActiveHand(hand);
                    if(WizardData.get(player) != null) WizardData.get(player).itemCastingModifiers = modifiers;
                    if(chargeup > 0 && world.isRemote) Wizardry.proxy.playChargeupSound(player);
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                }
            }else{
                if(cast(stack, spell, player, hand, 0, modifiers)){
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase user, int count){
        if(user instanceof EntityPlayer){

            EntityPlayer player = (EntityPlayer)user;

            Spell spell = SpellBladeHelper.getCurrentSpell(stack);

            Tier tier = getTier(stack);

            SpellModifiers modifiers;

            if(WizardData.get(player) != null){
                modifiers = WizardData.get(player).itemCastingModifiers;
            }else{
                modifiers = WizardryUtil.calculateModifiers(stack, (EntityPlayer)user, spell, tier, element); // Fallback to the old way, should never be used
            }

            int useTick = stack.getMaxItemUseDuration() - count;
            int chargeup = (int)(spell.getChargeup() * modifiers.get(SpellModifiers.CHARGEUP));

            if(spell.isContinuous){
                if(useTick >= chargeup){
                    int castingTick = useTick - chargeup;
                    if(castingTick == 0 || canCast(stack, spell, player, player.getActiveHand(), castingTick, modifiers)){
                        cast(stack, spell, player, player.getActiveHand(), castingTick, modifiers);
                    }else{
                       player.stopActiveHand();
                    }
                }
            }else{
                if(chargeup > 0 && useTick == chargeup){
                    cast(stack, spell, player, player.getActiveHand(), 0, modifiers);
                }
            }
        }
    }

    public boolean canCast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers){

        if(castingTick == 0){
            if(MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.WAND, spell, caster, modifiers))) return false;
        }else{
            if(MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Tick(SpellCastEvent.Source.WAND, spell, caster, modifiers, castingTick))) return false;
        }

        int cost = (int)(spell.getCost() * modifiers.get(SpellModifiers.COST) + 0.1f);

        Tier tier = getTier(stack);

        if(spell.isContinuous) cost = WizardryUtil.getDistributedCost(cost, castingTick);

        return cost <= this.getMana(stack)
                && spell.getTier().level <= tier.level
                && (SpellBladeHelper.getCurrentCooldown(stack) == 0 || caster.isCreative());
    }

    public boolean cast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers){
        World world = caster.world;

        if(world.isRemote && !spell.isContinuous && spell.requiresPacket()) return false;

        if(spell.cast(world, caster, hand, castingTick, modifiers)){

            if(castingTick == 0) MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.WAND, spell, caster, modifiers));

            if(!world.isRemote){
                if(!spell.isContinuous && spell.requiresPacket()){
                    IMessage msg = new PacketCastSpell.Message(caster.getEntityId(), hand, spell, modifiers);
                    WizardryPacketHandler.net.sendToDimension(msg, world.provider.getDimension());
                }
                int cost = (int)(spell.getCost() * modifiers.get(SpellModifiers.COST) + 0.1f);
                if(spell.isContinuous) cost = WizardryUtil.getDistributedCost(cost, castingTick);

                if(cost > 0) {
                    WizardryUtil.consumeMana(stack, cost, caster);
                }

            }

            caster.setActiveHand(hand);

            if(!spell.isContinuous && !caster.isCreative()){
                SpellBladeHelper.setCurrentCooldown(stack, (int)(spell.getCooldown() * modifiers.get(WizardryItems.cooldown_upgrade)));
            }

            Tier tier = getTier(stack);

            if(tier.level < Tier.MASTER.level && castingTick % WizardryUtil.CONTINUOUS_TRACKING_INTERVAL == 0){

                int progression = (int)(spell.getCost() * modifiers.get(SpellModifiers.PROGRESSION));
                SpellBladeHelper.addProgression(stack, progression);

                if(!Wizardry.settings.legacyWandLevelling){
                    Tier nextTier = tier.next();
                    int excess = SpellBladeHelper.getProgression(stack) - nextTier.getProgression();
                    if(excess >= 0 && excess < progression){
                        caster.playSound(WizardrySounds.ITEM_WAND_LEVELUP, 1.25f, 1);
                        WizardryAdvancementTriggers.wand_levelup.triggerFor(caster);
                        if(!world.isRemote)
                            caster.sendMessage(new TextComponentTranslation("item." + Wizardry.MODID + ":wand.levelup",
                                    stack.getItem().getItemStackDisplayName(stack), nextTier.getNameForTranslationFormatted()));
                    }
                }
                WizardData.get(caster).trackRecentSpell(spell);
            }
            return true;
        }
        return false;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase user, int timeLeft){
        if(user instanceof EntityPlayer){

            EntityPlayer player = (EntityPlayer)user;

            Spell spell = SpellBladeHelper.getCurrentSpell(stack);
            Tier tier = getTier(stack);

            SpellModifiers modifiers;

            if(WizardData.get(player) != null){
                modifiers = WizardData.get(player).itemCastingModifiers;
            }else{
                modifiers = WizardryUtil.calculateModifiers(stack, (EntityPlayer)user, spell, tier, element);
            }

            int castingTick = stack.getMaxItemUseDuration() - timeLeft;

            int cost = WizardryUtil.getDistributedCost((int)(spell.getCost() * modifiers.get(SpellModifiers.COST) + 0.1f), castingTick);

            if(spell.isContinuous && spell.getTier().level <= tier.level && cost <= this.getMana(stack)){

                MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Finish(SpellCastEvent.Source.WAND, spell, player, modifiers, castingTick));
                spell.finishCasting(world, player, Double.NaN, Double.NaN, Double.NaN, null, castingTick, modifiers);

                if(!player.isCreative()){
                    SpellBladeHelper.setCurrentCooldown(stack, (int)(spell.getCooldown() * modifiers.get(WizardryItems.cooldown_upgrade)));
                }
            }
        }
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand){
        if(player.isSneaking() && entity instanceof EntityPlayer && WizardData.get(player) != null){
            String string = WizardData.get(player).toggleAlly((EntityPlayer)entity) ? "item." + Wizardry.MODID + ":wand.addally"
                    : "item." + Wizardry.MODID + ":wand.removeally";
            if(!player.world.isRemote) player.sendMessage(new TextComponentTranslation(string, entity.getName()));
            return true;
        }
        return false;
    }

    public int getSpellSlotCount(ItemStack stack){
        return WizardryUtil.BASE_SPELL_SLOTS + SpellBladeHelper.getUpgradeLevel(stack, WizardryItems.attunement_upgrade);
    }

    public ItemStack applyUpgrade(EntityPlayer player, ItemStack wand, ItemStack upgrade){
        Tier thisTier = getTier(wand);

        if(upgrade.getItem() == WizardryItems.arcane_tome){
            Tier tier = Tier.values()[upgrade.getItemDamage()];

            if((player == null || player.isCreative() || Wizardry.settings.legacyWandLevelling
                    || SpellBladeHelper.getProgression(wand) >= tier.getProgression())
                    && tier == thisTier.next() && thisTier != Tier.MASTER){

                if(Wizardry.settings.legacyWandLevelling){
                    SpellBladeHelper.setProgression(wand, 0);
                }else{
                    SpellBladeHelper.setProgression(wand, SpellBladeHelper.getProgression(wand) - tier.getProgression());
                }

                if(player != null) WizardData.get(player).setTierReached(tier);

                int oldMaxMana = WizardryUtil.getMaxMana(wand);
                float oldPotency = WizardryUtil.getSpellPotency(wand);

                this.setTier(wand, tier.name());
                WizardryUtil.setMaxMana(wand, (int) (oldMaxMana * (tier.level * ModConfig.manaCapacityIncrease + 1.0)));
                WizardryUtil.setPotency(wand, (float) (oldPotency * (tier.level) * ModConfig.spellPotencyIncrease + 1.0));
                upgrade.shrink(1);

                return wand;
            }

        }else if(SpellBladeHelper.isWandUpgrade(upgrade.getItem())){


            Item specialUpgrade = upgrade.getItem();
            int maxUpgrades = thisTier.upgradeLimit;
            if(this.element == null) maxUpgrades += Constants.NON_ELEMENTAL_UPGRADE_BONUS;

            if(SpellBladeHelper.getTotalUpgrades(wand) < maxUpgrades
                    && SpellBladeHelper.getUpgradeLevel(wand, specialUpgrade) < Constants.UPGRADE_STACK_LIMIT){

                SpellBladeHelper.applyUpgrade(wand, specialUpgrade);

                if(specialUpgrade == WizardryItems.storage_upgrade){
                    int prevMaxMana = this.getManaCapacity(wand);
                    double manaModifier = 1.0 + Constants.STORAGE_INCREASE_PER_LEVEL * SpellBladeHelper.getUpgradeLevel(wand, upgrade.getItem());
                    int maxMana = (int) (prevMaxMana * manaModifier);
                    WizardryUtil.setMaxMana(wand, maxMana);
                }else if(specialUpgrade == WizardryItems.attunement_upgrade){

                    int newSlotCount = WizardryUtil.BASE_SPELL_SLOTS + SpellBladeHelper.getUpgradeLevel(wand,
                            WizardryItems.attunement_upgrade);

                    Spell[] spells = SpellBladeHelper.getSpells(wand);
                    Spell[] newSpells = new Spell[newSlotCount];

                    for(int i = 0; i < newSpells.length; i++){
                        newSpells[i] = i < spells.length && spells[i] != null ? spells[i] : Spells.none;
                    }

                    SpellBladeHelper.setSpells(wand, newSpells);

                    int[] cooldowns = SpellBladeHelper.getCooldowns(wand);
                    int[] newCooldowns = new int[newSlotCount];

                    if(cooldowns.length > 0){
                        System.arraycopy(cooldowns, 0, newCooldowns, 0, cooldowns.length);
                    }
                    SpellBladeHelper.setCooldowns(wand, newCooldowns);
                }

                upgrade.shrink(1);
                if(player != null){
                    WizardryAdvancementTriggers.special_upgrade.triggerFor(player);
                    if(SpellBladeHelper.getTotalUpgrades(wand) == Tier.MASTER.upgradeLimit){
                        WizardryAdvancementTriggers.max_out_wand.triggerFor(player);
                    }
                }
            }
        }

        return wand;
    }

    public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks){
        boolean changed = false;
        if(upgrade.getHasStack()){
            ItemStack original = centre.getStack().copy();
            centre.putStack(this.applyUpgrade(player, centre.getStack(), upgrade.getStack()));

            changed = !ItemStack.areItemStacksEqual(centre.getStack(), original);
        }
        Spell[] spells = SpellBladeHelper.getSpells(centre.getStack());
        Tier tier = getTier(centre.getStack());

        if(spells.length == 0){
            spells = new Spell[WizardryUtil.BASE_SPELL_SLOTS];
        }
        for(int i = 0; i < spells.length; i++){
            if(spellBooks[i].getStack() != ItemStack.EMPTY){
                Spell spell = Spell.byMetadata(spellBooks[i].getStack().getItemDamage());
                if(!(spell.getTier().level > tier.level) && spells[i] != spell && spell.isEnabled(SpellProperties.Context.WANDS)){
                    if (Wizardry.settings.preventBindingSameSpellTwiceToWands && Arrays.stream(spells).anyMatch(s -> s == spell)) {
                        continue;
                    }
                    spells[i] = spell;
                    changed = true;
                    if (Wizardry.settings.singleUseSpellBooks) {
                        spellBooks[i].getStack().shrink(1);
                    }
                }
            }
        }
        SpellBladeHelper.setSpells(centre.getStack(), spells);
        if (SpellBladeHelper.rechargeManaOnApplyButtonPressed(centre, crystals)) {
            changed = true;
        }
        return changed;
    }

    public void onClearButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks){
        ItemStack stack = centre.getStack();
        if (stack.getTagCompound() != null && stack.hasTagCompound() && stack.getTagCompound().getCompoundTag(SpellBladeHelper.WIZARDRY_DATA).hasKey(SpellBladeHelper.SPELL_ARRAY_KEY)) {
            NBTTagCompound nbt = stack.getTagCompound();
            int[] spells = nbt.getCompoundTag(SpellBladeHelper.WIZARDRY_DATA).getIntArray(SpellBladeHelper.SPELL_ARRAY_KEY);
            int expectedSlotCount = WizardryUtil.BASE_SPELL_SLOTS + SpellBladeHelper.getUpgradeLevel(stack,
                    WizardryItems.attunement_upgrade);
            if (spells.length < expectedSlotCount) {
                spells = new int[expectedSlotCount];
            }
            Arrays.fill(spells, 0);
            nbt.getCompoundTag(SpellBladeHelper.WIZARDRY_DATA).setIntArray(SpellBladeHelper.SPELL_ARRAY_KEY, spells);
            stack.setTagCompound(nbt);
        }
    }

    public IManaStoringItem getManaStoringItem() {
        return manaStoringItem;
    }
}
