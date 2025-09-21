package de.janschuri.lunaticlib.platform.waterfall.commands;

import de.janschuri.lunaticlib.commands.LunaticCommandHandler;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderHandler;
import de.janschuri.lunaticlib.utils.SingletonHolder;

import java.util.Objects;

public final class WaterfallCommandHandler {

    private WaterfallCommandHandler() {}

    private static final SingletonHolder<WaterfallCommandAdapter> HOLDER = new SingletonHolder<>();

    public static void initialize(WaterfallCommandAdapter adapter) {
        initialize(adapter, true);
    }
    public static void initialize(WaterfallCommandAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            WaterfallSenderHandler.initialize(adapter);
        }

        LunaticCommandHandler.initialize(adapter);
    }

    public static void shutdown() {
        shutdown(true);
    }

    public static void shutdown(boolean shutdownSenderHandler) {
        LunaticCommandHandler.shutdown();

        if (shutdownSenderHandler) {
            WaterfallSenderHandler.shutdown();
        }
    }

    public static WaterfallCommandAdapter getAdapter() {
        return (WaterfallCommandAdapter) LunaticCommandHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticCommandHandler.isEnabled();
    }
}