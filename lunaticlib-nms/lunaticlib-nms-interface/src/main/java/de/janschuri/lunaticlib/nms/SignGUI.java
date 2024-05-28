package de.janschuri.lunaticlib.nms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public interface SignGUI {

    public void sendSign(JavaPlugin plugin, Player p, Consumer<String[]> lines);
}