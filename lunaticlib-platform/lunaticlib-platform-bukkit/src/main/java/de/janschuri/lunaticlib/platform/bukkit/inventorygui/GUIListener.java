package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
      GUIManager.handleClick(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
      GUIManager.handleOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
      GUIManager.handleClose(event);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
      GUIManager.handleDrag(event);
    }
}
