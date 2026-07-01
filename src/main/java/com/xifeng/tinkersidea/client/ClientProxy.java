package com.xifeng.tinkersidea.client;

import c4.conarm.lib.book.ArmoryBook;
import com.xifeng.tinkersidea.common.CommonProxy;
import com.xifeng.tinkersidea.Weapons.WeaponAll;
import net.minecraft.item.Item;
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
            info.addSlotPosition(33 - 10 - 14 - 1, 42 + 10 + 10 + 1); // handle
            info.addSlotPosition(33 - 8 + 8, 42 - 10 + 2); // head
            info.addSlotPosition(33 + 14 + 6, 42 - 8 - 8); // head 2
            info.addSlotPosition( 11, 42); //guard
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
}
