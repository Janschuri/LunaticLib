package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface InventoryHandler {

    int getId();

    void init(Player player);

    void addButton(int slot, InventoryButton button);

    void addButton(PlayerInvButton button);

    void onClick(InventoryClickEvent event);

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

    void onDrag(InventoryDragEvent event);

    void onPlayerInvClick(InventoryClickEvent event);
    void onPlayerInvDrag(InventoryDragEvent event);

    void reloadGui(Player player);

    boolean processingClickEvent();
}
