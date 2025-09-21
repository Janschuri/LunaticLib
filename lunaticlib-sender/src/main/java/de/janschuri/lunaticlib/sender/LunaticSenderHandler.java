package de.janschuri.lunaticlib.sender;

import de.janschuri.lunaticlib.utils.SingletonHolder;

public class LunaticSenderHandler {

    private LunaticSenderHandler() {}

    private static final SingletonHolder<SenderAdapter<?>> HOLDER = new SingletonHolder<>();

    public static void initialize(SenderAdapter<?> adapter) {
        HOLDER.initialize(adapter);
    }

    public static void shutdown() {
        HOLDER.shutdown();
    }

    public static SenderAdapter getAdapter() {
        return HOLDER.get();
    }

    public static boolean isEnabled() {
        return HOLDER.isInitialized();
    }
}
