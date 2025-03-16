package de.janschuri.lunaticlib.platform.bungee.sender;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.command.LunaticDecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.*;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bungee.BungeeLunaticLib;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.platform.bungee.external.AdventureAPI;
import net.kyori.adventure.inventory.Book;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public PlayerSenderImpl(UUID uuid) {
        super(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid));
        this.uuid = uuid;
    }

    public PlayerSenderImpl(ProxiedPlayer player) {
        super(player);
        this.uuid = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getName();
    }

    @Override
    public String getSkinURL() {
        String skinURL = BungeeLunaticLib.getSkinCache(uuid);
        Logger.debugLog(String.format("SkinURL from cache: %s", skinURL));
        if (skinURL != null) {
            Logger.debugLog(String.format("SkinURL from cache: %s", skinURL));
            return skinURL;
        }

        return null;
    }

    @Override
    public boolean chat(String message) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat(message);
        return true;
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
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }

    @Override
    public CompletableFuture<double[]> getPosition() {
        return new GetPositionRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean isOnline() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid) != null;
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
    public boolean isSameServer(UUID uuid) {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName().equals(getServerName());
    }

    @Override
    public CompletableFuture<Boolean> openDecisionGUI(DecisionMessage message) {
        return new OpenDecisionGUIRequest().get(getServerName(), uuid, message);
    }

    @Override
    public void runCommand(String command) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat("/" + command);
    }
}
