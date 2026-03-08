package com.xifeng.tinkersidea.Weapons.wizardry;

import com.google.common.collect.Multimap;
import com.xifeng.tinkersidea.Attribute;
import com.xifeng.tinkersidea.Weapons.WeaponAll;
import com.xifeng.tinkersidea.parts.MagicMaterialType;
import com.xifeng.tinkersidea.util.WizardryUtil;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.item.IWorkbenchItem;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

public class SpellBlade extends SwordCore implements IWorkbenchItem, ISpellCastingItem, IManaStoringItem {

    private final WizardryCore wizardryCore;
    private static final UUID uuid = UUID.fromString("46b3ce0a-c968-dcd3-1d84-7045ff7d6580");

    public SpellBlade() {
        super(PartMaterialType.handle(TinkerTools.toolRod),
                PartMaterialType.head(TinkerTools.knifeBlade),
                PartMaterialType.extra(TinkerTools.crossGuard),
                MagicMaterialType.magicFocus(WeaponAll.magicFocus));
        this.addCategory(Category.WEAPON, SpecialCategory.Wizardry);
        setTranslationKey("spellblade").setRegistryName("spellblade");
        this.wizardryCore = new WizardryCore(this, Element.MAGIC, this);
    }

    @Override
    protected MagicNBT buildTagData(List<Material> list) {
        if(list.isEmpty()) {
            throw new RuntimeException("The list is empty! Report this issue to the mod author!");
        }
        HandleMaterialStats handle = list.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
        HeadMaterialStats head = list.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = list.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
        MagicMaterialStats magic = list.get(3).getStatsOrUnknown(MagicMaterialType.MAGICFOCUS);

        MagicNBT data = new MagicNBT();
        data.head(head);
        data.extra(binding);
        data.handle(handle);
        data.magic(magic);

        data.attack += 1;
        data.durability *= 0.8;
        return data;
    }

    @Override
    public float damagePotential() {
        return 0.5f;
    }

    @Override
    public double attackSpeed() {
        return 1.8;
    }

    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit =  super.dealDamage(stack, player, entity, damage);
        if(player instanceof EntityPlayer && entity instanceof EntityLivingBase) {
            MagicNBT nbt = MagicNBT.from(stack);
            double baseDamage = nbt.attack;
            float potency = WizardryUtil.getSpellPotency(stack) / 100.0f;
            float bonusMagicDmg = (float) (baseDamage * potency);
            entity.hurtResistantTime = 0;
            ((EntityLivingBase) entity).lastDamage = 0;
            entity.attackEntityFrom(DamageSource.MAGIC, bonusMagicDmg);
        }
        return hit;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        double amount = WizardryUtil.getSpellPotency(stack)/100.0;

        if (slot == EntityEquipmentSlot.MAINHAND && !ToolHelper.isBroken(stack)) {
            multimap.put(Attribute.MAGIC.getName(), new AttributeModifier(uuid, "Magic_Potency", amount, 0));
        }

        TinkerUtil.getTraitsOrdered(stack).forEach((trait) -> trait.getAttributeModifiers(slot, stack, multimap));
        return multimap;
    }

