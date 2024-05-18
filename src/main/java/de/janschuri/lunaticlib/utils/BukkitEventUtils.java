package de.janschuri.lunaticlib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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

public final class BukkitEventUtils {

    private BukkitEventUtils() {
    }

    public static boolean isAllowedViewChest(Player player, Block chest) {
        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), chest, BlockFace.UP);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        return allowed;
    }

    public static boolean isAllowedTakeItem(Player player, Inventory inventory) {
        InventoryView oldView = player.getOpenInventory();
        ItemStack cursor = oldView.getCursor();
        oldView.setCursor(new ItemStack(Material.AIR));
        InventoryView view = player.openInventory(inventory);
        player.openInventory(oldView);
        player.setItemOnCursor(cursor);

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.PICKUP_ALL);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
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
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        return allowed;
    }

    public static boolean isAllowedBreakBlock(Player player, Block block) {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        return allowed;
    }
}
