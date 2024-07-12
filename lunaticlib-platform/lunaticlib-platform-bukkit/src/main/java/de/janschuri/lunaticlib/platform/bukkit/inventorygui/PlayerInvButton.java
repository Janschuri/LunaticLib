package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerInvButton {

    private Function<Player, ItemStack> iconCreator;
    private Consumer<InventoryClickEvent> eventConsumer;
    private Function<InventoryClickEvent, Boolean> condition;

    public PlayerInvButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public PlayerInvButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    public PlayerInvButton condition(Function<InventoryClickEvent, Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public Consumer<InventoryClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }

    public Function<InventoryClickEvent, Boolean> getCondition() {
        return this.condition;
    }
}

