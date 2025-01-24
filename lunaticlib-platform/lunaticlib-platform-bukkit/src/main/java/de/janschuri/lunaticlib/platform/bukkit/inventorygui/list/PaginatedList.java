package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface PaginatedList<T> extends ListHandler<T> {

    default void addPaginateButtons(Player player) {
        int page = getPage();
        int pageCount = getPageCount();

        if (page > 0) {
            addButton(previousPageSlot(), previousPageButton());
        }

        addButton(currentPageSlot(), currentPageButton());

        if (page < pageCount) {
            addButton(nextPageSlot(), createNextPageButton());
        }
    }

    default int getPageCount() {
        return getProcessedItems().size() / getPageSize();
    }

    default List<?> paginate(List<?> list) {
        validatePage();
        int page = getPage();
        int pageSize = getPageSize();

        Logger.debugLog("Page: " + page);

        int fromIndex = page * pageSize;
        int toIndex = Math.min((page + 1) * pageSize, list.size());
        if (fromIndex > list.size()) {
            fromIndex = list.size();
        }

        Logger.debugLog("Paginating list from " + fromIndex + " to " + toIndex);
        Logger.debugLog("List size: " + list.size());

        return list.subList(fromIndex, toIndex);
    }

    int getPage();
    void setPage(int page);

    default void nextPage(Player player) {
        if (getPage() < getPageCount()) {
            int newPage = getPage() + 1;
            setPage(newPage);
            reloadGui();
        }
    }

    default void previousPage(Player player) {
        if (getPage() > 0) {
            int newPage = getPage() - 1;
            setPage(newPage);
            reloadGui();
        }
    }

    default InventoryButton currentPageButton() {
        return new InventoryButton()
                .creator(this::currentPageItem);
    }

    default InventoryButton previousPageButton() {
        return new InventoryButton()
                .creator(this::previousPageItem)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    previousPage(player);
                });
    }

    default InventoryButton createNextPageButton() {

        return new InventoryButton()
                .creator(this::nextPageItem)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    nextPage(player);
                });
    }

    default void validatePage() {
        int pageCount = getPageCount();
        int page = getPage();

        if (page > pageCount) {
            setPage(pageCount);
        }
    }

    default int currentPageSlot() {
        return 49;
    }

    default int nextPageSlot() {
        return 50;
    }

    default int previousPageSlot() {
        return 48;
    }

    default ItemStack currentPageItem(Player player) {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Page " + (getPage() + 1) + "/" + (getPageCount() + 1));
        item.setItemMeta(meta);
        return item;
    }

    default ItemStack nextPageItem(Player player) {
        ItemStack item = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926");
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(">>>");
        item.setItemMeta(meta);
        return item;
    }

    default ItemStack previousPageItem(Player player) {
        ItemStack item = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180");
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("<<<");
        item.setItemMeta(meta);
        return item;
    }
}