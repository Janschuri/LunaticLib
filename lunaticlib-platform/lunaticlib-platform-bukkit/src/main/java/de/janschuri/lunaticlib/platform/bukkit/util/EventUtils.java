package de.janschuri.lunaticlib.platform.bukkit.util;

import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class EventUtils {

    private static List<Event> fakeEvents = new ArrayList<>();

    private EventUtils() {
    }

    public static boolean isAllowedViewChest(Player player, Block chest) {
        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), chest, BlockFace.UP);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);
        return allowed;
    }

    public static boolean isAllowedTakeItem(Player player, Inventory inventory) {

        Logger.debugLog(String.valueOf(player.getOpenInventory().getType()));

        InventoryView oldView = player.getOpenInventory();
        ItemStack cursor = oldView.getCursor();
        oldView.setCursor(new ItemStack(Material.AIR));
        InventoryView view = player.openInventory(inventory);
        try {
            player.openInventory(oldView);
        } catch (Exception e) {
            Logger.debugLog("Error: " + e.getMessage());
        }
        player.setItemOnCursor(cursor);

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.PICKUP_ALL);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);
        return allowed;
    }

    public static boolean isAllowedPutItem(Player player, Inventory inventory) {
        InventoryView oldView = player.getOpenInventory();
        ItemStack cursor = oldView.getCursor();
        oldView.setCursor(new ItemStack(Material.AIR));
        InventoryView view = player.openInventory(inventory);
        player.openInventory(oldView);
        player.setItemOnCursor(cursor);

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.RIGHT, InventoryAction.PLACE_ALL);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);
        return allowed;
    }

    public static boolean isAllowedPlaceBlock(Player player, Block block) {
        BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(block.getType()), player, false, EquipmentSlot.HAND);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);
        return allowed;
    }

    public static boolean isFakeEvent(Event event) {
        return fakeEvents.contains(event);
    }
}
