package de.janschuri.lunaticlib.sender;

import de.janschuri.lunaticlib.sender.platform.SenderPlatform;

public class LunaticLibSender {

    protected LunaticLibSender(){}

    private static SenderPlatform<?, ?> platform;

    protected static void setPlatform(SenderPlatform<?, ?> platform) {
        LunaticLibSender.platform = platform;
    }

    public static SenderPlatform<?, ?> getPlatform() {
        if (platform == null) {
            throw new IllegalStateException("LunaticCommands platform is not set. Please call LunaticCommands.enable() first.");
        }
        return platform;
    }
}
