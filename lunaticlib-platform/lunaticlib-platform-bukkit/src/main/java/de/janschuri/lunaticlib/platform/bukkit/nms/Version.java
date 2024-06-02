package de.janschuri.lunaticlib.platform.bukkit.nms;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.nms.PacketHandler;
import de.janschuri.lunaticlib.nms.PlayerSkin;
import de.janschuri.lunaticlib.nms.SignGUI;
import de.janschuri.lunaticlib.nms.VersionEnum;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Version {

    private Version() {
    }

    private static de.janschuri.lunaticlib.nms.Version getVersion() {

        VersionEnum version = BukkitLunaticLib.getServerVersion();
        Logger.debugLog("Server version: " + version);

        switch (version) {
            case v1_20_R1:
                return new de.janschuri.lunaticlib.nms.v1_20_R1.VersionImpl();
            case v1_20_R2:
                return new de.janschuri.lunaticlib.nms.v1_20_R2.VersionImpl();
            case v1_20_R3, v1_20_R4:
                return new de.janschuri.lunaticlib.nms.v1_20_R3.VersionImpl();
            case v1_20_R5, v1_20_R6:
                return new de.janschuri.lunaticlib.nms.v1_20_R5.VersionImpl();
            default:
                return null;
        }
    }

    public static PacketHandler getPacketHandler(JavaPlugin plugin, Player p) {
        return getVersion().getPacketHandler(plugin, p);
    }

    public static SignGUI getSignGUI() {
        return getVersion().getSignGUI();
    }

    public static PlayerSkin getPlayerSkin() {
        return getVersion().getPlayerSkin();
    }
}
