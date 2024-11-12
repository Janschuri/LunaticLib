package de.janschuri.lunaticlib.platform.bukkit.inventorygui;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.GetNameRequest;
import de.janschuri.lunaticlib.common.futurerequests.requests.RunCommandRequest;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DecisionGUI extends InventoryGUI {

    private final DecisionMessage decisionMessage;

    public DecisionGUI(DecisionMessage decisionMessage) {
        super(createInventory(decisionMessage));
        this.decisionMessage = decisionMessage;
    }

    private static Inventory createInventory(DecisionMessage decisionMessage) {
        String title = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getPrefix());
        return Bukkit.createInventory(null, 9, title);
    }

    @Override
    public void decorate(Player player) {
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

        super.decorate(player);
    }
    private InventoryButton createGrayButton() {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
    }

    private InventoryButton createGreenButton() {
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        String confirmText = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getConfirmText());
        ItemMeta itemMeta = itemStack.getItemMeta();



        if (itemMeta != null) {
            itemMeta.setDisplayName(confirmText);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    String command = decisionMessage.getConfirmCommand();
                    if (command.startsWith("/")) {
                        command = command.substring(1);
                    }

                    String commandToExecute = command;

                    performCommand(player, commandToExecute)
                            .thenAccept(success -> {
                                if (success) {
                                    Bukkit.getScheduler().runTask(BukkitLunaticLib.getInstance(), player::closeInventory);
                                } else {
                                    Logger.errorLog("Error while executing command: " + commandToExecute);
                                }
                            });
                });
    }

    private InventoryButton createRedButton() {
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        String denyText = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getDenyText());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(denyText);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack)
                .consumer(event -> {
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
                });
    }

    private InventoryButton createWhiteButton() {
        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        String text = LegacyComponentSerializer.legacySection().serialize(decisionMessage.getQuestion());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(text);
        }
        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack);
    }

    private CompletableFuture<Boolean> performCommand(Player player, String command) {

        if (decisionMessage.isExecuteFromBackend()) {
            return new RunCommandRequest().getAsync(player.getUniqueId(), command);
        }

        boolean success = player.performCommand(command);
        return CompletableFuture.completedFuture(success);
    }
}
