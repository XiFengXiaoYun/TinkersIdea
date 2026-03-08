package com.xifeng.tinkersidea.Weapons.wizardry;

import com.google.common.collect.ImmutableList;
import slimeknights.tconstruct.library.materials.AbstractMaterialStats;

import java.util.List;

public class MagicMaterialStats extends AbstractMaterialStats {

    public final float spellPotency;
    public final int mana;
    public final int maxMana;

    public MagicMaterialStats(float potency, int maxMana) {
        super("magic_focus");
        this.spellPotency = potency;
        this.mana = 0;
        this.maxMana = maxMana;
    }

    @Override
    public List<String> getLocalizedInfo() {
        return ImmutableList.of(String.format("法术强度: %.2f", this.spellPotency), String.format("最大魔力值: %d", this.maxMana));
    }

    @Override
    public List<String> getLocalizedDesc() {
        return ImmutableList.of("决定法术强度", "决定最大储存的魔力值");
    }
}
