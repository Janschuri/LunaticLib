package de.janschuri.lunaticlib.platform.bungee.sender;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.*;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bungee.BungeeLunaticLib;
import de.janschuri.lunaticlib.PlayerSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

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
    public boolean hasItemInMainHand() {
        return new HasItemInMainHandRequest().get(getServerName(), uuid).thenApply(result -> result).join();
    }

    @Override
    public byte[] getItemInMainHand() {
        return new GetItemInMainHandRequest().get(getServerName(), uuid).thenApply(result -> result).join();
    }

    @Override
    public boolean removeItemInMainHand() {
        return new RemoveItemInMainHandRequest().get(getServerName(), uuid).thenApply(result -> result).join();
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        return new GiveItemDropRequest().get(getServerName(), uuid, item).thenApply(result -> result).join();
    }

    @Override
    public String getServerName() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }

    @Override
    public double[] getPosition() {
        return new GetPositionRequest().get(getServerName(), uuid).thenApply(result -> result).join();
    }

    @Override
    public boolean isOnline() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid) != null;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        return new IsInRangeRequest().get(getServerName(), uuid, playerUUID, range).thenApply(result -> result).join();
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
    public boolean openDecisionGUI(DecisionMessage message) {
        return new OpenDecisionGUIRequest().get(getServerName(), uuid, message).thenApply(result -> result).join();
    }

    @Override
    public void runCommand(String command) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat("/" + command);
    }
}
