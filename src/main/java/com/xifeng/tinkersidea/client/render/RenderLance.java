package com.xifeng.tinkersidea.client.render;

import com.xifeng.tinkersidea.entity.EntityLance;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import slimeknights.tconstruct.library.client.renderer.RenderProjectileBase;

public class RenderLance extends RenderProjectileBase<EntityLance> {

    public RenderLance(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void customRendering(EntityLance entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.customRendering(entity, x, y, z, entityYaw, partialTicks);
        GL11.glScalef(3.5F, 3.5F, 3.5F);
    }
}
