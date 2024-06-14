package de.janschuri.lunaticlib.platform.bungee.external;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

public class AdventureAPI {

    private static BungeeAudiences bungeeAudiences;

    public static void initialize(Plugin plugin) {
        bungeeAudiences = BungeeAudiences.create(plugin);
    }

    public static void close() {
        if (bungeeAudiences != null) {
            bungeeAudiences.close();
        }
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        if (bungeeAudiences != null) {
            bungeeAudiences.sender(sender).sendMessage(
                    Component.text(message)
            );
            return true;
        }
        return false;
    }

    public static boolean sendMessage(CommandSender sender, Component message) {
        if (bungeeAudiences != null) {
            bungeeAudiences.sender(sender).sendMessage(message);
            return true;
        }
        return false;
    }

    public static boolean sendBook(CommandSender sender, Book.Builder book) {
        if (bungeeAudiences != null) {
            bungeeAudiences.sender(sender).openBook(book.build());
            return true;
        }
        return false;
    }
}
