package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;

public interface Reopenable extends InventoryHandler {
    NamespacedKey uniqueKey();
    default boolean isPlayerUnique() {
        return true;
    }
}
