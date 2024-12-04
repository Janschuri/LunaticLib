package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InventoryGUI implements InventoryHandler {

    private final Inventory inventory;
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();
    private final List<PlayerInvButton> playerInvButtons = new ArrayList<>();

    public InventoryGUI(Inventory inventory) {
        this.inventory = inventory;
    }
    public InventoryGUI() {
        this.inventory = createInventory();
    }
    public InventoryGUI(int size, @NotNull String title) {
        this.inventory = createInventory();
    }
    public InventoryGUI(InventoryGUI gui) {
        this.inventory = gui.getInventory();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    protected Inventory createInventory(int size, @NotNull String title)
    {
        return Bukkit.createInventory(null, size, title);
    }

    protected Inventory createInventory()
    {
        return Bukkit.createInventory(null, 54, this.getClass().getSimpleName());
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void addButton(PlayerInvButton button) {
        this.playerInvButtons.add(button);
    }

    public void decorate(Player player) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            InventoryButton button = this.buttonMap.get(i);
            if (button != null) {
                ItemStack icon = button.getIconCreator().apply(player);
                this.inventory.setItem(i, icon);
            } else {
                ItemStack item = emptyButton(i).getIconCreator().apply(player);
                this.inventory.setItem(i, item);
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

    @Override
    public int getSize() {
        return this.inventory.getSize();
    }

    protected void reloadGui(Player player) {
        GUIManager.openGUI(this, player);
    }

    protected InventoryButton emptyButton(int slot) {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                .consumer(event -> {});
    }
}
