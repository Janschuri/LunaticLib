package de.janschuri.lunaticlib.nms.v1_20_R2;

import de.janschuri.lunaticlib.nms.PacketHandler;
import de.janschuri.lunaticlib.nms.PlayerSkin;
import de.janschuri.lunaticlib.nms.SignGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class VersionImpl implements de.janschuri.lunaticlib.nms.Version {


    @Override
    public PacketHandler getPacketHandler(JavaPlugin plugin, Player p) {
        return new PacketHandlerImpl(plugin, p);
    }

    @Override
    public SignGUI getSignGUI() {
        return new SignGUIImpl();
    }

    @Override
    public PlayerSkin getPlayerSkin() {
        return new PlayerSkinImpl();
    }
}
