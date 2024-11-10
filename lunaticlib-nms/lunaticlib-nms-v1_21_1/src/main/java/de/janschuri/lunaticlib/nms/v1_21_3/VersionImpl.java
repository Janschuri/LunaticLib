package de.janschuri.lunaticlib.nms.v1_21_3;

import de.janschuri.lunaticlib.nms.PlayerSkin;
import de.janschuri.lunaticlib.nms.Version;

public class VersionImpl implements Version {

    @Override
    public PlayerSkin getPlayerSkin() {
        return new PlayerSkinImpl();
    }
}
