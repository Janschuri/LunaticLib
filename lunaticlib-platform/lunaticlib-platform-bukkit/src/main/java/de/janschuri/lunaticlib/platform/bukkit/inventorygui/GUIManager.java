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
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GUIManager {

    private static final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();
    private static final Map<String, Reopenable> reopenables = new HashMap<>();

    public static void reopenGUI(@NotNull Reopenable reopenable, Player player) {
        String uniqueKey = reopenable.uniqueKey().toString();

        if (reopenable.isPlayerUnique()) {
            uniqueKey += ":" + player.getUniqueId();
        }

        reopenable = reopenables.getOrDefault(uniqueKey, reopenable);

        if (reopenable == null) {
            Logger.errorLog("Reopenable GUI not found for key: " + uniqueKey);
            return;
        }

        openGUI(reopenable, player);
    }

    public static void openGUI(@NotNull InventoryHandler gui, Player player) {
        Inventory inventory = gui.getInventory();
        registerHandledInventory(inventory, gui);

        if (gui instanceof Reopenable reopenable) {
            String uniqueKey = reopenable.uniqueKey().toString();

            if (reopenable.isPlayerUnique()) {
                uniqueKey += ":" + player.getUniqueId();
            }

            reopenables.put(uniqueKey, reopenable);
        }

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
