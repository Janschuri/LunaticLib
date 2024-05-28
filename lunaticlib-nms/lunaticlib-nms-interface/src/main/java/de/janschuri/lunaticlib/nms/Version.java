package de.janschuri.lunaticlib.nms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public interface Version {

    PacketHandler getPacketHandler(JavaPlugin plugin, Player p);
    SignGUI getSignGUI();
    PlayerSkin getPlayerSkin();
}
