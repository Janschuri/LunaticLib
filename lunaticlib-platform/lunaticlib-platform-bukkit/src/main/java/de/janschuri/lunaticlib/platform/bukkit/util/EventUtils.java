package de.janschuri.lunaticlib.platform.bukkit.util;

import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class EventUtils {

    private static List<Event> fakeEvents = new ArrayList<>();

    private EventUtils() {
    }

    public static boolean isAllowedInteract(Player player, Block block) {
        PlayerInteractEvent event = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), block, BlockFace.UP);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);

        Logger.debugLog("AllowedInteract: " + allowed);
        return allowed;
    }

    public static boolean isAllowedTakeItem(Player player, Inventory inventory) {
        InventoryView view = simulateInventoryView(player, inventory);

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.LEFT, InventoryAction.PICKUP_ALL);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);

        Logger.debugLog("AllowedTakeItem: " + allowed);
        return allowed;
    }

    public static boolean isAllowedPutItem(Player player, Inventory inventory) {
        InventoryView view = simulateInventoryView(player, inventory);

        InventoryClickEvent event = new InventoryClickEvent(view, InventoryType.SlotType.CONTAINER, 0, ClickType.RIGHT, InventoryAction.PLACE_ALL);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);

        Logger.debugLog("AllowedPutItem: " + allowed);
        return allowed;
    }

    public static boolean isAllowedPlaceBlock(Player player, Block block) {
        BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(block.getType()), player, false, EquipmentSlot.HAND);
        fakeEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
        boolean allowed = !event.isCancelled();
        event.setCancelled(true);
        fakeEvents.remove(event);

        Logger.debugLog("AllowedPlaceBlock: " + allowed);
        return allowed;
    }

    public static boolean isFakeEvent(Event event) {
        return fakeEvents.contains(event);
    }

    public static InventoryView simulateInventoryView(Player player, Inventory inventory) {
        InventoryView oldView = player.getOpenInventory();
        ItemStack cursor = oldView.getCursor();
        oldView.setCursor(new ItemStack(Material.AIR));
        InventoryView simulatedView = new SimulatedInventoryView(player, inventory);
        player.setItemOnCursor(cursor);
        return simulatedView;
    }

    private static class SimulatedInventoryView extends InventoryView {
        private final Player player;
        private final Inventory inventory;

        public SimulatedInventoryView(Player player, Inventory inventory) {
            this.player = player;
            this.inventory = inventory;
        }

        @Override
        public @NotNull Inventory getTopInventory() {
            return inventory;
        }

        @Override
        public @NotNull Inventory getBottomInventory() {
            return player.getInventory();
        }

        @Override
        public @NotNull HumanEntity getPlayer() {
            return player;
        }

        @Override
        public @NotNull InventoryType getType() {
            return inventory.getType();
        }

        @Override
        public @NotNull String getTitle() {
            return "";
        }
    }
}
