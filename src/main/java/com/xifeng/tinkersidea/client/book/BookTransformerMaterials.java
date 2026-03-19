package com.xifeng.tinkersidea.client.book;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.data.element.ImageData;
import slimeknights.mantle.client.gui.book.element.ElementImage;
import slimeknights.mantle.client.gui.book.element.ElementItem;
import slimeknights.mantle.client.gui.book.element.SizedBookElement;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.content.ContentPageIconList;
import slimeknights.tconstruct.library.book.content.ContentSingleStatMultMaterial;
import slimeknights.tconstruct.library.book.sectiontransformer.SectionTransformer;
import slimeknights.tconstruct.library.materials.Material;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

//copy from tconstruct
@SideOnly(Side.CLIENT)
public class BookTransformerMaterials extends SectionTransformer {

    private static final String materialType = "magic_focus";

    public BookTransformerMaterials() {
        super("magicmaterials");
    }

    @Override
    public void transform(BookData bookData, SectionData sectionData) {
        ContentListing listing = new ContentListing();
        listing.title = bookData.translate(this.sectionName);
        this.addPage(sectionData, this.sectionName, "", listing);
        if (Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION)) {
            int pageIndex = sectionData.pages.size();
            this.generateContent(sectionData);
            if(pageIndex < sectionData.pages.size()) {
                listing.addEntry(this.getStatName(), sectionData.pages.get(pageIndex));
            }
        }
    }

    private String getStatName() {
        return Material.UNKNOWN.getStats(materialType).getLocalizedName();
    }

    private void generateContent(SectionData sectionData) {
        List<Material> materialList = TinkerRegistry.getAllMaterials().stream().filter((m) -> !m.isHidden()).filter(Material::hasItems).filter((materialx) -> materialx.hasStats(materialType)).collect(Collectors.toList());
        if (materialList.isEmpty()) {
            System.out.println("No material found!!!");
            return;
        }
        List<ContentPageIconList> contentPages = ContentPageIconList.getPagesNeededForItemCount(materialList.size(), sectionData, this.getStatName());
        ListIterator<ContentPageIconList> iter = contentPages.listIterator();
        ContentPageIconList currentOverview = iter.next();
        contentPages.forEach((p) -> p.maxScale = 1.0F);

        for(List<Material> materials : Lists.partition(materialList, 3)) {
            ContentSingleStatMultMaterial content = new ContentSingleStatMultMaterial(materials, materialType);
            String id = materialType + "_" + materials.stream().map(Material::getIdentifier).collect(Collectors.joining("_"));
            PageData page = this.addPage(sectionData, id, "single_stat_material", content);

            for(Material material : materials) {
                System.out.println(material.getIdentifier());
                SizedBookElement icon;
                if (material.getRepresentativeItem() != null) {
                    icon = new ElementItem(0, 0, 1.0F, material.getRepresentativeItem());
                } else {
                    icon = new ElementImage(ImageData.MISSING);
                }

                if (!currentOverview.addLink(icon, material.getLocalizedNameColored(), page)) {
                    currentOverview = iter.next();
                }
            }
        }
    }
}
