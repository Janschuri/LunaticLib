package de.janschuri.lunaticlib.platform.bungee;

import de.janschuri.lunaticlib.platform.bungee.external.AdventureAPI;
import de.janschuri.lunaticlib.platform.bungee.external.Metrics;
import de.janschuri.lunaticlib.platform.Platform;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.utils.Mode;
import de.janschuri.lunaticlib.platform.bungee.listener.MessageListener;
import de.janschuri.lunaticlib.platform.bungee.listener.PostLoginListener;
import net.md_5.bungee.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class BungeeLunaticLib extends Plugin {
    private static BungeeLunaticLib instance;
    private static Map<UUID, String> SKIN_CACHE = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getProxy().registerChannel(IDENTIFIER);

        int pluginId = 21919;
        Metrics metrics = new Metrics(this, pluginId);

        Mode mode = Mode.PROXY;
        Platform platform = new PlatformImpl();
        Path dataDirectory = getDataFolder().toPath();
        AdventureAPI.initialize(this);
        getProxy().getPluginManager().registerListener(this, new MessageListener());
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());

        LunaticLib.onEnable(dataDirectory, mode, platform);
    }

    @Override
    public void onDisable() {
        AdventureAPI.close();
        LunaticLib.onDisable();
    }

    public static BungeeLunaticLib getInstance() {
        return instance;
    }

    public static String getSkinCache(UUID uuid) {
        return SKIN_CACHE.get(uuid);
    }

    public static void setSkinCache(UUID uuid, String skin) {
        SKIN_CACHE.put(uuid, skin);
    }
}
