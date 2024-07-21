package de.janschuri.lunaticlib.platform.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class BukkitUtils {

    public static long serializeCoords(Location location) {
        return (long) location.getBlockX() << 38 | (long) location.getBlockY() << 26 | (long) location.getBlockZ();
    }

    public static Location deserializeCoords(long coords, UUID worldUUID) {
        int x = (int) (coords >> 38);
        int y = (int) (coords >> 26 & 0xFFF);
        int z = (int) (coords & 0xFFF);
        World world = Bukkit.getWorld(worldUUID);
        return new Location(world, x, y, z);
    }

    public static boolean isInRange(Location location, Location otherLocation, double range) {
        return location.distance(otherLocation) <= range;
    }
}
