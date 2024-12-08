package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.platform.bukkit.inventorygui.list.ListGUI;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectMobGUI extends ListGUI<EntityType> {

    private static final Map<Integer, Consumer<EntityType>> consumerMap = new HashMap<>();

    public SelectMobGUI() {
        super();
    }

    public SelectMobGUI(int id) {
        super(id);
    }

    public SelectMobGUI consumer(Consumer<EntityType> consumer) {
        consumerMap.put(getId(), consumer);
        return this;
    }

    public Consumer<EntityType> getConsumer() {
        return consumerMap.get(getId());
    }

    @Override
    public InventoryButton listItemButton(EntityType entityType) {
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
    public List<EntityType> getItems() {
        return Arrays.stream(EntityType.values())
                .filter(EntityType::isAlive)
                .collect(Collectors.toList());
    }
}
