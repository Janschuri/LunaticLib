package de.janschuri.lunaticlib.senders.bungee;

import de.janschuri.lunaticlib.BungeeLunaticLib;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import de.janschuri.lunaticlib.utils.FutureRequests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class PlayerSender extends AbstractPlayerSender {

    public PlayerSender(UUID uuid) {
        super(uuid);
    }

    public PlayerSender(String name) {
        super(FutureRequests.getUniqueId(name));
    }

    public PlayerSender(CommandSender sender) {
        super(((ProxiedPlayer) sender).getUniqueId());
    }

    @Override
    public String getName() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getName();
    }

    @Override
    public boolean chat(String message) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat(message);
        return true;
    }

    @Override
    public boolean hasItemInMainHand() {
        return FutureRequests.hasItemInMainHand(uuid);
    }

    @Override
    public byte[] getItemInMainHand() {
        return FutureRequests.getItemInMainHand(uuid);
    }

    @Override
    public boolean removeItemInMainHand() {
        return FutureRequests.removeItemInMainHand(uuid);
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        return FutureRequests.giveItemDrop(uuid, item);
    }

    @Override
    public boolean hasPermission(String permission) {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).hasPermission(permission);
    }

    @Override
    public boolean sendMessage(String message) {
        return new Sender(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid)).sendMessage(message);
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        return new Sender(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid)).sendMessage(message);
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        return new Sender(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid)).sendMessage(message);
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> msg) {
        return new Sender(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid)).sendMessage(msg);
    }

    @Override
    public String getServerName() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }

    @Override
    public double[] getPosition() {
        return FutureRequests.getPosition(uuid);
    }

    @Override
    public boolean isOnline() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid) != null;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        return FutureRequests.isInRange(uuid, playerUUID, range);
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName().equals(getServerName());
    }
}
