package de.janschuri.lunaticlib.utils;

import de.janschuri.lunaticlib.BungeeLunaticLib;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.PaperLunaticLib;
import de.janschuri.lunaticlib.VelocityLunaticLib;

import java.awt.*;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static boolean scheduleTask(Runnable task, long delay, TimeUnit unit) {
        try {
            executor.schedule(task, delay, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUUID(String input) {
        Pattern UUID_PATTERN = Pattern.compile(
                "^([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12}$");
        return UUID_PATTERN.matcher(input).matches();
    }

    public static double[] getPositionBetweenLocations(double[] loc1, double[] loc2) {
        double[] midpoint = new double[3];
        midpoint[0] = (loc1[0] + loc2[0]) / 2;
        midpoint[1] = (loc1[1] + loc2[1]) / 2;
        midpoint[2] = (loc1[2] + loc2[2]) / 2;
        return midpoint;
    }

    public static boolean isValidHexCode(String hexCode) {
        Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher matcher = pattern.matcher(hexCode);
        return matcher.matches();
    }

    public static Color hexToColor(String hexCode) {
        // Check if the hex string is in a valid format
        if (!isValidHexCode(hexCode)) {
            throw new IllegalArgumentException("Invalid hex color code");
        }

        // Remove '#' if present and extract RGB values
        String hexDigits = hexCode.substring(1); // Remove '#'
        int r, g, b;

        try {
            if (hexDigits.length() == 3) {
                // Handle short format (e.g., #RGB)
                r = Integer.parseInt(hexDigits.substring(0, 1), 16) * 17; // Convert and expand to full range
                g = Integer.parseInt(hexDigits.substring(1, 2), 16) * 17;
                b = Integer.parseInt(hexDigits.substring(2, 3), 16) * 17;
            } else if (hexDigits.length() == 6) {
                // Handle long format (e.g., #RRGGBB)
                r = Integer.parseInt(hexDigits.substring(0, 2), 16);
                g = Integer.parseInt(hexDigits.substring(2, 4), 16);
                b = Integer.parseInt(hexDigits.substring(4, 6), 16);
            } else {
                throw new IllegalArgumentException("Invalid hex color code");
            }

            // Create and return the Color object
            return new Color(r, g, b);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color code", e);
        }
    }

    public static boolean classExists(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (ClassNotFoundException|NullPointerException e) {
            return false;
        }
    }

    public static void sendConsoleCommand(String command) {

        switch (LunaticLib.getPlatform()) {
            case VELOCITY:
                VelocityLunaticLib.sendConsoleCommand(command);
                break;
            case BUNGEE:
                BungeeLunaticLib.sendConsoleCommand(command);
                break;
            case PAPER:
                PaperLunaticLib.sendConsoleCommand(command);
                break;
        }
    }

    public static String getSkinURLFromValue(String base64TextureValue) {

        // Decode the base64 texture value
        byte[] decodedBytes = Base64.getDecoder().decode(base64TextureValue);
        String decodedString = new String(decodedBytes);

        // Extract skin URL and signature from decoded string
        String[] parts = decodedString.split("\"");
        return parts[19];
    }
}
