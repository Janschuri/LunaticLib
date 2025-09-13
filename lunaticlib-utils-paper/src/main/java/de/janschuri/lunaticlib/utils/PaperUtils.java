package de.janschuri.lunaticlib.utils;

import org.bukkit.Location;

public class PaperUtils extends Utils {
    public static boolean isInRange(Location location, Location otherLocation, double range) {
        return location.distance(otherLocation) <= range;
    }
}
