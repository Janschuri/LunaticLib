package de.janschuri.lunaticlib.sender;

import java.util.UUID;

public interface PlayerSender<P extends H, H> extends Sender<H> {
    UUID getUniqueId();
    String getName();
    void chat(String message);
    boolean hasPermission(String permission);
    String getServerName();
    default boolean isSameServer(PlayerSender playerSender){
        return this.getServerName().equals(playerSender.getServerName());
    }
    void runCommand(String command);
    P getHandle();
}
