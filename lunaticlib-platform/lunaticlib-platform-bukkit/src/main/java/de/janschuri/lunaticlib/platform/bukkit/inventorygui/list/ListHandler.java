package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface ListHandler<T> extends InventoryHandler {


    InventoryButton listItemButton(T item);

    List<T> getItems();

    void addFilter(String name, Predicate<T> filter);

    Collection<Predicate<T>> getFilters();

    int getPageSize();

    int getStartIndex();

    InventoryButton emptyListItemButton(int slot);

    List<T> getProcessedItems();

    void loadProcessedItems(Player player);
}
