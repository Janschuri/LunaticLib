package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ListGUI<T> extends InventoryGUI {

    private static final Map<Integer, Integer> pageMap = new HashMap<>();
    private final static Map<Integer, String> searchMap = new HashMap<>();

    public ListGUI() {
        super();
    }

    public ListGUI(int id) {
        super(id);
    }

    @Override
    public void decorate(Player player) {
        int page = getPage();
        int pageCount = getPageCount();
        int pageSize = getPageSize();
        List<T> paginatedItems = getProcessedItems(player);


        for (int i = 0; i < pageSize; i++) {
            if (i >= paginatedItems.size()) {
                addButton(i+9, emptyListItemButton(i+9));
                continue;
            }

            T item = paginatedItems.get(i);
            InventoryButton button = listItemButton(item);
            addButton(i+9, button);
        }

        if (getSearchFilter(player) != null) {
            addButton(getSearchSlot(), createSearchButton());
        }

        if (page > 0) {
            addButton(previousPageSlot(), previousPageButton());
        }

        addButton(currentPageSlot(), currentPageButton());

        if (page < pageCount) {
            addButton(nextPageSlot(), createNextPageButton());
        }

        super.decorate(player);
    }

    protected abstract InventoryButton listItemButton(T item);

    protected abstract List<T> getItems();

    protected int getPage() {
        return pageMap.getOrDefault(getId(), 0);
    }

    protected void nextPage(Player player) {
        if (getPage() < getPageCount()) {
            int newPage = getPage() + 1;
            pageMap.put(getId(), newPage);
            reloadGui(player);
        }
    }

    protected void previousPage(Player player) {
        if (getPage() > 0) {
            int newPage = getPage() - 1;
            pageMap.put(getId(), newPage);
            reloadGui(player);
        }
    }

    protected int getPageSize() {
        return 36;
    }

    public String getSearch() {
        searchMap.putIfAbsent(getId(), "");
        return searchMap.get(getId());
    }

    public void setSearch(String search) {
        searchMap.put(getId(), search);
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

    public int currentPageSlot() {
        return 49;
    }

    public ItemStack currentPageItem(Player player) {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Page " + (getPage() + 1) + "/" + (getPageCount() + 1));
        item.setItemMeta(meta);
        return item;
    }

    public int nextPageSlot() {
        return 50;
    }

    public ItemStack nextPageItem(Player player) {
        ItemStack item = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926");
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(">>>");
        item.setItemMeta(meta);
        return item;
    }

    public int previousPageSlot() {
        return 48;
    }

    public ItemStack previousPageItem(Player player) {
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

    protected List<T> getProcessedItems(Player player) {
        int fromIndex = getPage() * getPageSize(); // Calculate starting index

        return getItems()
                .stream()
                .filter(getSearchFilter(player))
                .skip(fromIndex)
                .limit(getPageSize())
                .toList();
    }

    protected int getPageCount() {
        return getItems().size() / getPageSize();
    }

    public Predicate<T> getSearchFilter(Player player) {
        return null;
    }

    private InventoryButton createSearchButton() {
        return new InventoryButton()
                .creator(this::getSearchItem)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();
                    player.closeInventory();

                    SignGUI gui = SignGUI.builder()
                            .setType(Material.DARK_OAK_SIGN)
                            .setHandler((p, result) -> {
                                StringBuilder search = new StringBuilder();
                                for (int i = 0; i < 4; i++) {
                                    search.append(result.getLine(i));
                                }

                                return List.of(
                                        SignGUIAction.run(() ->{
                                            Bukkit.getScheduler().runTask(BukkitLunaticLib.getInstance(), () -> {
                                                setSearch(search.toString());
                                                reloadGui(player);
                                            });
                                        })
                                );
                            })
                            .build();

                    gui.open(player);
                });
    }

    public int getSearchSlot() {
        return 0;
    }

    public ItemStack getSearchItem(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Search");
        item.setItemMeta(meta);
        return item;
    }
}
