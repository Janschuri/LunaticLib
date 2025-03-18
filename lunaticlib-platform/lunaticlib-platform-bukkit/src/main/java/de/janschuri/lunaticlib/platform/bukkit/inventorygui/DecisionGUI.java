package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.RunCommandRequest;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DecisionGUI extends InventoryGUI {

    private Consumer<InventoryClickEvent> acceptConsumer;
    private Consumer<InventoryClickEvent> denyConsumer;
    private String question = "Are you sure?";
    private String confirmText = "Yes";
    private String denyText = "No";
    private final boolean executeFromBackend = false;

    public DecisionGUI(String title) {
        super(title, 9);
    }

    public DecisionGUI(Component title) {
        super(LegacyComponentSerializer.legacySection().serialize(title), 9);
    }

    public DecisionGUI(DecisionMessage decisionMessage) {
        super(LegacyComponentSerializer.legacySection().serialize(decisionMessage.getPrefix()), 9);

        this.question = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getQuestion());
        this.confirmText = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getConfirmText());
        this.denyText = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getDenyText());

        this.acceptConsumer = event -> {
            Player player = (Player) event.getWhoClicked();

            String command = decisionMessage.getConfirmCommand();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }

            String commandToExecute = command;

            performCommand(player, commandToExecute)
                    .thenAccept(success -> {
                        if (success) {
                            Bukkit.getScheduler().runTask(BukkitLunaticLib.getInstance(), () -> player.closeInventory());
                        } else {
                            Logger.errorLog("Error while executing command: " + commandToExecute);
                        }
                    });
        };

        this.denyConsumer = event -> {
            Player player = (Player) event.getWhoClicked();

            String command = decisionMessage.getDenyCommand();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }

            String commandToExecute = command;

            performCommand(player, commandToExecute)
                    .thenAccept(success -> {
                        if (success) {
                            player.closeInventory();
                        } else {
                            Logger.errorLog("Error while executing command: " + commandToExecute);
                        }
                    });
        };
    }

    @Override
    public void init(Player player) {
        int inventorySize = this.getInventory().getSize();

        for (int i = 0; i < inventorySize; i++) {
            if (i == 2) {
                this.addButton(i, createGreenButton());
            } else if (i == 4) {
                this.addButton(i, createWhiteButton());
            } else if (i == 6) {
                this.addButton(i, createRedButton());
            } else {
                this.addButton(i, createGrayButton());
            }
        }

        super.init(player);
    }
    private InventoryButton createGrayButton() {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
    }

    private InventoryButton createGreenButton() {
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(confirmText);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(acceptConsumer);
    }

    private InventoryButton createRedButton() {
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(denyText);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(denyConsumer);
    }

    private InventoryButton createWhiteButton() {
        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(question);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack);
    }

    private CompletableFuture<Boolean> performCommand(Player player, String command) {
        if (executeFromBackend) {
            return new RunCommandRequest().get(player.getUniqueId(), command);
        }

        return CompletableFuture.completedFuture(player.performCommand(command));
    }

    @Override
    public int getSize() {
        return 9;
    }

    public DecisionGUI accept(Consumer<InventoryClickEvent> consumer) {
        this.acceptConsumer = consumer;
        return this;
    }

    public DecisionGUI deny(Consumer<InventoryClickEvent> consumer) {
        this.denyConsumer = consumer;
        return this;
    }
}
