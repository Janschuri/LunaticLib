package de.janschuri.lunaticlib.platform.paper.sender;

import de.janschuri.lunaticlib.sender.LunaticSenderHandler;

public final class PaperSenderHandler {

    private PaperSenderHandler() {}

    public static void initialize(PaperSenderAdapter adapter) {
        LunaticSenderHandler.initialize(adapter);
    }

    public static void shutdown() {
        LunaticSenderHandler.shutdown();
    }

    public static PaperSenderAdapter getAdapter() {
        return (PaperSenderAdapter) LunaticSenderHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticSenderHandler.isEnabled();
    }
}