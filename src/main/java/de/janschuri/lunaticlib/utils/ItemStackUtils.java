package de.janschuri.lunaticlib.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.janschuri.lunaticlib.logger.Logger;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
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

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull.clone();
    }

    public static ItemStack getSkullFromUUID(UUID uuid) {
        Logger.debugLog("Getting skull from UUID: " + uuid);

        return getSkullFromURL(getSkinURLFromUUID(uuid));
    }

    public static String getSkinURLFromUUID(UUID uuid) {
        Logger.debugLog("Getting skull from UUID: " + uuid);
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            Logger.debugLog("Player is offline. Could not get skin URL.");
            return null;
        }

        GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());
        Collection<Property> property = profile.getProperties().get("textures");

        if (property != null) {
            Logger.debugLog("Player has textures. Getting skin URL.");
            Logger.debugLog("Properties: " + property.size());
            Logger.debugLog("Properties: " + property);
            for (Property prop : property) {
                Logger.debugLog("Property: " + prop.getName() + " - " + prop.getValue());
                if (prop.getName().equals("textures")) {
                    String texture = prop.getValue();
                    return texture;
                }
            }
        }

        Logger.debugLog("Player has no textures. Could not get skin URL.");
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
