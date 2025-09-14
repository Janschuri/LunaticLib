package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.commands.platform.Platform;

public class LunaticLibCommands {

    protected LunaticLibCommands(){}

    private static Platform<?, ?> platform;

    protected static void setPlatform(Platform<?, ?> platform) {
        LunaticLibCommands.platform = platform;
    }

    public static Platform<?, ?> getPlatform() {
        if (platform == null) {
            throw new IllegalStateException("LunaticCommands platform is not set. Please call LunaticCommands.enable() first.");
        }
        return platform;
    }
}
