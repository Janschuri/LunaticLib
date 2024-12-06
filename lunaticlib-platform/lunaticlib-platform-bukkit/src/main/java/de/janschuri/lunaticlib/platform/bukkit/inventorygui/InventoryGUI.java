package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class InventoryGUI implements InventoryHandler {

    private final Inventory inventory;
    private final static Map<Inventory, Boolean> processingClickEvent = new HashMap<>();
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();
    private final List<PlayerInvButton> playerInvButtons = new ArrayList<>();

    public InventoryGUI(Inventory inventory) {
        if (inventory != null && inventory.getSize() == getSize()) {
            this.inventory = inventory;
        } else {
            Logger.debugLog("Inventory is null or has wrong size. Creating new inventory.");
            this.inventory = createInventory();
        }
    }

    public InventoryGUI() {
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    private Inventory createInventory() {
        return Bukkit.createInventory(null, getSize(), getTitle());
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void addButton(PlayerInvButton button) {
        this.playerInvButtons.add(button);
    }

    public void decorate(Player player) {
        Logger.debugLog("Decorating:" + this.buttonMap);

        for (int i = 0; i < this.inventory.getSize(); i++) {
            InventoryButton button = this.buttonMap.get(i);
            ItemStack icon = null;

            if (button != null) {
                icon = button.getIconCreator().apply(player);
            } else {
                icon = emptyButton(i).getIconCreator().apply(player);
            }

            ItemStack item = this.inventory.getItem(i);

            if (!icon.equals(item)) {
                this.inventory.setItem(i, icon);
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (button != null) {
            if (button.getEventConsumer() != null) {
                button.getEventConsumer().accept(event);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInvClick(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            event.setCancelled(true);
        }

        if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            event.setCancelled(true);
        }

        for (PlayerInvButton playerInvButton : this.playerInvButtons) {
            if (playerInvButton.getCondition().apply(event)) {
                playerInvButton.getEventConsumer().accept(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInvDrag(InventoryDragEvent event) {
    }

    public int getSize() {
        return 54;
    }

    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    protected void reloadGui(Player player) {
        this.buttonMap.clear();
        GUIManager.openGUI(this, player);
    }

    protected InventoryButton emptyButton(int slot) {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                .consumer(event -> {});
    }

    protected boolean processingClickEvent() {
        boolean result = processingClickEvent.getOrDefault(this.inventory, false);

        processingClickEvent.put(this.inventory, true);
        Runnable runnable = () -> {
            processingClickEvent.remove(this.inventory);
        };

        Utils.scheduleTask(runnable, 100, TimeUnit.MILLISECONDS);
        return result;
    }
}
