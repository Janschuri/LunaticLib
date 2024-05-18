package de.janschuri.lunaticlib.external;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AdventureAPI {

    private static BukkitAudiences bukkitAudiences;

    // Initialize Adventure API
    public static void initialize(JavaPlugin plugin) {
        bukkitAudiences = BukkitAudiences.create(plugin);
    }

    // Close Adventure API
    public static void close() {
        if (bukkitAudiences != null) {
            bukkitAudiences.close();
        }
    }

    // Send message to player
    public static void sendMessage(CommandSender sender, String message) {
        if (bukkitAudiences != null) {
            bukkitAudiences.sender(sender).sendMessage(
                    Component.text(message).color(NamedTextColor.GOLD)
            );
        }
    }

    // Send formatted message to player
    public static void sendMessage(CommandSender sender, Component message) {
        if (bukkitAudiences != null) {
            bukkitAudiences.sender(sender).sendMessage(message);
        }
    }
}
