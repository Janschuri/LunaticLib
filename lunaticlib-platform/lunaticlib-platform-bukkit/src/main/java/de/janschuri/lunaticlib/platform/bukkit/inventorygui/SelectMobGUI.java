package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectMobGUI extends ListGUI<EntityType> {

    private final Inventory inventory;
    private static final Map<Inventory, Consumer<EntityType>> consumerMap = new HashMap<>();

    public SelectMobGUI(Inventory inventory) {
        super(inventory);
        this.inventory = getInventory();
    }

    public SelectMobGUI() {
        super();
        this.inventory = getInventory();
    }

    public SelectMobGUI consumer(Consumer<EntityType> consumer) {
        consumerMap.put(this.inventory, consumer);
        return this;
    }

    public Consumer<EntityType> getConsumer() {
        return consumerMap.get(this.inventory);
    }

    @Override
    protected InventoryButton listItemButton(EntityType entityType) {
        ItemStack itemStack = ItemStackUtils.getSpawnEgg(entityType);

        SpawnEggMeta meta = (SpawnEggMeta) itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(entityType.name());
            itemStack.setItemMeta(meta);
        }

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    getConsumer().accept(entityType);
                });
    }

    @Override
    protected List<EntityType> getItems() {
        return Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .collect(Collectors.toList());
    }
}
