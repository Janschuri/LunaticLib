package de.janschuri.lunaticlib.platform.bungee.external;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

public class AdventureAPI {

    private static BungeeAudiences bungeeAudiences;

    // Initialize Adventure API
    public static void initialize(Plugin plugin) {
        bungeeAudiences = BungeeAudiences.create(plugin);
    }

    // Close Adventure API
    public static void close() {
        if (bungeeAudiences != null) {
            bungeeAudiences.close();
        }
    }

    // Send message to player
    public static boolean sendMessage(CommandSender sender, String message) {
        if (bungeeAudiences != null) {
            bungeeAudiences.sender(sender).sendMessage(
                    Component.text(message)
            );
            return true;
        }
        return false;
    }

    // Send formatted message to player
    public static boolean sendMessage(CommandSender sender, Component message) {
        if (bungeeAudiences != null) {
            bungeeAudiences.sender(sender).sendMessage(message);
            return true;
        }
        return false;
    }
}
