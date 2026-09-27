package com.xifeng.tinkersidea.leveling.event;

import c4.conarm.lib.armor.ArmorCore;
import c4.conarm.lib.tinkering.ArmorBuilder;
import c4.conarm.lib.tinkering.TinkersArmor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xifeng.tinkersidea.TinkersIdea;
import com.xifeng.tinkersidea.config.ModConfig;
import com.xifeng.tinkersidea.leveling.LevelMode;
import com.xifeng.tinkersidea.leveling.LevelingRule;
import com.xifeng.tinkersidea.leveling.StatsModifier;
import com.xifeng.tinkersidea.leveling.modifier.LevelNBT;
import com.xifeng.tinkersidea.leveling.modifier.ModifierArmorLeveling;
import com.xifeng.tinkersidea.leveling.modifier.ModifierToolLeveling;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.capability.projectile.TinkerProjectileHandler;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.DualToolHarvestUtils;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ranged.BowCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.ArrayList;
import java.util.List;

public final class LevelUpEventHandler {
    public static Multimap<Integer, StatsModifier> levelingRules = ArrayListMultimap.create();
    public static Multimap<Integer, String> toolGroups = ArrayListMultimap.create();
    public static String[] groups = ModConfig.Leveling.toolGroups;
    public static String[] rules = ModConfig.Leveling.rules;
    //this is what we actually need
    public static List<LevelingRule> ruleList = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if(!(event.getItemStack().getItem() instanceof TinkerToolCore) && !(event.getItemStack().getItem() instanceof ArmorCore)) return;
        ItemStack stack = event.getItemStack();
        if(ToolHelper.isBroken(stack)) return;
        boolean isArmor = stack.getItem() instanceof ArmorCore;
        boolean hasLeveling = hasLeveling(stack, isArmor);
        if(!hasLeveling) return;
        LevelNBT nbt = LevelNBT.get(stack, isArmor);
        String expText = I18n.format("tooltips.level.exp",
                TextFormatting.RESET + String.valueOf(nbt.exp),
                nbt.maxExp
        );
        String levelName = I18n.format("tooltips.level." + (nbt.getLevel()));
        String levelText = I18n.format("tooltips.level.level", TextFormatting.RESET + levelName);
        event.getToolTip().add(1, expText);
        event.getToolTip().add(2, levelText);
    }

    private static boolean hasLeveling(ItemStack stack, boolean isArmor) {
        NBTTagCompound nbt = stack.getTagCompound();
        return isArmor ? TinkerUtil.hasModifier(nbt, ModifierArmorLeveling.ID) : TinkerUtil.hasModifier(nbt, ModifierToolLeveling.ID);
    }

    @SubscribeEvent
    public static void onToolBuild(TinkerToolEvent.OnItemBuilding event) {
        if(!TinkerUtil.hasModifier(event.tag, ModifierToolLeveling.ID)) {
            ModifierToolLeveling.instance.apply(event.tag);
            NBTTagList tagList = TagUtil.getModifiersTagList(event.tag);
            int index = TinkerUtil.getIndexInCompoundList(tagList, ModifierToolLeveling.ID);
            NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);
            if(!modifierTag.hasKey("maxExp")) {
                modifierTag.setInteger("maxExp", ModConfig.Leveling.baseMaxExp);
            }
        }
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        if(event.getEntity().world.isRemote) return;
        if(!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        //先不管副手攻击的情况
        //TODO: 针对副手攻击做额外的兼容处理
        if(!(player.getHeldItemMainhand().getItem() instanceof TinkerToolCore)) return;
        EntityLivingBase living = event.getEntityLiving();
        float health = living.getMaxHealth();
        int xp = Math.min((int) (health * ModConfig.Leveling.ratio), ModConfig.Leveling.maxExpGet);
        if(!event.getSource().isProjectile()) {
            ItemStack stack = player.getHeldItemMainhand();
            if(ToolHelper.isBroken(stack)) return;
            handleExp(stack, xp, player, false);
        } else {
            if(event.getSource().getImmediateSource() instanceof EntityProjectileBase) {
                EntityProjectileBase projectileBase = (EntityProjectileBase) event.getSource().getImmediateSource();
                TinkerProjectileHandler handler = projectileBase.tinkerProjectile;
                ItemStack launcher = handler.getLaunchingStack();
                ItemStack arrow = ((BowCore) launcher.getItem()).findAmmo(launcher, player);
                handleExp(launcher, xp/2, player, false);
                if(launcher != arrow) {
                    handleExp(arrow, xp, player, false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockDrop(BlockEvent.HarvestDropsEvent event) {
        if(!(event.getHarvester() instanceof EntityPlayer) || event.getHarvester() == null) return;
        EntityPlayer player = event.getHarvester();
        if(player.world.isRemote) return;
        ItemStack tool = DualToolHarvestUtils.getItemstackToUse(player, event.getState());
        if(!(tool.getItem() instanceof TinkerToolCore)) return;
        Block block = event.getState().getBlock();
        int exp = Math.min((int) (block.blockHardness * block.getHarvestLevel(event.getState())), ModConfig.Leveling.maxExpBlock);
        if(exp <= 0) return;
        handleExp(tool, exp, player, false);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHurt(LivingHurtEvent event) {
        if(event.getEntityLiving().world.isRemote) return;
        if(!(event.getEntityLiving() instanceof EntityPlayer) || event.getSource().isUnblockable) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        int exp = Math.min((int) (event.getAmount() * ModConfig.Leveling.damageToExp), ModConfig.Leveling.maxExpArmor);
        if(exp <= 0) return;
        for(ItemStack stack : player.getArmorInventoryList()) {
            if(!stack.isEmpty() || stack.getItem() instanceof ArmorCore) {
                handleExp(stack, exp, player, true);
            }
        }
    }


    private static void handleExp(ItemStack stack, int exp, EntityPlayer player, boolean isArmor) {

        NBTTagList tagList = TagUtil.getModifiersTagList(stack);
        String id = isArmor ? ModifierArmorLeveling.ID : ModifierToolLeveling.ID;
        int index = TinkerUtil.getIndexInCompoundList(tagList, id);
        NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);

        LevelNBT nbt = LevelNBT.get(stack, isArmor);
        int oldLevel = nbt.getLevel();
        boolean levelUp = nbt.add(exp, LevelMode.valueOf(ModConfig.Leveling.mode));
        nbt.write(modifierTag);
        TagUtil.setModifiersTagList(stack, tagList);
        if(levelUp) {
            int level = nbt.getLevel();
            int delta = level - oldLevel;
            for(int i = 0; i < delta; i ++) {
                applyStatsBonus(stack, nbt);
            }
            player.playSound(SoundEvents.BLOCK_ANVIL_USE, 1f, 1f);
            String levelUpText = I18n.format("message.levelup", stack.getDisplayName(), nbt.getLevel());
            player.sendStatusMessage(new TextComponentString(levelUpText), false);
            try {
                NBTTagCompound rootTag = TagUtil.getTagSafe(stack);
                if(!isArmor) {
                    ToolBuilder.rebuildTool(rootTag, (TinkersItem) stack.getItem());
                } else {
                    ArmorBuilder.rebuildArmor(rootTag, (TinkersArmor) stack.getItem());
                }
                stack.setTagCompound(rootTag);
            } catch (TinkerGuiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void applyStatsBonus(ItemStack stack, LevelNBT nbt) {
        if(ruleList.isEmpty()) return;
        if(stack.isEmpty()) return;
        ResourceLocation rl = stack.getItem().getRegistryName();
        if(rl == null) return;
        LevelingRule rule = null;
        TinkersIdea.logger.info("Start to apply stats bonus");
        for(LevelingRule r : ruleList) {
            if(r.has(rl.toString())) {
                rule = r;
                break;
            }
        }
        if(rule == null) return;
        calcStatsBonus(nbt, rule);
        TinkersIdea.logger.info("Apply Stats Bonus finished!");
    }

    private static void calcStatsBonus(LevelNBT nbt, LevelingRule rule) {
        int level = nbt.level - 1;
        if(level <= 0) return;
        List<StatsModifier> applyStats = rule.matchModifier(level);
        if(applyStats.isEmpty()) return;
        for(StatsModifier mod : applyStats) {
            int type = mod.type;
            //int
            if(type == 1) {
                nbt.addIntStats(mod.stats, (int) mod.base);
            //float
            } else if (type == 2) {
                nbt.addFloatStats(mod.stats, (float) mod.base);
            } else {
                TinkersIdea.logger.error("Type Error! Must be 1 or 2");
            }
        }
    }

    public static void initConfig() {
        if(ModConfig.Leveling.loadFromJson) {
            return;
        }
        TinkersIdea.logger.info("Start init config!");
        initToolGroups();
        initRules();
        for(int i = 0; i < toolGroups.size(); i++) {
            List<String> l1 = new ArrayList<>(toolGroups.get(i));
            List<StatsModifier> l2 = new ArrayList<>(levelingRules.get(i));
            LevelingRule rule = new LevelingRule(l1, l2);
            ruleList.add(rule);
        }
    }

    private static void initToolGroups() {
        TinkersIdea.logger.info("Start init tool groups!");
        for(int i = 0; i < groups.length; i++) {
            String[] elements = groups[i].split(",");
            TinkersIdea.logger.info(elements[0]);
            for(String name : elements) {
                Item item = Item.getByNameOrId(name);
                if(item != null) {
                    TinkersIdea.logger.info("Add tool {} to group", (new ItemStack(item)).getDisplayName());
                    toolGroups.put(i, name);
                } else {
                    TinkersIdea.logger.error("No such item {}", name);
                }
            }
        }
    }

    private static void initRules() {
        int type;
        int threshold;
        double amount;
        TinkersIdea.logger.info("Start init tool leveling rules!");
        for(int i = 0; i < rules.length; i++) {
            String[] e = rules[i].split(",");
            TinkersIdea.logger.info(e[0]);
            for(String stats : e) {
                String[] s = stats.split(":");
                try {
                    type = Integer.parseInt(s[1]);
                    threshold = Integer.parseInt(s[2]);
                    amount = Double.parseDouble(s[3]);
                } catch (NumberFormatException ex) {
                    throw new RuntimeException(ex);
                }
                StatsModifier modifier = new StatsModifier(s[0], amount, threshold);
                modifier.type = type;
                levelingRules.put(i, modifier);
            }
        }
    }
}
