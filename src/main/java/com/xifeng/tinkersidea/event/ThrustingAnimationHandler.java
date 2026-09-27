package com.xifeng.tinkersidea.event;

import com.xifeng.tinkersidea.Weapons.common.ThrustingLance;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThrustingAnimationHandler {
    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack mainHand = player.getHeldItemMainhand();
        if (mainHand.getItem() instanceof ThrustingLance && player.isHandActive()) {
            ModelBiped model = event.getRenderer().getMainModel();

            // 获取活跃的手
            EnumHand activeHand = player.getActiveHand();

            if (activeHand == EnumHand.MAIN_HAND) {
                // 右臂（主手）突刺姿态
                // 手臂伸直指向前方
                model.bipedRightArm.rotateAngleX = -90.0F * 0.017453292F; // -90度，水平向前
                model.bipedRightArm.rotateAngleY = 0.0F;
                model.bipedRightArm.rotateAngleZ = 0.0F;

                // 可以添加手臂向前伸的动画
                // model.bipedRightArm.rotationPointZ += 2.0F;

            } else {
                // 左臂（副手）突刺姿态
                model.bipedLeftArm.rotateAngleX = -90.0F * 0.017453292F;
                model.bipedLeftArm.rotateAngleY = 0.0F;
                model.bipedLeftArm.rotateAngleZ = 0.0F;
            }
        }
    }
}
