package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.PaginatedList;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.SearchableList;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectMobGUI extends ListGUI<EntityType> implements PaginatedList<EntityType>, SearchableList<EntityType> {

    private Consumer<EntityType> consumerMap;
    private String search = "";
    private int page = 0;

    public SelectMobGUI() {
        super();
    }

    public SelectMobGUI consumer(Consumer<EntityType> consumer) {
        consumerMap = consumer;
        return this;
    }

    public Consumer<EntityType> getConsumer() {
        return consumerMap;
    }

    @Override
    public InventoryButton listItemButton(EntityType entityType) {
        ItemStack itemStack = ItemStackUtils.getSpawnEgg(entityType);

        SpawnEggMeta meta = (SpawnEggMeta) itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(entityType.name());
            itemStack.setItemMeta(meta);
        }

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(entityType);
                });
    }

    @Override
    public List<EntityType> getItems() {
        return Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
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
    public Predicate<EntityType> getSearchFilter(Player player) {
        return entityType -> entityType.name().toLowerCase().contains(getSearch().toLowerCase());
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
