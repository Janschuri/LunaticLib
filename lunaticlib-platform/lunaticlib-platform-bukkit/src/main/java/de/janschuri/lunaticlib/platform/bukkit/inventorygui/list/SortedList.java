package de.janschuri.lunaticlib.platform.bukkit.inventorygui.list;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.InventoryButton;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public interface SortedList<T> extends ListHandler<T> {

    default void addSorterButtons(Player player) {
        addButton(getSortSlot(), createSorterButton());

        addButton(getDescendingSlot(), createDescendingButton());
    }

    default List<?> sortItems(Player player, List<?> items) {
        Comparator<T> comparator = getCurrentSorter().getComparator(player);

        if (isDescending()) {
            comparator = comparator.reversed();
        }

        return items.stream()
                .map(item -> (T) item)
                .sorted(comparator)
                .toList();
    }

    void setSorterIndex(int sorter);

    int getSorterIndex();

    default Sorter<T> getCurrentSorter() {
        return getSorters().get(getSorterIndex());
    }

    default void nextSorter(Player player) {
        setSorterIndex((getSorterIndex() + 1) % getSorters().size());
        reloadGui();
    }

    default InventoryButton createSorterButton() {
        Sorter<T> sorter = getCurrentSorter();

        return new InventoryButton()
                .creator(player -> sorter.getIconCreator().apply(player))
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    nextSorter(player);
                });
    }

    private InventoryButton createDescendingButton() {
        return new InventoryButton()
                .creator(player -> {
                    if (isDescending()) {
                        return getDescendingIcon();
                    } else {
                        return getAscendingIcon();
                    }
                })
                .consumer(event -> {
                    if (processingClickEvent()) {
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();

                    toggleDescending();

                    reloadGui();
                });
    }

    default int getDescendingSlot() {
        return 5;
    }

    default int getSortSlot() {
        return 4;
    }

    List<Sorter<T>> getSorters();

    boolean isDescending();

    void setDescending(boolean descending);

    default void toggleDescending() {
        setDescending(!isDescending());
    }

    default ItemStack getAscendingIcon() {
        ItemStack arrow = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/b221da4418bd3bfb42eb64d2ab429c61decb8f4bf7d4cfb77a162be3dcb0b927");
        ItemMeta meta = arrow.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Ascended");
        arrow.setItemMeta(meta);

        return getItemWithGuiId(arrow, "ascending");
    }

    default ItemStack getDescendingIcon() {
        ItemStack arrow = ItemStackUtils.getSkullFromURL("https://textures.minecraft.net/texture/a3852bf616f31ed67c37de4b0baa2c5f8d8fca82e72dbcafcba66956a81c4");
        ItemMeta meta = arrow.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Descended");
        arrow.setItemMeta(meta);

        return getItemWithGuiId(arrow, "descending");
    }

    class Sorter<T> {
        private final String name;
        private Function<Player, Comparator<T>> comparatorCreator;
        private Function<Player, ItemStack> iconCreator;

        public Sorter(String name) {
            this.name = name;
        }

        public Sorter<T> creator(Function<Player, ItemStack> iconCreator) {
            this.iconCreator = iconCreator;
            return this;
        }

        public Sorter<T> comparator(Function<Player, Comparator<T>> comparatorCreator) {
            this.comparatorCreator = comparatorCreator;
            return this;
        }

        public Comparator<T> getComparator(Player player) {
            return this.comparatorCreator.apply(player);
        }

        public Function<Player, ItemStack> getIconCreator() {
            return this.iconCreator;
        }

        public String getName() {
            return name;
        }
    }
}
