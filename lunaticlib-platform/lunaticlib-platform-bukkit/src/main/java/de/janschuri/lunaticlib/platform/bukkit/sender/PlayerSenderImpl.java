package de.janschuri.lunaticlib.platform.bukkit.sender;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.external.AdventureAPI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.DecisionGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.nms.PlayerSkin;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import net.kyori.adventure.inventory.Book;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public PlayerSenderImpl(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public PlayerSenderImpl(UUID uuid) {
        super(Bukkit.getPlayer(uuid));
        this.uuid = uuid;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public double[] getPosition() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player.isOnline()) {
            return new double[] {
                Bukkit.getPlayer(uuid).getLocation().getX(),
                Bukkit.getPlayer(uuid).getLocation().getY(),
                Bukkit.getPlayer(uuid).getLocation().getZ()
            };
        }
        return null;
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
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.isOnline()) {
            return true;
        }
        return Bukkit.getOfflinePlayer(uuid).hasPlayedBefore();
    }


    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Override
    public String getSkinURL() {
        if (Bukkit.getPlayer(uuid) != null) {
            PlayerSkin.getSkinURL(Bukkit.getPlayer(uuid));
        }
        return null;
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return true;
    }


    @Override
    public boolean openBook(Book.Builder book) {
        if (Bukkit.getPlayer(uuid) != null) {
            AdventureAPI.sendBook(Bukkit.getPlayer(uuid), book);
        }
        return false;
    }

    @Override
    public boolean closeBook() {
        if (Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            player.openInventory(Bukkit.createInventory(null, 9));
            player.closeInventory();
        }
        return false;
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            Logger.errorLog("Player is null");
            return false;
        }

        GUIManager.openGUI(new DecisionGUI(message), player);
        return true;
    }
}
