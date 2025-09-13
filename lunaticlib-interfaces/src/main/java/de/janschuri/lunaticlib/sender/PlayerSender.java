package de.janschuri.lunaticlib.sender;

import java.util.UUID;

public interface PlayerSender extends Sender {
    UUID getUniqueId();
    String getName();
    boolean chat(String message);
    boolean hasPermission(String permission);
    String getServerName();
    boolean isOnline();
    boolean exists();
    boolean isSameServer(UUID uuid);
    void runCommand(String command);
}
