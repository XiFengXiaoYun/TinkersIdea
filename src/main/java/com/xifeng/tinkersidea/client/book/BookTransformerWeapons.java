package com.xifeng.tinkersidea.client.book;

import com.xifeng.tinkersidea.Registry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.BookRepository;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.content.ContentTool;
import slimeknights.tconstruct.library.book.sectiontransformer.SectionTransformer;
import slimeknights.tconstruct.library.tools.ToolCore;

@SideOnly(Side.CLIENT)
public class BookTransformerWeapons extends SectionTransformer {
    private final BookRepository bookRepository;

    public BookTransformerWeapons(BookRepository source) {
        super("tools");
        this.bookRepository = source;
    }

    @Override
    public void transform(BookData bookData, SectionData sectionData) {
        ContentListing listing = (ContentListing)sectionData.pages.get(0).content;
        for(ToolCore tool : Registry.getTools()) {
            PageData page = new PageData();
            page.source = bookRepository;
            page.parent = sectionData;
            page.type = ContentTool.ID;
            page.data = "tools/" + tool.getIdentifier() + ".json";
            sectionData.pages.add(page);
            page.load();
            listing.addEntry(tool.getLocalizedName(), page);
        }
    }
}
