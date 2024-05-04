package de.janschuri.lunaticlib.senders.paper;

import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import de.janschuri.lunaticlib.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class PlayerSender extends AbstractPlayerSender {
    public PlayerSender(CommandSender sender) {
        super(((OfflinePlayer) sender).getUniqueId());
    }

    public PlayerSender(UUID uuid) {
        super(uuid);
    }

    public PlayerSender(String name) {
        super(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    @Override
    public boolean sendMessage(String message) {
        if (Bukkit.getPlayer(uuid) != null) {
            return new Sender(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return Bukkit.getPlayer(uuid).hasPermission(permission);
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public double[] getPosition() {
        return new double[] {
                Bukkit.getOfflinePlayer(uuid).getLocation().getX(),
                Bukkit.getOfflinePlayer(uuid).getLocation().getY(),
                Bukkit.getOfflinePlayer(uuid).getLocation().getZ()
        };
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        if (Bukkit.getPlayer(uuid) != null) {
            return new Sender(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        if (Bukkit.getPlayer(uuid) != null) {
            return new Sender(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> msg) {
        if (Bukkit.getPlayer(uuid) != null) {
            return new Sender(Bukkit.getPlayer(uuid)).sendMessage(msg);
        }
        return false;
    }

    @Override
    public boolean chat(String message) {
        if (Bukkit.getPlayer(uuid) != null) {
            Bukkit.getPlayer(uuid).chat(message);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasItemInMainHand() {
        return !Bukkit.getPlayer(uuid).getInventory().getItemInMainHand().getType().isAir();
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
        if (Bukkit.getPlayer(uuid) != null) {
            ItemStack itemStack = ItemStackUtils.deserializeItemStack(item);
            Bukkit.getPlayer(uuid).getWorld().dropItem(Bukkit.getPlayer(uuid).getLocation(), itemStack);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        if (range < 0) {
            return true;
        }
        org.bukkit.entity.Player player = Bukkit.getPlayer(playerUUID);
        return Bukkit.getPlayer(uuid).getLocation().distance(player.getLocation()) <= range;
    }

    @Override
    public boolean exists() {
        return Bukkit.getOfflinePlayer(uuid).hasPlayedBefore();
    }


    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Override
    public String getSkinURL() {
        return ItemStackUtils.getSkinURLFromUUID(uuid);
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return true;
    }
}
