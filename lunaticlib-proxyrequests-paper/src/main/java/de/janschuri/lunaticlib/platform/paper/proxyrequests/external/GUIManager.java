package de.janschuri.lunaticlib.platform.paper.proxyrequests.external;

import de.janschuri.lunaticlib.inventorygui.guis.DecisionGUI;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import org.bukkit.entity.Player;

public class GUIManager {

    private GUIManager() {}

    public static void openDecisionGUI(DecisionMessage message, Player player) {
        de.janschuri.lunaticlib.inventorygui.handler.GUIManager.openGUI(new DecisionGUI(message), player);
    }
}
