package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.command.LunaticDecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.*;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.platform.velocity.VelocityLunaticLib;
import de.janschuri.lunaticlib.common.utils.Utils;
import net.kyori.adventure.inventory.Book;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public PlayerSenderImpl(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public PlayerSenderImpl(UUID uuid) {
        super(VelocityLunaticLib.getProxy().getPlayer(uuid).orElse(null));
        this.uuid = uuid;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return player.map(Player::getUsername).orElse(null);

    }

    @Override
    public String getSkinURL() {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            List<GameProfile.Property> properties = player.get().getGameProfile().getProperties();
            for (GameProfile.Property property : properties) {
                if (property.getName().equals("textures")) {
                    String value = property.getValue();
                    return Utils.getSkinURLFromValue(value);
                }
            }
        }
        return null;
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
    public CompletableFuture<Boolean> hasItemInMainHand() {
        return new HasItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public CompletableFuture<byte[]> getItemInMainHand() {
        return new GetItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public CompletableFuture<Boolean> removeItemInMainHand() {
        return new RemoveItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public CompletableFuture<Boolean> giveItemDrop(byte[] item) {
        return new GiveItemDropRequest().get(getServerName(), uuid, item);
    }

    @Override
    public String getServerName() {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.getCurrentServer().get().getServerInfo().getName()).orElse(null);
    }

    @Override
    public CompletableFuture<double[]> getPosition() {
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
    public CompletableFuture<Boolean> isInRange(UUID playerUUID, double range) {
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

    @Override
    public CompletableFuture<Boolean> openDecisionGUI(DecisionMessage message) {
        return new  OpenDecisionGUIRequest().get(getServerName(), uuid, message);
    }

    @Override
    public void runCommand(String command) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            CommandManager commandManager = VelocityLunaticLib.getProxy().getCommandManager();
            commandManager.executeAsync(player.get(), command);
        }
    }
}
