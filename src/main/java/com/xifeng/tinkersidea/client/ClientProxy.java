package com.xifeng.tinkersidea.client;

import com.xifeng.tinkersidea.CommonProxy;
import com.xifeng.tinkersidea.Weapons.WeaponAll;
import com.xifeng.tinkersidea.client.book.BookTransformerModifiers;
import com.xifeng.tinkersidea.client.book.BookTransformerWeapons;
import net.minecraft.item.Item;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;

public class ClientProxy extends CommonProxy {
    @Override
    public void initToolGuis() {
        if(WeaponAll.greatSword != null) {
            ToolBuildGuiInfo info = new ToolBuildGuiInfo(WeaponAll.greatSword);
            info.addSlotPosition(33 - 10 - 14, 42 + 10 + 12); // handle
            info.addSlotPosition(33 - 8 + 6, 42 - 10 + 4 - 4); // head
            info.addSlotPosition(33 + 14 + 6, 42 - 10 - 2 - 4); // head 2
            info.addSlotPosition( 11, 42); //guard
            TinkerRegistryClient.addToolBuilding(info);
        }
        if(WeaponAll.spellBlade != null) {
            ToolBuildGuiInfo info = new ToolBuildGuiInfo(WeaponAll.spellBlade);
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
        TinkerBook.INSTANCE.addTransformer(new BookTransformerWeapons(new FileRepository("tconstruct:book")));
        TinkerBook.INSTANCE.addTransformer(new BookTransformerModifiers(new FileRepository("tconstruct:book")));
    }

    @Override
    public <T extends Item & IToolPart> void registerToolPartModel(T part) {
        ModelRegisterUtil.registerPartModel(part);
    }
}
