package de.janschuri.lunaticlib.proxyrequests.platform.waterfall.impl.sender;

import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.WaterfallLunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender.WaterfallPlayerSenderImpl;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ProxyRequestsPlayerSenderImpl extends WaterfallPlayerSenderImpl implements ProxyRequestsPlayerSender {

    private final UUID uuid;

    public ProxyRequestsPlayerSenderImpl(UUID uuid) {
        super(WaterfallLunaticLibProxyRequests.getPluginInstance().getProxy().getPlayer(uuid));
        this.uuid = uuid;
    }

    public ProxyRequestsPlayerSenderImpl(ProxiedPlayer player) {
        super(player);
        this.uuid = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getSkinURL() {
        String skinURL = WaterfallLunaticLibProxyRequests.getSkinCache(uuid);

        if (skinURL != null) {
            return skinURL;
        }

        return null;
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
    public double[] getPosition() {
        return new GetPositionRequest().get(getServerName(), uuid).thenApply(result -> result).join();
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        return new IsInRangeRequest().get(getServerName(), uuid, playerUUID, range).thenApply(result -> result).join();
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        return new OpenDecisionGUIRequest().get(getServerName(), uuid, message).thenApply(result -> result).join();
    }
}
