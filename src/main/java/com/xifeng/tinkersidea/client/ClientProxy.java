package com.xifeng.tinkersidea.client;

import c4.conarm.lib.book.ArmoryBook;
import com.xifeng.tinkersidea.common.CommonProxy;
import com.xifeng.tinkersidea.Weapons.WeaponAll;
import com.xifeng.tinkersidea.event.ThrustingAnimationHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void initToolGuis() {
        if(WeaponAll.greatSword != null) {
            ToolBuildGuiInfo info = new ToolBuildGuiInfo(WeaponAll.greatSword);
            info.addSlotPosition(12, 62);
            info.addSlotPosition(48, 26);
            info.addSlotPosition(30, 44);
            TinkerRegistryClient.addToolBuilding(info);
        }

        if(WeaponAll.lance != null) {
            ToolBuildGuiInfo info = new ToolBuildGuiInfo(WeaponAll.lance);
            info.addSlotPosition(12, 62);
            info.addSlotPosition(48, 26);
            info.addSlotPosition(30, 44);
            TinkerRegistryClient.addToolBuilding(info);
        }
    }

    @Override
    public void registerToolModel(ToolCore toolCore) {
        ModelRegisterUtil.registerToolModel(toolCore);
    }

    @Override
    public void postInit() {
        TinkerBook.INSTANCE.addRepository(new FileRepository("tinkersidea:book"));
        ArmoryBook.INSTANCE.addRepository(new FileRepository("tinkersidea:armorybook"));
    }

    @Override
    public <T extends Item & IToolPart> void registerToolPartModel(T part) {
        ModelRegisterUtil.registerPartModel(part);
    }

    @Override
    public void registerItemModel(Item item) {
        ModelRegisterUtil.registerItemModel(item);
    }

    @Override
    public void registerAnimation() {
        MinecraftForge.EVENT_BUS.register(ThrustingAnimationHandler.class);
    }
}
