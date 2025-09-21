package de.janschuri.lunaticlib.platform.paper;

import de.janschuri.lunaticlib.platform.paper.commands.PaperCommandAdapter;
import de.janschuri.lunaticlib.platform.paper.commands.PaperCommandHandler;
import de.janschuri.lunaticlib.platform.paper.inventorygui.PaperInventoryGUIHandler;
import de.janschuri.lunaticlib.platform.paper.proxyrequests.PaperProxyRequestsAdapter;
import de.janschuri.lunaticlib.platform.paper.proxyrequests.PaperProxyRequestsHandler;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderAdapter;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperLunaticLib extends JavaPlugin {

    private static PaperLunaticLib instance;

    @Override
    public void onEnable() {
        instance = this;

        PaperSenderAdapter senderAdapter = new PaperSenderAdapter();
        PaperSenderHandler.initialize(senderAdapter);

        PaperCommandAdapter commandAdapter = new PaperCommandAdapter();
        PaperCommandHandler.initialize(commandAdapter, false);

        PaperProxyRequestsAdapter proxyRequestsAdapter = new PaperProxyRequestsAdapter(this);
        PaperProxyRequestsHandler.initialize(proxyRequestsAdapter, false);

        PaperInventoryGUIHandler.enable(this);

        int pluginId = 21913;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public static PaperLunaticLib getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        PaperSenderHandler.shutdown();
        PaperCommandHandler.shutdown();
        PaperProxyRequestsHandler.shutdown();
        PaperInventoryGUIHandler.shutdown();
    }
}
