package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GUIManager {

    private static final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    public static void openGUI(InventoryGUI gui, Player player) {
        openGUI(gui, player, true);
    }

    public static void openGUI(InventoryGUI gui, Player player, boolean openInventory) {
        registerHandledInventory(gui.getInventory(), gui);
        if (openInventory) {
            player.openInventory(gui.getInventory());
        }
    }

    public static void registerHandledInventory(Inventory inventory, InventoryHandler handler) {
        activeInventories.put(inventory, handler);
    }

    public static void unregisterInventory(Inventory inventory) {
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

            int guiSize = handler.getSize();

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
            unregisterInventory(inventory);
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
