package de.janschuri.lunaticlib.proxyrequests.sender;

import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.utils.DecisionMessage;

import java.util.UUID;

public interface ProxyRequestsPlayerSender<P extends C, C> extends PlayerSender<P, C> {

    String getSkinURL();

    boolean hasItemInMainHand();

    byte[] getItemInMainHand();

    boolean removeItemInMainHand();

    boolean giveItemDrop(byte[] item);

    double[] getPosition();

    boolean isInRange(ProxyRequestsPlayerSender playerSender, double range);

    boolean openDecisionGUI(DecisionMessage message);
}
