package de.janschuri.lunaticlib.platform.bukkit.util;

import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ItemStackUtils {
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

    public static ItemStack getSkullFromURL(String url) {
        Logger.debugLog("Getting skull from URL: " + url);

        return new ItemStack(Material.PLAYER_HEAD);
    }

    public static ItemStack getSkullFromUUID(UUID uuid) {
        Logger.debugLog("Getting skull from UUID: " + uuid);
        return getSkullFromURL(getSkinURLFromUUID(uuid));
    }

    public static String getSkinURLFromUUID(UUID uuid) {
        return null;
    }


    public static String getKey(ItemStack itemStack){
        Material material = itemStack.getType();
        if(material.isBlock()){
            String id = material.getKey().getKey();
            return "block.minecraft."+id;
        } else if(material.isItem()){
            String id = material.getKey().getKey();
            return "item.minecraft."+id;
        }
        return "block.minecraft.dirt";
    }
}
