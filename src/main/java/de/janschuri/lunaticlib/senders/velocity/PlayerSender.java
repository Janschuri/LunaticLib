package de.janschuri.lunaticlib.senders.velocity;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.VelocityLunaticLib;
import de.janschuri.lunaticlib.futurerequests.requests.*;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;

import java.util.*;

public class PlayerSender extends AbstractPlayerSender {

    public PlayerSender(CommandSource sender) {
        super(((com.velocitypowered.api.proxy.Player) sender).getUniqueId());
    }

    public PlayerSender(UUID uuid) {
        super(uuid);
    }

    public PlayerSender(String name) {
        super(new GetUniqueIdRequest().get(name));
    }


    @Override
    public boolean sendMessage(String message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);

        if(player.isPresent()) {
            return new Sender(player.get()).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);

        if (player.isPresent()) {
            return new Sender(player.get()).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            return new Sender(player.get()).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> msg) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            return new Sender(player.get()).sendMessage(msg);
        }
        return false;
    }

    @Override
    public String getName() {
        return new GetNameRequest().get(uuid);
    }

    @Override
    public boolean chat(String message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            player.get().spoofChatInput(
                message
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean hasItemInMainHand() {
        return new HasItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public byte[] getItemInMainHand() {
        return new GetItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean removeItemInMainHand() {
        return new RemoveItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        return new GiveItemDropRequest().get(getServerName(), uuid, item);
    }

    @Override
    public boolean hasPermission(String permission) {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.hasPermission(permission)).orElse(false);
    }

    @Override
    public String getServerName() {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.getCurrentServer().get().getServerInfo().getName()).orElse(null);
    }

    @Override
    public double[] getPosition() {
        return new GetPositionRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean isOnline() {
        Collection<com.velocitypowered.api.proxy.Player> players = VelocityLunaticLib.getProxy().getAllPlayers();
        for (com.velocitypowered.api.proxy.Player player : players) {
            if (player.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        return new IsInRangeRequest().get(getServerName(), uuid, playerUUID, range);
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public boolean isSameServer(UUID player1UUID) {
        Optional<com.velocitypowered.api.proxy.Player> player1 = VelocityLunaticLib.getProxy().getPlayer(player1UUID);
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);

        return player1.isPresent() && player.isPresent() && player1.get().getCurrentServer().get().getServerInfo().getName().equals(player.get().getCurrentServer().get().getServerInfo().getName());
    }
}
