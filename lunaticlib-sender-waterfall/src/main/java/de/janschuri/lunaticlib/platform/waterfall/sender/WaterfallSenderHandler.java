package de.janschuri.lunaticlib.platform.waterfall.sender;

import de.janschuri.lunaticlib.platform.waterfall.sender.external.AdventureAPI;
import de.janschuri.lunaticlib.sender.LunaticSenderHandler;

public final class WaterfallSenderHandler {

    private WaterfallSenderHandler() {}

    public static void initialize(WaterfallSenderAdapter adapter) {
        LunaticSenderHandler.initialize(adapter);
        AdventureAPI.initialize(adapter.getPlugin());
    }

    public static void shutdown() {
        AdventureAPI.close();
        LunaticSenderHandler.shutdown();
    }

    public static WaterfallSenderAdapter getAdapter() {
        return (WaterfallSenderAdapter) LunaticSenderHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticSenderHandler.isEnabled();
    }
}