//Magic part!
    @Nonnull
    public Spell getCurrentSpell(ItemStack stack){
        return wizardryCore.getCurrentSpell(stack);
    }

    @MethodsReturnNonnullByDefault
    @Override
    public Spell getNextSpell(ItemStack stack){
        return wizardryCore.getNextSpell(stack);
    }

    @MethodsReturnNonnullByDefault
    @Override
    public Spell getPreviousSpell(ItemStack stack){
        return wizardryCore.getPreviousSpell(stack);
    }

    @Override
    public Spell[] getSpells(ItemStack stack){
        return wizardryCore.getSpells(stack);
    }

    @Override
    public void selectNextSpell(ItemStack stack){
        wizardryCore.selectNextSpell(stack);
    }

    @Override
    public void selectPreviousSpell(ItemStack stack){
        wizardryCore.selectPreviousSpell(stack);
    }

    @Override
    public boolean selectSpell(ItemStack stack, int index){
        return wizardryCore.selectSpell(stack, index);
    }

    @Override
    public int getCurrentCooldown(ItemStack stack){
        return wizardryCore.getCurrentCooldown(stack);
    }

    @Override
    public int getCurrentMaxCooldown(ItemStack stack){
        return wizardryCore.getCurrentMaxCooldown(stack);
    }

    @Override
    public boolean showSpellHUD(EntityPlayer player, ItemStack stack){
        return true;
    }

    @Override
    public boolean showTooltip(ItemStack stack){
        return true;
    }

    @Override
    public void setMana(ItemStack stack, int mana){
        WizardryUtil.setMana(stack, mana);
    }

    @Override
    public int getMana(ItemStack stack){
        return WizardryUtil.getMana(stack);
    }

    @Override
    public int getManaCapacity(ItemStack stack){
        return WizardryUtil.getMaxMana(stack);
    }

    @Override
    public void onUpdate(@ParametersAreNonnullByDefault ItemStack stack, @ParametersAreNonnullByDefault World world, @ParametersAreNonnullByDefault Entity entity, int slot, boolean isHeldInMainhand){
        super.onUpdate(stack, world, entity, slot, isHeldInMainhand);
        wizardryCore.onUpdate(stack, world, entity, slot, isHeldInMainhand);
    }

    @Override
    public boolean canContinueUsing(@ParametersAreNonnullByDefault ItemStack oldStack, @ParametersAreNonnullByDefault ItemStack newStack){
        return wizardryCore.canContinueUsing(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @ParametersAreNonnullByDefault ItemStack newStack, boolean slotChanged){
        return wizardryCore.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @MethodsReturnNonnullByDefault
    @Override
    public EnumAction getItemUseAction(@ParametersAreNonnullByDefault ItemStack itemstack){
        return wizardryCore.getItemUseAction(itemstack);
    }

    @Override
    public int getMaxItemUseDuration(@ParametersAreNonnullByDefault ItemStack stack){
        return 72000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@ParametersAreNonnullByDefault ItemStack stack, World world, @ParametersAreNonnullByDefault List<String> text, @ParametersAreNonnullByDefault net.minecraft.client.util.ITooltipFlag advanced){
        super.addInformation(stack, world, text, advanced);
        if(!Util.isCtrlKeyDown() && !Util.isShiftKeyDown()) {
            wizardryCore.addInformation(stack, world, text, advanced);
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(@ParametersAreNonnullByDefault ItemStack stack){
        return DrawingUtils.mix(0xff8bfe, 0x8e2ee4, (float)getDurabilityForDisplay(stack));
    }

    @MethodsReturnNonnullByDefault
    @Override
    public ActionResult<ItemStack> onItemRightClick(@ParametersAreNonnullByDefault World world, @ParametersAreNonnullByDefault EntityPlayer player, @ParametersAreNonnullByDefault EnumHand hand){
        return wizardryCore.onItemRightClick(world, player, hand);
    }

    @Override
    public void onUsingTick(@ParametersAreNonnullByDefault ItemStack stack, @ParametersAreNonnullByDefault EntityLivingBase user, int count){
        wizardryCore.onUsingTick(stack, user, count);
    }

    @Override
    public boolean canCast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers){
        return wizardryCore.canCast(stack, spell, caster, hand, castingTick, modifiers);
    }

    @Override
    public boolean cast(ItemStack stack, Spell spell, EntityPlayer caster, EnumHand hand, int castingTick, SpellModifiers modifiers){
        return wizardryCore.cast(stack, spell, caster, hand, castingTick, modifiers);
    }

    @Override
    public void onPlayerStoppedUsing(@ParametersAreNonnullByDefault ItemStack stack, @ParametersAreNonnullByDefault World world, @ParametersAreNonnullByDefault EntityLivingBase user, int timeLeft){
        wizardryCore.onPlayerStoppedUsing(stack, world, user, timeLeft);
    }

    @Override
    public boolean itemInteractionForEntity(@ParametersAreNonnullByDefault ItemStack stack, @ParametersAreNonnullByDefault EntityPlayer player, @ParametersAreNonnullByDefault EntityLivingBase entity, @ParametersAreNonnullByDefault EnumHand hand){
        return wizardryCore.itemInteractionForEntity(stack, player, entity, hand);
    }
    // Workbench stuff

    @Override
    public int getSpellSlotCount(ItemStack stack){
        return wizardryCore.getSpellSlotCount(stack);
    }

    @Override
    public ItemStack applyUpgrade(@Nullable EntityPlayer player, ItemStack wand, ItemStack upgrade){
        return wizardryCore.applyUpgrade(player, wand, upgrade);
    }

    @Override
    public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks){
        return wizardryCore.onApplyButtonPressed(player, centre, crystals, upgrade, spellBooks);
    }

    @Override
    public void onClearButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks){
        wizardryCore.onClearButtonPressed(player, centre, crystals, upgrade, spellBooks);
    }
}
