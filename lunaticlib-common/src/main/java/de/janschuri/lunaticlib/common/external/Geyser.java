package de.janschuri.lunaticlib.common.external;

import de.janschuri.lunaticlib.common.LunaticLib;

import java.util.UUID;

public class Geyser {

    public static boolean isBedrockPlayer(UUID uuid) {
        if (!LunaticLib.isInstalledGeyser()) {
            return false;
        } else {
            return org.geysermc.api.Geyser.api().isBedrockPlayer(uuid);
        }
    }
}
