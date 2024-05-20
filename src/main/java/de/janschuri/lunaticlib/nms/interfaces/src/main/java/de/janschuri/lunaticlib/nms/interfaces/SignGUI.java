package de.janschuri.lunaticlib.nms.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public interface SignGUI {
    void sendSign(Player p, Consumer<String[]> lines);

}
