package de.janschuri.lunaticlib.proxyrequests.sender;

import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.utils.DecisionMessage;

import java.util.UUID;

public interface ProxyRequestsPlayerSender extends PlayerSender {


    public String getSkinURL();

    public boolean hasItemInMainHand();

    byte[] getItemInMainHand();

    public boolean removeItemInMainHand();

    public boolean giveItemDrop(byte[] item);

    public double[] getPosition();

    public boolean isInRange(UUID playerUUID, double range);

    public boolean openDecisionGUI(DecisionMessage message);
}
