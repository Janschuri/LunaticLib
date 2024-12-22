package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.SearchableList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectBlockGUI extends ListGUI<Material> implements PaginatedList<Material>, SearchableList<Material> {

    private Consumer<Material> consumer;
    private String search = "";
    private Integer page = 0;

    public SelectBlockGUI() {
        super();
    }

    public SelectBlockGUI consumer(Consumer<Material> consumer) {
        this.consumer = consumer;
        return this;
    }

    public Consumer<Material> getConsumer() {
        return consumer;
    }

    @Override
    public InventoryButton listItemButton(Material block) {
        ItemStack itemStack = new ItemStack(block);

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(block);
                });
    }

    @Override
    public List<Material> getItems() {
        return Arrays.stream(Material.values())
                .filter(Material::isBlock)
                .filter(Material::isItem)
                .collect(Collectors.toList());
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Predicate<Material> getSearchFilter(Player player) {
        return block -> block.name().toLowerCase().contains(getSearch().toLowerCase());
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public void setSearch(String search) {
        this.search = search;
    }
}
