package de.janschuri.lunaticlib.platform.bukkit.util;

import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
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

        if (map == null) {
            return null;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                try {
                    map.put(entry.getKey(), Integer.parseInt((String) entry.getValue()));
                } catch (NumberFormatException ignored) {}
            }
        }

        Object obj = ConfigurationSerialization.deserializeObject(map);
        try {
            ItemStack item = (ItemStack) obj;

            if (map.containsKey("meta")) {
                try {
                    Map<String, Object> metaMap = (Map<String, Object>) map.get("meta");

                    for (Map.Entry<String, Object> entry : metaMap.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            try {
                                metaMap.put(entry.getKey(), Integer.parseInt((String) entry.getValue()));
                            } catch (NumberFormatException ignored) {}
                        }
                    }

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

    public static ItemStack getSpawnEgg(EntityType entityType) {
        Material material = Material.getMaterial(entityType.name() + "_SPAWN_EGG");
        return material == null ? new ItemStack(Material.GHAST_SPAWN_EGG) : new ItemStack(material);
    }

    public static ItemStack getItemStack(Material material) {
        ItemStack fallbackItemStack = new ItemStack(Material.BARRIER);
        ItemMeta fallbackMeta = fallbackItemStack.getItemMeta();
        fallbackMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
        fallbackMeta.addEnchant(Enchantment.MENDING, 1, true);
        fallbackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        fallbackItemStack.setItemMeta(fallbackMeta);

        if (material == null) {
            return fallbackItemStack;
        }

        if (material.isItem()) {
            return new ItemStack(material);
        }

        if (material.name().contains("WALL_")) {
            String name = material.name().replace("WALL_", "");
            Material wallMaterial = Material.getMaterial(name);
            if (wallMaterial != null) {
                ItemStack itemStack = new ItemStack(wallMaterial);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
        }

        if (material.name().contains("POTTED_")) {
            String name = material.name().replace("POTTED_", "");
            Material wallMaterial = Material.getMaterial(name);
            if (wallMaterial != null) {
                ItemStack itemStack = new ItemStack(wallMaterial);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
        }

        if (material.name().contains("CANDLE_CAKE")) {
                ItemStack itemStack = new ItemStack(Material.CAKE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
        }

        switch (material) {
            case WATER:
                ItemStack water = new ItemStack(Material.WATER_BUCKET);
                ItemMeta waterMeta = water.getItemMeta();
                waterMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                water.setItemMeta(waterMeta);
                return water;
            case LAVA:
                ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
                ItemMeta lavaMeta = lava.getItemMeta();
                lavaMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                lava.setItemMeta(lavaMeta);
                return lava;
            case TALL_SEAGRASS:
                ItemStack tallSeagrass = new ItemStack(Material.SEAGRASS);
                ItemMeta tallSeagrassMeta = tallSeagrass.getItemMeta();
                tallSeagrassMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                tallSeagrass.setItemMeta(tallSeagrassMeta);
                return tallSeagrass;
            case PISTON_HEAD:
                ItemStack pistonHead = new ItemStack(Material.PISTON);
                ItemMeta pistonHeadMeta = pistonHead.getItemMeta();
                pistonHeadMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                pistonHead.setItemMeta(pistonHeadMeta);
                return pistonHead;
            case MOVING_PISTON:
                ItemStack movingPiston = new ItemStack(Material.PISTON);
                ItemMeta movingPistonMeta = movingPiston.getItemMeta();
                movingPistonMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                movingPiston.setItemMeta(movingPistonMeta);
                return movingPiston;
            case FIRE:
                ItemStack fire = new ItemStack(Material.CAMPFIRE);
                ItemMeta fireMeta = fire.getItemMeta();
                fireMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                fire.setItemMeta(fireMeta);
                return fire;
            case SOUL_FIRE:
                ItemStack soulFire = new ItemStack(Material.SOUL_CAMPFIRE);
                ItemMeta soulFireMeta = soulFire.getItemMeta();
                soulFireMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                soulFire.setItemMeta(soulFireMeta);
                return soulFire;
            case REDSTONE_WIRE:
                ItemStack redstoneWire = new ItemStack(Material.REDSTONE);
                ItemMeta redstoneWireMeta = redstoneWire.getItemMeta();
                redstoneWireMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                redstoneWire.setItemMeta(redstoneWireMeta);
                return redstoneWire;
            case NETHER_PORTAL:
                ItemStack netherPortal = new ItemStack(Material.NETHER_BRICK);
                ItemMeta netherPortalMeta = netherPortal.getItemMeta();
                netherPortalMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                netherPortal.setItemMeta(netherPortalMeta);
                return netherPortal;
            case ATTACHED_PUMPKIN_STEM:
                ItemStack attachedPumpkinStem = new ItemStack(Material.PUMPKIN);
                ItemMeta attachedPumpkinStemMeta = attachedPumpkinStem.getItemMeta();
                attachedPumpkinStemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                attachedPumpkinStem.setItemMeta(attachedPumpkinStemMeta);
                return attachedPumpkinStem;
            case ATTACHED_MELON_STEM:
                ItemStack attachedMelonStem = new ItemStack(Material.MELON);
                ItemMeta attachedMelonStemMeta = attachedMelonStem.getItemMeta();
                attachedMelonStemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                attachedMelonStem.setItemMeta(attachedMelonStemMeta);
                return attachedMelonStem;
            case PUMPKIN_STEM:
                ItemStack pumpkinStem = new ItemStack(Material.PUMPKIN);
                ItemMeta pumpkinStemMeta = pumpkinStem.getItemMeta();
                pumpkinStemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                pumpkinStem.setItemMeta(pumpkinStemMeta);
                return pumpkinStem;
            case MELON_STEM:
                ItemStack melonStem = new ItemStack(Material.MELON);
                ItemMeta melonStemMeta = melonStem.getItemMeta();
                melonStemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                melonStem.setItemMeta(melonStemMeta);
                return melonStem;
            case WATER_CAULDRON:
                ItemStack waterCauldron = new ItemStack(Material.CAULDRON);
                ItemMeta waterCauldronMeta = waterCauldron.getItemMeta();
                waterCauldronMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                waterCauldron.setItemMeta(waterCauldronMeta);
                return waterCauldron;
            case LAVA_CAULDRON:
                ItemStack lavaCauldron = new ItemStack(Material.CAULDRON);
                ItemMeta lavaCauldronMeta = lavaCauldron.getItemMeta();
                lavaCauldronMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                lavaCauldron.setItemMeta(lavaCauldronMeta);
                return lavaCauldron;
            case POWDER_SNOW_CAULDRON:
                ItemStack powderSnowCauldron = new ItemStack(Material.CAULDRON);
                ItemMeta powderSnowCauldronMeta = powderSnowCauldron.getItemMeta();
                powderSnowCauldronMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                powderSnowCauldron.setItemMeta(powderSnowCauldronMeta);
                return powderSnowCauldron;
            case END_PORTAL:
                ItemStack endPortal = new ItemStack(Material.END_PORTAL_FRAME);
                ItemMeta endPortalMeta = endPortal.getItemMeta();
                endPortalMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                endPortal.setItemMeta(endPortalMeta);
                return endPortal;
            case COCOA:
                ItemStack cocoa = new ItemStack(Material.COCOA_BEANS);
                ItemMeta cocoaMeta = cocoa.getItemMeta();
                cocoaMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                cocoa.setItemMeta(cocoaMeta);
                return cocoa;
            case TRIPWIRE:
                ItemStack tripwire = new ItemStack(Material.STRING);
                ItemMeta tripwireMeta = tripwire.getItemMeta();
                tripwireMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                tripwire.setItemMeta(tripwireMeta);
                return tripwire;
            case CARROTS:
                ItemStack carrots = new ItemStack(Material.CARROT);
                ItemMeta carrotsMeta = carrots.getItemMeta();
                carrotsMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                carrots.setItemMeta(carrotsMeta);
                return carrots;
            case POTATOES:
                ItemStack potatoes = new ItemStack(Material.POTATO);
                ItemMeta potatoesMeta = potatoes.getItemMeta();
                potatoesMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                potatoes.setItemMeta(potatoesMeta);
                return potatoes;
            case BEETROOTS:
                ItemStack beetroots = new ItemStack(Material.BEETROOT);
                ItemMeta beetrootsMeta = beetroots.getItemMeta();
                beetrootsMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                beetroots.setItemMeta(beetrootsMeta);
                return beetroots;
            case PITCHER_CROP:
                ItemStack pitcherCrop = new ItemStack(Material.PITCHER_POD);
                ItemMeta pitcherCropMeta = pitcherCrop.getItemMeta();
                pitcherCropMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                pitcherCrop.setItemMeta(pitcherCropMeta);
                return pitcherCrop;
            case TORCHFLOWER_CROP:
                ItemStack torchflowerCrop = new ItemStack(Material.TORCHFLOWER);
                ItemMeta torchflowerCropMeta = torchflowerCrop.getItemMeta();
                torchflowerCropMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                torchflowerCrop.setItemMeta(torchflowerCropMeta);
                return torchflowerCrop;
            case END_GATEWAY:
                ItemStack endGateway = new ItemStack(Material.END_PORTAL_FRAME);
                ItemMeta endGatewayMeta = endGateway.getItemMeta();
                endGatewayMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                endGateway.setItemMeta(endGatewayMeta);
                return endGateway;
            case FROSTED_ICE:
                ItemStack frostedIce = new ItemStack(Material.ICE);
                ItemMeta frostedIceMeta = frostedIce.getItemMeta();
                frostedIceMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                frostedIce.setItemMeta(frostedIceMeta);
                return frostedIce;
            case KELP_PLANT:
                ItemStack kelpPlant = new ItemStack(Material.KELP);
                ItemMeta kelpPlantMeta = kelpPlant.getItemMeta();
                kelpPlantMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                kelpPlant.setItemMeta(kelpPlantMeta);
                return kelpPlant;
            case BAMBOO_SAPLING:
                ItemStack bambooSapling = new ItemStack(Material.BAMBOO);
                ItemMeta bambooSaplingMeta = bambooSapling.getItemMeta();
                bambooSaplingMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                bambooSapling.setItemMeta(bambooSaplingMeta);
                return bambooSapling;
            case BUBBLE_COLUMN:
                ItemStack bubbleColumn = new ItemStack(Material.WATER_BUCKET);
                ItemMeta bubbleColumnMeta = bubbleColumn.getItemMeta();
                bubbleColumnMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                bubbleColumn.setItemMeta(bubbleColumnMeta);
                return bubbleColumn;
            case SWEET_BERRY_BUSH:
                ItemStack sweetBerryBush = new ItemStack(Material.SWEET_BERRIES);
                ItemMeta sweetBerryBushMeta = sweetBerryBush.getItemMeta();
                sweetBerryBushMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                sweetBerryBush.setItemMeta(sweetBerryBushMeta);
                return sweetBerryBush;
            case WEEPING_VINES_PLANT:
                ItemStack weepingVinesPlant = new ItemStack(Material.WEEPING_VINES);
                ItemMeta weepingVinesPlantMeta = weepingVinesPlant.getItemMeta();
                weepingVinesPlantMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                weepingVinesPlant.setItemMeta(weepingVinesPlantMeta);
                return weepingVinesPlant;
            case TWISTING_VINES_PLANT:
                ItemStack twistingVinesPlant = new ItemStack(Material.TWISTING_VINES);
                ItemMeta twistingVinesPlantMeta = twistingVinesPlant.getItemMeta();
                twistingVinesPlantMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                twistingVinesPlant.setItemMeta(twistingVinesPlantMeta);
                return twistingVinesPlant;
            case POWDER_SNOW:
                ItemStack powderSnow = new ItemStack(Material.SNOWBALL);
                ItemMeta powderSnowMeta = powderSnow.getItemMeta();
                powderSnowMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                powderSnow.setItemMeta(powderSnowMeta);
                return powderSnow;
            case CAVE_VINES:
                ItemStack caveVines = new ItemStack(Material.VINE);
                ItemMeta caveVinesMeta = caveVines.getItemMeta();
                caveVinesMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                caveVines.setItemMeta(caveVinesMeta);
                return caveVines;
            case CAVE_VINES_PLANT:
                ItemStack caveVinesPlant = new ItemStack(Material.VINE);
                ItemMeta caveVinesPlantMeta = caveVinesPlant.getItemMeta();
                caveVinesPlantMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                caveVinesPlant.setItemMeta(caveVinesPlantMeta);
                return caveVinesPlant;
            case BIG_DRIPLEAF_STEM:
                ItemStack bigDripleafStem = new ItemStack(Material.BIG_DRIPLEAF);
                ItemMeta bigDripleafStemMeta = bigDripleafStem.getItemMeta();
                bigDripleafStemMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                bigDripleafStem.setItemMeta(bigDripleafStemMeta);
                return bigDripleafStem;
            case POTTED_AZALEA_BUSH:
                ItemStack pottedAzaleaBush = new ItemStack(Material.AZALEA);
                ItemMeta pottedAzaleaBushMeta = pottedAzaleaBush.getItemMeta();
                pottedAzaleaBushMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                pottedAzaleaBush.setItemMeta(pottedAzaleaBushMeta);
                return pottedAzaleaBush;
            case POTTED_FLOWERING_AZALEA_BUSH:
                ItemStack pottedFloweringAzaleaBush = new ItemStack(Material.FLOWERING_AZALEA);
                ItemMeta pottedFloweringAzaleaBushMeta = pottedFloweringAzaleaBush.getItemMeta();
                pottedFloweringAzaleaBushMeta.setDisplayName("§r" + Utils.underscoreToSpace(material.name()));
                pottedFloweringAzaleaBush.setItemMeta(pottedFloweringAzaleaBushMeta);
                return pottedFloweringAzaleaBush;
        }

        return fallbackItemStack;
    }
}
