package de.janschuri.lunaticlib.platform.bukkit.nms;

import de.janschuri.lunaticlib.common.utils.Utils;
import org.bukkit.entity.Player;

public class PlayerSkin {

    public static String[] getFromPlayer(Player playerBukkit) {
        return Version.getPlayerSkin().getFromPlayer(playerBukkit);
    }

    public static String getSkinURL(Player playerBukkit) {
        return Utils.getSkinURLFromValue(getFromPlayer(playerBukkit)[0]);
    }
}
