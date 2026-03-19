package com.xifeng.tinkersidea.util;

import com.xifeng.tinkersidea.Weapons.wizardry.MagicNBT;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.utils.TagUtil;

public final class TITagUtil {

    public static MagicNBT getMagicStats(NBTTagCompound root) {
        return new MagicNBT(TagUtil.getToolTag(root));
    }

    public static MagicNBT getOriginalMagicStats(NBTTagCompound root) {
        return new MagicNBT(TagUtil.getTagSafe(root, "StatsOriginal"));
    }
}
