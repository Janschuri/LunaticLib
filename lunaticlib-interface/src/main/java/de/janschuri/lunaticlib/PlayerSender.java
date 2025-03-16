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
    CompletableFuture<Boolean> hasItemInMainHand();
    CompletableFuture<byte[]> getItemInMainHand();
    CompletableFuture<Boolean> removeItemInMainHand();
    CompletableFuture<Boolean> giveItemDrop(byte[] item);
    boolean hasPermission(String permission);
    String getServerName();
    CompletableFuture<double[]> getPosition();
    boolean isOnline();
    CompletableFuture<Boolean> isInRange(UUID playerUUID, double range);
    boolean exists();
    boolean isSameServer(UUID uuid);
    default boolean sendMessage(DecisionMessage message, boolean asInventoryGUI) {
        if (asInventoryGUI) {
            return openDecisionGUI(message)
                    .thenApply(aBoolean -> aBoolean && sendMessage(message.asComponent()))
                    .join();
        } else {
            return sendMessage(message.asComponent());
        }
    }
    CompletableFuture<Boolean> openDecisionGUI(DecisionMessage message);
    void runCommand(String command);
}
