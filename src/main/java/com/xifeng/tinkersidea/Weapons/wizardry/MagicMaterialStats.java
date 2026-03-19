package com.xifeng.tinkersidea.Weapons.wizardry;

import com.google.common.collect.ImmutableList;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.AbstractMaterialStats;

import java.util.List;

public class MagicMaterialStats extends AbstractMaterialStats {

    public final float spellPotency;
    public final int maxMana;
    private static final String LOC_SpellPotency = "stat.spellblade.spellPotency.name";
    private static final String LOC_MaxMana = "stat.spellblade.maxMana.name";
    private static final String LOC_SpellPotencyDesc = "stat.spellblade.spellPotency.desc";
    private static final String LOC_MaxManaDesc = "stat.spellblade.maxMana.desc";
    private static final String Color_SpellPotency = CustomFontColor.encodeColor(133, 234, 249);
    private static final String Color_MaxMana = CustomFontColor.encodeColor(6, 146, 216);

    public MagicMaterialStats(float potency, int maxMana) {
        super("magic_focus");
        this.spellPotency = potency;
        this.maxMana = maxMana;
    }

    private static String formatSpellPotency(float potency) {
        return formatNumber(LOC_SpellPotency, Color_SpellPotency, potency);
    }

    private static String formatMaxMana(int maxMana) {
        return formatNumber(LOC_MaxMana, Color_MaxMana, maxMana);
    }

    private static String descSpellPotency() {
        return Util.translate(LOC_SpellPotencyDesc);
    }

    private static String descMaxMana() {
        return Util.translate(LOC_MaxManaDesc);
    }

    @Override
    public List<String> getLocalizedInfo() {
        return ImmutableList.of(formatSpellPotency(spellPotency), formatMaxMana(maxMana));
    }

    @Override
    public List<String> getLocalizedDesc() {
        return ImmutableList.of(descSpellPotency(), descMaxMana());
    }
}
