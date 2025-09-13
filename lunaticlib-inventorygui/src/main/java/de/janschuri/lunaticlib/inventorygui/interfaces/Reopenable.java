package de.janschuri.lunaticlib.inventorygui.interfaces;

import org.bukkit.NamespacedKey;

public interface Reopenable extends InventoryHandler {
    NamespacedKey uniqueKey();
    default boolean isPlayerUnique() {
        return true;
    }
}
