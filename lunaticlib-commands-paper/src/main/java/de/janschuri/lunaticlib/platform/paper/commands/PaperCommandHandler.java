package de.janschuri.lunaticlib.platform.paper.commands;

import de.janschuri.lunaticlib.commands.LunaticCommandHandler;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderHandler;

public final class PaperCommandHandler {

    private PaperCommandHandler() {}


    public static void initialize(PaperCommandAdapter adapter) {
        initialize(adapter, true);
    }

    public static void initialize(PaperCommandAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            PaperSenderHandler.initialize(adapter);
        }

        LunaticCommandHandler.initialize(adapter);
    }

    public static void shutdown() {
        shutdown(true);
    }

    public static void shutdown(boolean shutdownSenderHandler) {
        LunaticCommandHandler.shutdown();

        if (shutdownSenderHandler) {
            PaperSenderHandler.shutdown();
        }
    }

    public static PaperCommandAdapter getAdapter() {
        return (PaperCommandAdapter) LunaticCommandHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticCommandHandler.isEnabled();
    }
}