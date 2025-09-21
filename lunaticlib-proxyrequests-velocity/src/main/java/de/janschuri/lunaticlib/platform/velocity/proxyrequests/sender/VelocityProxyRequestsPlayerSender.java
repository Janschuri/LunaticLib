package de.janschuri.lunaticlib.platform.velocity.proxyrequests.sender;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.VelocityProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocityPlayerSender;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import de.janschuri.lunaticlib.utils.Utils;

import java.util.*;

public class VelocityProxyRequestsPlayerSender extends VelocityPlayerSender implements ProxyRequestsPlayerSender<Player, CommandSource> {

    private final UUID uuid;

    public VelocityProxyRequestsPlayerSender(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    @Override
    public String getSkinURL() {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityProxyRequestsHandler.getAdapter().getProxy().getPlayer(uuid);
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
    public boolean isInRange(ProxyRequestsPlayerSender playerSender, double range) {
        return new IsInRangeRequest().get(getServerName(), uuid, playerSender.getUniqueId(), range).thenApply(result -> result).join();
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        return new OpenDecisionGUIRequest().get(getServerName(), uuid, message).thenApply(result -> result).join();
    }
}
