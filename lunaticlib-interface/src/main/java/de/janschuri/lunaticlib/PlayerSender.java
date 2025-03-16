package de.janschuri.lunaticlib;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerSender extends Sender {
    UUID getUniqueId();
    String getName();
    String getSkinURL();
    boolean chat(String message);
    boolean hasItemInMainHand();
    byte[] getItemInMainHand();
    boolean removeItemInMainHand();
    boolean giveItemDrop(byte[] item);
    boolean hasPermission(String permission);
    String getServerName();
    double[] getPosition();
    boolean isOnline();
    boolean isInRange(UUID playerUUID, double range);
    boolean exists();
    boolean isSameServer(UUID uuid);
    default boolean sendMessage(DecisionMessage message, boolean asInventoryGUI) {
        if (asInventoryGUI) {
            return openDecisionGUI(message);
        } else {
            return sendMessage(message.asComponent());
        }
    }
    boolean openDecisionGUI(DecisionMessage message);
    void runCommand(String command);
}
