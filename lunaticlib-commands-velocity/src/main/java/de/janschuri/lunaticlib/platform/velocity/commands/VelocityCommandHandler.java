package de.janschuri.lunaticlib.platform.velocity.commands;

import de.janschuri.lunaticlib.commands.LunaticCommandHandler;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderAdapter;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderHandler;
import de.janschuri.lunaticlib.sender.LunaticSenderHandler;

public final class VelocityCommandHandler {

    private VelocityCommandHandler() {}

    public static void initialize(VelocityCommandAdapter adapter) {
        initialize(adapter, true);
    }

    public static void initialize(VelocityCommandAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            LunaticSenderHandler.initialize(adapter);
        }

        LunaticCommandHandler.initialize(adapter);
    }

    public static void shutdown() {
        shutdown(true);
    }


    public static void shutdown(boolean shutdownSenderHandler) {
        LunaticCommandHandler.shutdown();

        if (shutdownSenderHandler) {
            VelocitySenderHandler.shutdown();
        }
    }

    public static VelocityCommandAdapter getAdapter() {
        return (VelocityCommandAdapter) LunaticCommandHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticCommandHandler.isEnabled();
    }
}