package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.command.LunaticDecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.GetSkinURLRequest;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.futurerequests.requests.OpenDecisionGUIRequest;
import de.janschuri.lunaticlib.platform.velocity.VelocityLunaticLib;
import de.janschuri.lunaticlib.common.utils.Utils;
import net.kyori.adventure.inventory.Book;

import java.util.*;

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
        if (player.isPresent()) {
            return player.get().getUsername();
        }

        return new de.janschuri.lunaticlib.common.futurerequests.requests.GetNameRequest().get(uuid);
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
        } else {
            return new GetSkinURLRequest().get(uuid);
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
    public boolean hasItemInMainHand() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.HasItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public byte[] getItemInMainHand() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.GetItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean removeItemInMainHand() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.RemoveItemInMainHandRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.GiveItemDropRequest().get(getServerName(), uuid, item);
    }

    @Override
    public String getServerName() {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.getCurrentServer().get().getServerInfo().getName()).orElse(null);
    }

    @Override
    public double[] getPosition() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.GetPositionRequest().get(getServerName(), uuid);
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
        return new de.janschuri.lunaticlib.common.futurerequests.requests.IsInRangeRequest().get(getServerName(), uuid, playerUUID, range);
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
    public boolean openBook(Book.Builder book) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            player.get().openBook(book.build());
            return true;
        }
        return false;
    }

    @Override
    public boolean closeBook() {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            return new de.janschuri.lunaticlib.common.futurerequests.requests.CloseBookRequest().get(getServerName(), uuid);
        }
        return false;
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
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
