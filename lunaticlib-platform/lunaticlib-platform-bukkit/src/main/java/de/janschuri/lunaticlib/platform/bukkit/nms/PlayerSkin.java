package de.janschuri.lunaticlib.platform.bukkit.nms;

import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import org.bukkit.entity.Player;

public class PlayerSkin {

    private PlayerSkin() {
    }

    public static String[] getFromPlayer(Player playerBukkit) {
        return BukkitLunaticLib.getVersion().getPlayerSkin().getFromPlayer(playerBukkit);
    }

    public static String getSkinURL(Player playerBukkit) {
        String[] skin = getFromPlayer(playerBukkit);

        if (skin == null) {
            return null;
        }

        String value = skin[0];

        if (value == null) {
            return null;
        }

        return Utils.getSkinURLFromValue(value);
    }
}
