package com.xifeng.tinkersidea.client.book;

import com.xifeng.tinkersidea.modifiers.ModifierRegister;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.BookRepository;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.sectiontransformer.SectionTransformer;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

@SideOnly(Side.CLIENT)
public class BookTransformerModifiers extends SectionTransformer {
    private final BookRepository source;

    public BookTransformerModifiers(BookRepository source) {
        super("modifiers");
        this.source = source;
    }

    @Override
    public void transform(BookData book, SectionData section) {
        ContentListing listing = (ContentListing)section.pages.get(0).content;
        for (ModifierTrait mod : ModifierRegister.modifierTraits) {
            PageData page = new PageData();
            page.source = source;
            page.parent = section;
            page.type = "modifier";
            page.data = "modifiers/" + mod.identifier + ".json";
            section.pages.add(page);
            page.load();
            listing.addEntry(mod.getLocalizedName(), page);
        }
    }
}
