package com.xifeng.tinkersidea.client.book;

import com.xifeng.tinkersidea.modifiers.registry.ModifierRegister;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.BookRepository;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.sectiontransformer.SectionTransformer;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class BookTransformerModifiers extends SectionTransformer {
    private final BookRepository source;
    public boolean isArmor;
    public Set<Modifier> traits;

    public BookTransformerModifiers(BookRepository source, boolean isArmor, Set<Modifier> traits) {
        super("modifiers");
        this.source = source;
        this.isArmor = isArmor;
        this.traits = traits;
    }

    @Override
    public void transform(BookData book, SectionData section) {
        ContentListing listing = (ContentListing)section.pages.get(0).content;
        for (Modifier mod : traits) {
            PageData page = new PageData();
            page.source = source;
            page.parent = section;
            page.type = isArmor ? "armormodifier" : "modifier";
            page.data = "modifiers/" + mod.identifier + ".json";
            section.pages.add(page);
            page.load();
            listing.addEntry(mod.getLocalizedName(), page);
        }
    }
}
