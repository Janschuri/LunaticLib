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

    private static final Map<Integer, Consumer<Material>> consumerMap = new HashMap<>();
    private static final Map<Integer, String> searchMap = new HashMap<>();
    private static final Map<Integer, Integer> pageMap = new HashMap<>();

    public SelectBlockGUI() {
        super();
    }

    public SelectBlockGUI(int id) {
        super(id);
    }

    public SelectBlockGUI consumer(Consumer<Material> consumer) {
        consumerMap.put(getId(), consumer);
        return this;
    }

    public Consumer<Material> getConsumer() {
        return consumerMap.get(getId());
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
        pageMap.putIfAbsent(getId(), 0);
        return pageMap.get(getId());
    }

    @Override
    public void setPage(int page) {
        pageMap.put(getId(), page);
    }

    @Override
    public Predicate<Material> getSearchFilter(Player player) {
        return block -> block.name().toLowerCase().contains(getSearch().toLowerCase());
    }

    @Override
    public String getSearch() {
        return searchMap.getOrDefault(getId(), "");
    }

    @Override
    public void setSearch(String search) {
        searchMap.put(getId(), search);
    }
}
