package de.janschuri.lunaticlib.inventorygui.interfaces.list;

import de.janschuri.lunaticlib.inventorygui.buttons.InventoryButton;
import de.janschuri.lunaticlib.inventorygui.interfaces.InventoryHandler;
import org.bukkit.entity.Player;

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
