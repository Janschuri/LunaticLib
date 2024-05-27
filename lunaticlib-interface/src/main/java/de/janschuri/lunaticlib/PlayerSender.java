package de.janschuri.lunaticlib;

import java.util.UUID;

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
}
