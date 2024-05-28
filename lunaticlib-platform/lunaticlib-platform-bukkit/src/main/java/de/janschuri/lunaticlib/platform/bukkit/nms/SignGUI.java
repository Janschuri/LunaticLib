package de.janschuri.lunaticlib.platform.bukkit.nms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class SignGUI {

    public static void sendSign(JavaPlugin plugin, Player p, Consumer<String[]> lines) {
        Version.getSignGUI().sendSign(plugin, p, lines);
    }
}
