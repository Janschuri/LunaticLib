package de.janschuri.lunaticlib.nms.v1_20_1;

import de.janschuri.lunaticlib.nms.PlayerSkin;
import de.janschuri.lunaticlib.nms.Version;

public class VersionImpl implements Version {

    @Override
    public PlayerSkin getPlayerSkin() {
        return new PlayerSkinImpl();
    }
}
