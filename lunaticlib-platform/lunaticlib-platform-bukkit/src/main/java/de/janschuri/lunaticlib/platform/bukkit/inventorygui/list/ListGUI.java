package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import com.mysql.cj.log.Log;
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

    public ListGUI(int id) {
        super(id);
    }

    @Override
    public void init(Player player) {
        int pageSize = getPageSize();

        if (this instanceof SearchableList<?>) {
            ((SearchableList<T>) this).addSearchButtons(player);
            ((SearchableList<T>) this).addSearchFilter(player);
        }

        loadProcessedItems(player);

        List<T> items = getProcessedItems();

        if (this instanceof PaginatedList<?>) {
            items = (List<T>) ((PaginatedList<?>) this).paginate(items);
            ((PaginatedList<?>) this).addPaginateButtons(player);
        }


        for (int i = 0; i < pageSize; i++) {
            if (i >= items.size()) {
                addButton(i+9, emptyListItemButton(i+9));
                continue;
            }

            T item = items.get(i);
            InventoryButton button = listItemButton(item);
            addButton(i+9, button);
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
