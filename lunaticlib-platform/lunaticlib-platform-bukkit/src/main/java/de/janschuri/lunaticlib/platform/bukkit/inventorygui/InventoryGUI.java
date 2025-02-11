package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InventoryGUI implements InventoryHandler {

    private final static AtomicInteger idCreator = new AtomicInteger(0);
    private static final Map<Integer, InventoryGUI> guiMap = new HashMap<>();

    private final int id;
    private Inventory inventory;
    private boolean processingClickEvent;
    private String title;
    private final int size;
    private final Map<Integer, InventoryButton> buttonMap;
    private final List<PlayerInvButton> playerInvButtonList;

    public InventoryGUI() {
        this(null, 54);
    }

    public InventoryGUI(String title, int size) {
        this.id = idCreator.getAndIncrement();
        this.processingClickEvent = false;
        this.title = title == null ? getDefaultTitle() : title;
        this.size = size;
        this.inventory = createInventory();
        this.buttonMap = new HashMap<>();
        this.playerInvButtonList = new ArrayList<>();

        guiMap.put(this.id, this);
    }

    protected static InventoryGUI getGUI(int id) {
        return guiMap.get(id);
    }

    public int getId() {
        return this.id;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    private Inventory createInventory() {
        return Bukkit.createInventory(null, getSize(), getTitle());
    }

    @Override
    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    @Override
    public void addButton(PlayerInvButton button) {
        this.playerInvButtonList.add(button);
    }

    @Override
    public void init(Player player) {
        for (int i = 0; i < getInventory().getSize(); i++) {
            InventoryButton button = this.buttonMap.get(i);
            ItemStack icon;

            if (button != null) {
                icon = button.getIconCreator().apply(player);
            } else {
                icon = emptyButton(i).getIconCreator().apply(player);
            }

            ItemStack item = getInventory().getItem(i);

            if (isSameButton(item, icon)) {
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
        this.init((Player) event.getPlayer());
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

        for (PlayerInvButton playerInvButton : this.playerInvButtonList) {
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
        return size;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultTitle() {
        return this.getClass().getSimpleName();
    }

    public final String getTitle() {
        if (title == null) {
            title = "InventoryGUI";
        }

        return title;
    }

    public void reloadGui() {
        reloadGui(false);
    }

    public void reloadGui(boolean forceNewInventory) {
        this.buttonMap.clear();

        List<HumanEntity> humanEntities = getInventory().getViewers();

        if (humanEntities.isEmpty()) {
            return;
        }

        if (forceNewInventory) {
            this.inventory = createInventory();
        }

        for (HumanEntity humanEntity : humanEntities) {

            if (humanEntity instanceof Player p) {
                GUIManager.openGUI(this, p);
            }
        }
    }

    protected InventoryButton emptyButton(int slot) {
        return new InventoryButton()
                .creator((player) -> new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
                .consumer(event -> {});
    }

    public boolean processingClickEvent() {
        boolean result = processingClickEvent;

        processingClickEvent = true;
        Runnable runnable = () -> {
            processingClickEvent = false;
        };

        Utils.scheduleTask(runnable, 100, TimeUnit.MILLISECONDS);
        return result;
    }

    @Override
    public final NamespacedKey getGuiIdKey() {
        return new NamespacedKey(BukkitLunaticLib.getInstance(), "item-gui-id");
    }

    @Override
    public final ItemStack getItemWithGuiId(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        NamespacedKey key = getGuiIdKey();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, name);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSameButton(ItemStack button1, ItemStack button2) {
        if (button1 == null || button2 == null) {
            return false;
        }

        ItemMeta itemMeta = button1.getItemMeta();
        ItemMeta buttonMeta = button2.getItemMeta();

        if (itemMeta == null || buttonMeta == null) {
            return false;
        }

        NamespacedKey key = getGuiIdKey();

        PersistentDataContainer button1Container = itemMeta.getPersistentDataContainer();
        PersistentDataContainer button2Container = buttonMeta.getPersistentDataContainer();

        String button1Name = button1Container.get(key, PersistentDataType.STRING);
        String button2Name = button2Container.get(key, PersistentDataType.STRING);

        return button1Name != null && button1Name.equals(button2Name);
    }
}
