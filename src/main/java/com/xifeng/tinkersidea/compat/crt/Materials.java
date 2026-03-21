package com.xifeng.tinkersidea.compat.crt;

import com.xifeng.tinkersidea.Weapons.wizardry.MagicMaterialStats;
import com.xifeng.tinkersidea.materials.MagicMaterials;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.CraftTweaker;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tinkersidea.Materials")
@ZenRegister
@ModOnly("ebwizardry")
public class Materials {

    @ZenMethod
    public static void addMagicStats(String mat, int mana, float spellPotency) {
        Material material = TinkerRegistry.getMaterial(mat);
        if(material == Material.UNKNOWN) {
            CraftTweaker.LOG.error("Material with name " + mat + " not found!");
            return;
        }
        CraftTweaker.LOG.info("Adding material " + mat);
        MagicMaterialStats stats = new MagicMaterialStats(spellPotency, mana);
        MagicMaterials.crtMaterials.put(material, stats);
        TinkerRegistry.addMaterialStats(material, stats);
    }
}
