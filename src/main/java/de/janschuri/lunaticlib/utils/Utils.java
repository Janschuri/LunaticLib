package de.janschuri.lunaticlib.utils;

import de.janschuri.lunaticlib.config.Language;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static Timer timer = new Timer();
    public static Timer getTimer() {
        return timer;
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

    public static byte[] serializeItemStack(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack deserializeItemStack(byte[] data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
