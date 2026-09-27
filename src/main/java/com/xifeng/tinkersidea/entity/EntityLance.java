package com.xifeng.tinkersidea.entity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

public class EntityLance extends EntityProjectileBase {
    public int piercingEntity = 0;

    public int maxPiercingEntity = 1;

    public double decay = 0.25;

    public EntityLance(World world) {
        super(world);
    }

    public EntityLance(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityLance(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
        super(world, player, speed, inaccuracy, 1.0F, stack, launchingStack);
    }

    @MethodsReturnNonnullByDefault
    @Override
    protected ItemStack getArrowStack() {
        return this.tinkerProjectile.getItemStack();
    }

    @Override
    public double getStuckDepth() {
        return 0.2f;
    }

    @Override
    public void onHitBlock(RayTraceResult raytraceResult) {
        super.onHitBlock(raytraceResult);
    }

    @Override
    protected void onEntityHit(Entity entityHit) {
        if(this.piercingEntity < this.maxPiercingEntity) {
            this.piercingEntity++;
        } else {
            this.setDead();
        }
    }
    @Override
    public void onHitEntity(RayTraceResult raytraceResult) {
        super.onHitEntity(raytraceResult);
    }

}
