package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
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
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InventoryGUI implements InventoryHandler {

    private final int id;
    private final static AtomicInteger idCreator = new AtomicInteger(0);
    private final static Map<Integer, Inventory> inventoryMap = new HashMap<>();
    private final static Map<Integer, Boolean> processingClickEvent = new HashMap<>();

    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();
    private final List<PlayerInvButton> playerInvButtons = new ArrayList<>();

    public InventoryGUI() {
        this(idCreator.getAndIncrement());
    }

    public InventoryGUI(int id) {
        this.id = id == -1 ? idCreator.getAndIncrement() : id;

        if (!inventoryMap.containsKey(this.id)) {
            inventoryMap.put(this.id, createInventory());
        }
    }

    public InventoryGUI inventory(Inventory inventory) {
        if (inventory != getInventory()) {
            getInventory().getViewers().forEach(HumanEntity::closeInventory);
            inventoryMap.put(this.id, inventory);
        }
        return this;
    }

    public int getId() {
        return this.id;
    }

    public Inventory getInventory() {
        return inventoryMap.get(this.id);
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
        for (int i = 0; i < getInventory().getSize(); i++) {
            InventoryButton button = this.buttonMap.get(i);
            ItemStack icon;

            if (button != null) {
                icon = button.getIconCreator().apply(player);
            } else {
                icon = emptyButton(i).getIconCreator().apply(player);
            }

            ItemStack item = getInventory().getItem(i);

            if (item == null) {
                getInventory().setItem(i, icon);
                continue;
            }

            if (item.equals(icon)) {
                continue;
            }

            if (item.isSimilar(icon)) {
                icon.setAmount(item.getAmount());
                continue;
            }

            if (item.getType() == icon.getType()) {
                icon.setItemMeta(item.getItemMeta());
                continue;
            }

            getInventory().setItem(i, icon);
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
                break;
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
        boolean result = processingClickEvent.getOrDefault(this.id, false);

        processingClickEvent.put(this.id, true);
        Runnable runnable = () -> {
            processingClickEvent.remove(this.id);
        };

        Utils.scheduleTask(runnable, 100, TimeUnit.MILLISECONDS);
        return result;
    }
}
