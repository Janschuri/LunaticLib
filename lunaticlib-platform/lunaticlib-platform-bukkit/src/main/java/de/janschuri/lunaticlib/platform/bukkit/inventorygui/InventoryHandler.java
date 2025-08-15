package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryHandler {

    int getId();

    void init(Player player);

    Inventory getInventory();

    void addButton(int slot, InventoryButton button);

    void addButton(PlayerInvButton button);

    void onClick(InventoryClickEvent event);

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);
    void onDrag(InventoryDragEvent event);

    void reloadGui();

    boolean processingClickEvent();

    String getTitle();

    void setTitle(String title);

    NamespacedKey getGuiIdKey();

    ItemStack getItemWithGuiId(ItemStack item, String name);

    boolean isSameButton(ItemStack item, ItemStack button);
}
