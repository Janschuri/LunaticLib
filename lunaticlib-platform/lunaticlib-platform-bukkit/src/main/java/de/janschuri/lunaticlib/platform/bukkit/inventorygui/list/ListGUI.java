package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public abstract class ListGUI<T> extends InventoryGUI implements ListHandler<T> {

    private final Map<String, Predicate<T>> filters = new HashMap<>();
    private final List<T> processedItems = new ArrayList<>();

    public ListGUI() {
        super();
    }

    @Override
    public void init(Player player) {
        int pageSize = getPageSize();

        if (this instanceof SearchableList<?> searchableList) {
            searchableList.addSearchButtons(player);
            searchableList.addSearchFilter(player);
        }

        loadProcessedItems(player);

        List<T> items = getProcessedItems();

        if (this instanceof SortedList<?> sortedList) {
            items = (List<T>) sortedList.sortItems(player, items);
            sortedList.addSorterButtons(player);
        }

        if (this instanceof PaginatedList<?> paginatedList) {
            items = (List<T>) paginatedList.paginate(items);
            ((PaginatedList<?>) this).addPaginateButtons(player);
        }

        int startIndex = getStartIndex();

        Logger.debugLog("Start index: " + startIndex);

        for (int i = 0; i < pageSize; i++) {
            if (i >= items.size()) {
                addButton(i+startIndex, emptyListItemButton(i+startIndex));
                continue;
            }

            T item = items.get(i);
            InventoryButton button = listItemButton(item);
            addButton(i+startIndex, button);
        }

        super.init(player);
    }

    @Override
    public abstract InventoryButton listItemButton(T item);

    @Override
    public abstract List<T> getItems();

    @Override
    public void addFilter(String name, Predicate<T> filter) {
        filters.put(name, filter);
    }

    @Override
    public Collection<Predicate<T>> getFilters() {
        return filters.values();
    }

    @Override
    public int getPageSize() {
        return 36;
    }

    @Override
    public int getStartIndex() {
        return 9;
    }

    @Override
    public InventoryButton emptyListItemButton(int slot) {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.AIR));
    }

    @Override
    public void loadProcessedItems(Player player) {
        Logger.debugLog("Filters: " + getFilters().size());

        Predicate<T> combinedFilter = getFilters().stream()
                .reduce(Predicate::and)
                .orElse(t -> true);

        this.processedItems.clear();
        this.processedItems.addAll(
                getItems()
                .stream()
                .filter(combinedFilter)
                .toList()
        );
    }

    @Override
    public List<T> getProcessedItems() {
        return processedItems;
    }
}
