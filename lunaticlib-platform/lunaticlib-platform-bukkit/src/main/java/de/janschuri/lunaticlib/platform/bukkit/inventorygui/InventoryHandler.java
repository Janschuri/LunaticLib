package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface InventoryHandler {

    void onClick(InventoryClickEvent event);

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

    void onDrag(InventoryDragEvent event);

    void onPlayerInvClick(InventoryClickEvent event);
    void onPlayerInvDrag(InventoryDragEvent event);
    int getSize();
}
