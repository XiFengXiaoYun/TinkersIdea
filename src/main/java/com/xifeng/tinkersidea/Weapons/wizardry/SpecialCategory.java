package com.xifeng.tinkersidea.Weapons.wizardry;

import slimeknights.tconstruct.library.tinkering.Category;

public class SpecialCategory extends Category {

    public static final Category Wizardry = new SpecialCategory("wizardry");
    public final String name;
    public SpecialCategory(String name) {
        super(name);
        this.name = name;
    }
}
