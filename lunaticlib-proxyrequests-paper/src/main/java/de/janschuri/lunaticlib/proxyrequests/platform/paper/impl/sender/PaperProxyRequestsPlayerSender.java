package de.janschuri.lunaticlib.proxyrequests.platform.paper.impl.sender;

import de.janschuri.lunaticlib.inventorygui.guis.DecisionGUI;
import de.janschuri.lunaticlib.inventorygui.handler.GUIManager;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.paper.impl.sender.PaperPlayerSenderImpl;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import de.janschuri.lunaticlib.utils.paper.ItemStackUtils;
import de.janschuri.lunaticlib.utils.paper.PaperUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class PaperProxyRequestsPlayerSender extends PaperPlayerSenderImpl implements ProxyRequestsPlayerSender {

    private final UUID uuid;

    public PaperProxyRequestsPlayerSender(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public PaperProxyRequestsPlayerSender(UUID uuid) {
        super(Bukkit.getPlayer(uuid));
        this.uuid = uuid;
    }

    @Override
    public double[] getPosition() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player.isOnline()) {
            double[] pos = new double[] {
                    Bukkit.getPlayer(uuid).getLocation().getX(),
                    Bukkit.getPlayer(uuid).getLocation().getY(),
                    Bukkit.getPlayer(uuid).getLocation().getZ()
            };

            return pos;
        }
        return null;
    }

    @Override
    public String getSkinURL() {
        if (Bukkit.getPlayer(uuid) != null) {
            return Bukkit.getPlayer(uuid).getPlayerProfile().getTextures().getSkin().toString();
        }
        return null;
    }

    @Override
    public boolean hasItemInMainHand() {

        return !Bukkit.getPlayer(uuid).getInventory().getItemInMainHand().getType().equals(Material.AIR);
    }

    @Override
    public byte[] getItemInMainHand() {
        if (Bukkit.getPlayer(uuid) != null) {
            ItemStack item = Bukkit.getPlayer(uuid).getInventory().getItemInMainHand();

            return ItemStackUtils.serializeItemStack(item);
        }
        return null;
    }

    @Override
    public boolean removeItemInMainHand() {
        if (Bukkit.getPlayer(uuid) != null) {
            Bukkit.getPlayer(uuid).getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            return true;
        }
        return false;
    }

    @Override
    public boolean giveItemDrop(byte[] item) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            ItemStack itemStack = ItemStackUtils.deserializeItemStack(item);

            if (itemStack == null) {
                return false;
            }

            Map<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);

            for (ItemStack overflowItem : overflow.values()) {
                player.getWorld().dropItem(player.getLocation(), overflowItem);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        if (range < 0) {
            return true;
        }
        org.bukkit.entity.Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return true;
        }

        if (player.getWorld() != Bukkit.getPlayer(uuid).getWorld()) {
            return true;
        }

        Location location1= player.getLocation();
        Location location2 = Bukkit.getPlayer(uuid).getLocation();

        return PaperUtils.isInRange(location1, location2, range);
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            ProxyRequestsLogger.error("Player is null");
            return false;
        }

        if (!PaperUtils.classExists("de.janschuri.lunaticlib.inventorygui.handler.GUIManager")) {
            ProxyRequestsLogger.error("GUIManager not found. Is lunaticlib-inventorygui present?");
        }

        GUIManager.openGUI(new DecisionGUI(message), player);
        return true;
    }
}
