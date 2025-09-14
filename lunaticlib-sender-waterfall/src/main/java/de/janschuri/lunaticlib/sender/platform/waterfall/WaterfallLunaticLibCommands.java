package de.janschuri.lunaticlib.sender.platform.waterfall;

import de.janschuri.lunaticlib.sender.LunaticLibSender;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.PlatformImpl;
import net.md_5.bungee.api.plugin.Plugin;

public final class WaterfallLunaticLibCommands extends LunaticLibSender {

    private static PlatformImpl platform;
    private static Plugin plugin;

    public static void enable(Plugin pluginInstance) {
        if (platform == null) {
            platform = new PlatformImpl();
            setPlatform(platform);
        }

        WaterfallLunaticLibCommands.plugin = pluginInstance;
    }

    public static Plugin getPluginInstance() {
        return WaterfallLunaticLibCommands.plugin;
    }
}