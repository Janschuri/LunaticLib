package de.janschuri.lunaticlib.platform.bukkit.util;

import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) meta;

        PlayerProfile profile = Bukkit.getOfflinePlayer(UUID.randomUUID()).getPlayerProfile();
        PlayerTextures textures = profile.getTextures();

        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(urlObject);
        profile.setTextures(textures);
        skullMeta.setOwnerProfile(profile);

        item.setItemMeta(skullMeta);
        return item.clone();
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

    public static Map<String, Object> itemStackToMap(ItemStack itemStack) {
        Map<String, Object> map = new HashMap<>(itemStack.serialize());
        map.put("==", "org.bukkit.inventory.ItemStack");

        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            Map<String, Object> metaMap = new HashMap<>(meta.serialize());
            metaMap.put("==", "ItemMeta");
            map.put("meta", metaMap);
        }

        return map;
    }

    public static ItemStack mapToItemStack(Map<String, Object> map) {
        Logger.debugLog("Map: " + map);
        Object obj = ConfigurationSerialization.deserializeObject(map);

        try {
            ItemStack item = (ItemStack) obj;

            if (map.containsKey("meta")) {
                try {
                    Map<String, Object> metaMap = (Map<String, Object>) map.get("meta");
                    Object metaObj = ConfigurationSerialization.deserializeObject(metaMap);
                    ItemMeta meta = (ItemMeta) metaObj;
                    assert item != null;
                    item.setItemMeta(meta);
                } catch (Exception e) {
                    Logger.debugLog("Error: " + e.getMessage());
                }
            }

            return item;
        } catch (Exception e) {
            Logger.debugLog("Error: " + e.getMessage());
            return null;
        }
    }
}
