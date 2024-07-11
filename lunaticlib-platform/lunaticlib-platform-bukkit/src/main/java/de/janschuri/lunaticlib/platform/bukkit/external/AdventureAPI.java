package de.janschuri.lunaticlib.platform.bukkit.external;

import net.kyori.adventure.Adventure;
import net.kyori.adventure.internal.properties.AdventureProperties;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AdventureAPI {

    private static BukkitAudiences bukkitAudiences;

    public static void initialize(JavaPlugin plugin) {
        bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public static void close() {
        if (bukkitAudiences != null) {
            bukkitAudiences.close();
        }
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        if (bukkitAudiences != null) {
            bukkitAudiences.sender(sender).sendMessage(
                    Component.text(message)
            );
            return true;
        }
        return false;
    }

    public static boolean sendMessage(CommandSender sender, Component message) {
        if (bukkitAudiences != null) {
            bukkitAudiences.sender(sender).sendMessage(message);
            return true;
        }
        return false;
    }

    public static boolean sendBook(CommandSender sender, Book.Builder book) {
        if (bukkitAudiences != null) {
            bukkitAudiences.sender(sender).openBook(book.build());
            return true;
        }
        return false;
    }
}
