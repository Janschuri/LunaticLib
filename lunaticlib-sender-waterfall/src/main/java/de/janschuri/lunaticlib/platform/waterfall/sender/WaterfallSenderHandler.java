package de.janschuri.lunaticlib.platform.waterfall.sender;

import de.janschuri.lunaticlib.platform.waterfall.sender.external.AdventureAPI;
import de.janschuri.lunaticlib.sender.LunaticSenderHandler;
import net.md_5.bungee.api.plugin.Plugin;

public final class WaterfallSenderHandler {

    private static WaterfallAdapter adapter;

    public static void enable(Plugin plugin) {
        if (adapter == null) {
            synchronized (WaterfallSenderHandler.class) {
                if (adapter == null) {
                    adapter = new WaterfallAdapter();
                    LunaticSenderHandler.enable(adapter);
                }
            }
        }

        AdventureAPI.initialize(plugin);
    }

    public static void disable() {
        AdventureAPI.close();
    }

    public static WaterfallAdapter adapter() {
        return adapter;
    }
}