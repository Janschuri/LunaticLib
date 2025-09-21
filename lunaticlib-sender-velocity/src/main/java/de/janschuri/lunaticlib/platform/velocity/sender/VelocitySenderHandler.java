package de.janschuri.lunaticlib.platform.velocity.sender;

import de.janschuri.lunaticlib.sender.LunaticSenderHandler;

public final class VelocitySenderHandler {

    private VelocitySenderHandler() {}

    public static void initialize(VelocitySenderAdapter adapter) {
        LunaticSenderHandler.initialize(adapter);
    }

    public static void shutdown() {
        LunaticSenderHandler.shutdown();
    }

    public static VelocitySenderAdapter getAdapter() {
        return (VelocitySenderAdapter) LunaticSenderHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticSenderHandler.isEnabled();
    }
}