package com.xifeng.tinkersidea.parts;

import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;

public class MagicMaterialType {
    public static final String MAGICFOCUS = "magic_focus";
    public static PartMaterialType magicFocus(IToolPart part) {
        return new PartMaterialType(part, MAGICFOCUS);
    }
}
