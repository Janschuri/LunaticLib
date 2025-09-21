package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.utils.SingletonHolder;

public final class LunaticCommandHandler {
    private LunaticCommandHandler() {}

    private static final SingletonHolder<CommandAdapter<?,?>> HOLDER = new SingletonHolder<>();

    public static void initialize(CommandAdapter<?,?> adapter) {
        HOLDER.initialize(adapter);
    }
    public static void shutdown() {
        HOLDER.shutdown();
    }
    public static CommandAdapter<?,?> getAdapter() {
        return HOLDER.get();
    }
    public static boolean isEnabled() {
        return HOLDER.isInitialized();
    }
}

