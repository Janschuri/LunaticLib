package de.janschuri.lunaticlib.platform.bungee.sender;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.futurerequests.requests.GetSkinURLRequest;
import de.janschuri.lunaticlib.common.futurerequests.requests.OpenDecisionGUIRequest;
import de.janschuri.lunaticlib.platform.bungee.BungeeLunaticLib;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.platform.bungee.external.AdventureAPI;
import net.kyori.adventure.inventory.Book;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Optional;
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
        return new GetSkinURLRequest().get(uuid);
    }

    @Override
    public boolean chat(String message) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat(message);
        return true;
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
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }

    @Override
    public double[] getPosition() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.GetPositionRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean isOnline() {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid) != null;
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
    public boolean isSameServer(UUID uuid) {
        return BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName().equals(getServerName());
    }

    @Override
    public boolean openBook(Book.Builder book) {
        return AdventureAPI.sendBook(BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid), book);
    }

    @Override
    public boolean closeBook() {
        return new de.janschuri.lunaticlib.common.futurerequests.requests.CloseBookRequest().get(getServerName(), uuid);
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        return new OpenDecisionGUIRequest().get(getServerName(), uuid, message);
    }

    @Override
    public void runCommand(String command) {
        BungeeLunaticLib.getInstance().getProxy().getPlayer(uuid).chat("/" + command);
    }
}
