package de.janschuri.lunaticlib.nms.v1_21_1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.nms.PlayerSkin;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class PlayerSkinImpl implements PlayerSkin {

    public String[] getFromPlayer(Player playerBukkit) {
        net.minecraft.world.entity.player.Player playerNMS = ((CraftPlayer) playerBukkit).getHandle();
        GameProfile profile = playerNMS.getGameProfile();
        try {
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.value();
            String signature = property.signature();
            return new String[] {texture, signature};
        } catch (NoSuchElementException e) {
            Logger.debugLog("No skin found for player " + playerBukkit.getName() + " (" + playerBukkit.getUniqueId() + ").");
            return null;
        }
    }

}
