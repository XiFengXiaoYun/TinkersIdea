package com.xifeng.tinkersidea.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public final class DamageUtil {
    public static List<EntityLivingBase> getEntitiesInRange(Entity target, World worldIn, double range) {
        if (target == null || worldIn == null) {
            return Collections.emptyList();
        }
        List<EntityLivingBase> entityLivingBaseList = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().grow(range, 0.25, range));
        entityLivingBaseList.removeIf(entityLivingBase -> entityLivingBase == target);
        return entityLivingBaseList;
    }
}
