package de.janschuri.lunaticlib.nms.v1_20_R2;

import de.janschuri.lunaticlib.nms.PlayerSkin;
import de.janschuri.lunaticlib.nms.Version;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VersionImpl implements Version {

    @Override
    public PlayerSkin getPlayerSkin() {
        return new PlayerSkinImpl();
    }
}
