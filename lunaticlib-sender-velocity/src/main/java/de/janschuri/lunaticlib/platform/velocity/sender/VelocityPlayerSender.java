package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.janschuri.lunaticlib.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VelocityPlayerSender extends VelocitySender implements PlayerSender<Player, CommandSource> {

    private final Player player;

    public VelocityPlayerSender(@NotNull Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public void chat(String message) {
        player.spoofChatInput(
                message
        );
    }

    @Override
    public void runCommand(String command) {
            CommandManager commandManager = ((VelocitySenderAdapter) VelocitySenderHandler.adapter()).proxy().getCommandManager();
            commandManager.executeAsync(player, command);
    }

    @Override
    public boolean isSameServer(PlayerSender playerSender) {
        Player otherPlayer = ((VelocityPlayerSender) playerSender).player;
        return player.getCurrentServer().get().getServerInfo().getName().equals(otherPlayer.getCurrentServer().get().getServerInfo().getName());
    }


    @Override
    public String getServerName() {
        return player.getCurrentServer().get().getServerInfo().getName();
    }

    @Override
    public Player getHandle() {
        return player;
    }
}
