package de.janschuri.lunaticlib.sender.platform.velocity.impl;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.proxy.Player;
import de.janschuri.lunaticlib.sender.PlayerSender;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static de.janschuri.lunaticlib.sender.platform.velocity.VelocityLunaticLibCommands.getProxy;

public class VelocityPlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public VelocityPlayerSenderImpl(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public VelocityPlayerSenderImpl(UUID uuid) {
        super(getProxy().getPlayer(uuid).orElse(null));
        this.uuid = uuid;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        Optional<Player> player = getProxy().getPlayer(uuid);
        return player.map(Player::getUsername).orElse(null);

    }

    @Override
    public boolean chat(String message) {
        Optional<com.velocitypowered.api.proxy.Player> player = getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            player.get().spoofChatInput(
                    message
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean isOnline() {
        Collection<Player> players = getProxy().getAllPlayers();
        for (com.velocitypowered.api.proxy.Player player : players) {
            if (player.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public void runCommand(String command) {
        Optional<com.velocitypowered.api.proxy.Player> player = getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            CommandManager commandManager = getProxy().getCommandManager();
            commandManager.executeAsync(player.get(), command);
        }
    }

    @Override
    public boolean isSameServer(UUID player1UUID) {
        Optional<com.velocitypowered.api.proxy.Player> player1 = getProxy().getPlayer(player1UUID);
        Optional<com.velocitypowered.api.proxy.Player> player = getProxy().getPlayer(uuid);

        return player1.isPresent() && player.isPresent() && player1.get().getCurrentServer().get().getServerInfo().getName().equals(player.get().getCurrentServer().get().getServerInfo().getName());
    }


    @Override
    public String getServerName() {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.getCurrentServer().get().getServerInfo().getName()).orElse(null);
    }
}
