package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import com.mysql.cj.log.Log;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ListGUI<T> extends InventoryGUI {

    private static final Map<Inventory, Integer> pageMap = new HashMap<>();

    public ListGUI() {
        super();
    }

    public ListGUI(Inventory inventory) {
        super(inventory);
    }

    @Override
    public void decorate(Player player) {
        int page = getPage();
        int pageCount = getPageCount();
        int pageSize = getPageSize();
        List<T> paginatedItems = getItems(page);


        for (int i = 0; i < pageSize; i++) {
            if (i >= paginatedItems.size()) {
                addButton(i+9, emptyListItemButton(i+9));
                continue;
            }

            T item = paginatedItems.get(i);
            InventoryButton button = listItemButton(item);
            addButton(i+9, button);
        }

        Logger.debugLog("Page: " + page + " Pages: " + pageCount);
        if (page > 0) {
            addButton(48, previousPageButton());
        }

        addButton(49, currentPageButton());

        if (page < pageCount) {
            addButton(50, createNextPageButton());
        }

        super.decorate(player);
    }

    protected abstract InventoryButton listItemButton(T item);

    protected abstract List<T> getItems();

    protected int getPage() {
        return pageMap.getOrDefault(getInventory(), 0);
    }

    protected void nextPage(Player player) {
        if (getPage() < getPageCount()) {
            int newPage = getPage() + 1;
            pageMap.put(getInventory(), newPage);
            reloadGui(player);
        }
    }

    protected void previousPage(Player player) {
        if (getPage() > 0) {
            int newPage = getPage() - 1;
            pageMap.put(getInventory(), newPage);
            reloadGui(player);
        }
    }

    protected int getPageSize() {
        return 36;
    }

    protected InventoryButton previousPageButton() {
        return new InventoryButton()
                .creator(this::previousPageItem)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    previousPage(player);
                });
    }

    protected InventoryButton createNextPageButton() {

        return new InventoryButton()
                .creator(this::nextPageItem)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    nextPage(player);
                });
    }

    protected InventoryButton currentPageButton() {
        return new InventoryButton()
                .creator(this::currentPageItem);
    }

    protected ItemStack currentPageItem(Player player) {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Page " + (getPage() + 1) + "/" + (getPageCount() + 1));
        item.setItemMeta(meta);
        return item;
    }

    protected ItemStack nextPageItem(Player player) {
        ItemStack item = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926");
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(">>>");
        item.setItemMeta(meta);
        return item;
    }

    protected ItemStack previousPageItem(Player player) {
        ItemStack item = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180");
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("<<<");
        item.setItemMeta(meta);
        return item;
    }

    protected InventoryButton emptyListItemButton(int slot) {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.AIR));
    }

    protected List<T> getItems(int page) {
        List<T> items = getItems();
        int fromIndex = page * getPageSize();
        int toIndex = Math.min(fromIndex + getPageSize(), items.size());
        return items.subList(fromIndex, toIndex);
    }

    protected int getPageCount() {
        return getItems().size() / getPageSize();
    }
}
