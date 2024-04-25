package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticLib extends JavaPlugin {

    private static final String IDENTIFIER = "lunaticlib:proxy";
    private static LunaticLib instance;

        @Override
        public void onEnable() {
            instance = this;
        }

        @Override
        public void onDisable() {
            // Plugin shutdown logic
        }

    public static LunaticLib getInstance() {
        return instance;
    }

    public static void sendPluginMessage(byte[] message) {
        getInstance().getServer().sendPluginMessage(getInstance(), IDENTIFIER, message);
    }
}
