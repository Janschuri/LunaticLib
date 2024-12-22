package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class GUIManager {

    private static final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();


    public static void openGUI(InventoryGUI gui, Player player) {
        Inventory inventory = gui.getInventory();
        registerHandledInventory(inventory, gui);

        if (player.getOpenInventory().getTopInventory().equals(inventory)) {
            gui.init(player);
            return;
        }

                Bukkit.getScheduler().runTask(BukkitLunaticLib.getInstance(), () -> {
                    player.openInventory(inventory);
                });

    }

    public static void registerHandledInventory(Inventory inventory, InventoryHandler handler) {
        activeInventories.put(inventory, handler);
    }

    public static void unregisterInventory(Inventory inventory) {
        List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
        for (HumanEntity viewer : viewers) {
            viewer.closeInventory();
        }
        inventory.clear();
        activeInventories.remove(inventory);
    }

    public static void handleClick(InventoryClickEvent event) {
        InventoryHandler handler = activeInventories.get(event.getInventory());
        if (handler != null) {
            Inventory playerInv = event.getWhoClicked().getInventory();

            if (event.getClickedInventory() != playerInv) {
                handler.onClick(event);
            } else {
                handler.onPlayerInvClick(event);
            }
        }
    }

    public static void handleOpen(InventoryOpenEvent event) {
        InventoryHandler handler = activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onOpen(event);
        }
    }

    public static void handleDrag(InventoryDragEvent event) {
        InventoryHandler handler = activeInventories.get(event.getInventory());


        if (handler != null) {

            int guiSize = event.getView().getTopInventory().getSize();

            Logger.infoLog("GUI size: " + guiSize);

            Set<Integer> slots = event.getInventorySlots();

            for (int slot : slots) {
                Logger.debugLog("Slot: " + slot);
                Logger.debugLog("GUI size: " + guiSize);
                if (slot < guiSize) {
                    handler.onDrag(event);
//                    return;
                }
            }

//            handler.onPlayerInvDrag(event);
        }
    }

    public static void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHandler handler = activeInventories.get(inventory);
        if (handler != null) {
            handler.onClose(event);
            activeInventories.remove(inventory);
            inventory.clear();
        }
    }

    public static void closeAll() {
        Map<Inventory, InventoryHandler> activeInventoriesCopy = new HashMap<>(activeInventories);
        for (Inventory inventory : activeInventoriesCopy.keySet()) {
            List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
            for (HumanEntity viewer : viewers) {
                viewer.closeInventory();
            }
        }
    }

}
