package de.janschuri.lunaticlib.proxyrequests.platform.velocity.impl.sender;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import de.janschuri.lunaticlib.proxyrequests.platform.velocity.VelocityLunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.VelocityPlayerSenderImpl;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import de.janschuri.lunaticlib.utils.Utils;

import java.util.*;

public class VelocityProxyRequestsPlayerSenderImpl extends VelocityPlayerSenderImpl implements ProxyRequestsPlayerSender {

    private final UUID uuid;

    public VelocityProxyRequestsPlayerSenderImpl(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public VelocityProxyRequestsPlayerSenderImpl(UUID uuid) {
        super(VelocityLunaticLibProxyRequests.getProxy().getPlayer(uuid).orElse(null));
        this.uuid = uuid;
    }

    @Override
    public String getSkinURL() {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLibProxyRequests.getProxy().getPlayer(uuid);
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
