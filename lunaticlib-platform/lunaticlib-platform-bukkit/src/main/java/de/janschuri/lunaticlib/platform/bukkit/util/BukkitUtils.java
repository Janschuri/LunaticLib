package de.janschuri.lunaticlib.platform.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class BukkitUtils {

    public static boolean isInRange(Location location, Location otherLocation, double range) {
        return location.distance(otherLocation) <= range;
    }
}
