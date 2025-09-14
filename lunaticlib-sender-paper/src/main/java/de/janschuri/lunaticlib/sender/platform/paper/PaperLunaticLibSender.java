package de.janschuri.lunaticlib.sender.platform.paper;

import de.janschuri.lunaticlib.sender.LunaticLibSender;
import de.janschuri.lunaticlib.sender.platform.paper.impl.PaperSenderPlatformImpl;

public final class PaperLunaticLibSender extends LunaticLibSender {

    private static PaperSenderPlatformImpl platform;

    public static void enable() {
        if (getPlatform() == null) {
            platform = new PaperSenderPlatformImpl();
            setPlatform(platform);
        }
    }

    public static PaperSenderPlatformImpl getPlatformImpl() {
        if (platform == null) {
            throw new IllegalStateException("PaperLunaticLibSender platform is not set. Please call PaperLunaticLibSender.enable() first.");
        }

        return platform;
    }
}