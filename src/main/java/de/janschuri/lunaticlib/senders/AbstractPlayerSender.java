package de.janschuri.lunaticlib.senders;

import java.util.UUID;

public abstract class AbstractPlayerSender extends AbstractSender {
    private final UUID uuid;

    protected AbstractPlayerSender(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }
    public abstract UUID getUniqueId(String name);
    public abstract String getName();
    public abstract boolean chat(String message);
    public abstract boolean hasItemInMainHand();
    public abstract byte[] getItemInMainHand();
    public abstract boolean removeItemInMainHand();
    public abstract boolean giveItemDrop(byte[] item);
    public abstract boolean hasPermission(String permission);
    public abstract String getServerName();
    public abstract double[] getPosition();
    public abstract boolean isOnline();
    public abstract boolean isInRange(UUID playerUUID, double range);
    public abstract boolean exists();
    public abstract AbstractPlayerSender getPlayerCommandSender(UUID uuid);
    public abstract AbstractPlayerSender getPlayerCommandSender(String name);

    public abstract boolean isSameServer(UUID uuid);

}
