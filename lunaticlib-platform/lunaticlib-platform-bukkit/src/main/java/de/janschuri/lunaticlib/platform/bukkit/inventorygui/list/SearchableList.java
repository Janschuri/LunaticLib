package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface SearchableList<T> extends ListHandler<T> {

    default void addSearchButtons(Player player) {
        addButton(getSearchSlot(), createSearchButton());
    }

    default void addSearchFilter(Player player) {
        addFilter("search", getSearchFilter(player));
    }

    Predicate<T> getSearchFilter(Player player);

    String getSearch();

    void setSearch(String search);

    default InventoryButton createSearchButton() {
        return new InventoryButton()
                .creator(this::getSearchItem)
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();
                    player.closeInventory();

                    SignGUI gui = null;
                    try {
                        gui = SignGUI.builder()
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
                    } catch (SignGUIVersionException e) {
                        throw new RuntimeException(e);
                    }

                    gui.open(player);
                });
    }

    default int getSearchSlot() {
        return 0;
    }

    default ItemStack getSearchItem(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Search");
        item.setItemMeta(meta);
        return item;
    }
}
