package de.janschuri.lunaticlib.platform.paper.utils;

import de.janschuri.lunaticlib.utils.Utils;
import org.bukkit.Location;

public class PaperUtils extends Utils {
    public static boolean isInRange(Location location, Location otherLocation, double range) {
        return location.distance(otherLocation) <= range;
    }
}
