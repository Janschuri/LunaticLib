package de.janschuri.lunaticlib.platform.bukkit.sender;

import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.command.LunaticDecisionMessage;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.DecisionGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.util.BukkitUtils;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import net.kyori.adventure.inventory.Book;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<double[]> getPosition() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player.isOnline()) {
            double[] pos = new double[] {
                Bukkit.getPlayer(uuid).getLocation().getX(),
                Bukkit.getPlayer(uuid).getLocation().getY(),
                Bukkit.getPlayer(uuid).getLocation().getZ()
            };

            return CompletableFuture.completedFuture(pos);
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
    public CompletableFuture<Boolean> hasItemInMainHand() {

        return CompletableFuture.completedFuture(!Bukkit.getPlayer(uuid).getInventory().getItemInMainHand().getType().equals(Material.AIR));
    }

    @Override
    public CompletableFuture<byte[]> getItemInMainHand() {
        if (Bukkit.getPlayer(uuid) != null) {
            ItemStack item = Bukkit.getPlayer(uuid).getInventory().getItemInMainHand();

            return CompletableFuture.completedFuture(ItemStackUtils.serializeItemStack(item));
        }
        return null;
    }

    @Override
    public CompletableFuture<Boolean> removeItemInMainHand() {
        if (Bukkit.getPlayer(uuid) != null) {
            Bukkit.getPlayer(uuid).getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Boolean> giveItemDrop(byte[] item) {

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            ItemStack itemStack = ItemStackUtils.deserializeItemStack(item);

            if (itemStack == null) {
                return CompletableFuture.completedFuture(false);
            }

            Map<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);

            for (ItemStack overflowItem : overflow.values()) {
                player.getWorld().dropItem(player.getLocation(), overflowItem);
            }

            return CompletableFuture.completedFuture(true);
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public CompletableFuture<Boolean> isInRange(UUID playerUUID, double range) {
        if (range < 0) {
            return CompletableFuture.completedFuture(true);
        }
        org.bukkit.entity.Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return CompletableFuture.completedFuture(true);
        }

        if (player.getWorld() != Bukkit.getPlayer(uuid).getWorld()) {
            return CompletableFuture.completedFuture(true);
        }

        Location location1= player.getLocation();
        Location location2 = Bukkit.getPlayer(uuid).getLocation();

        return CompletableFuture.completedFuture(BukkitUtils.isInRange(location1, location2, range));
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
            return Bukkit.getPlayer(uuid).getPlayerProfile().getTextures().getSkin().toString();
        }
        return null;
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return true;
    }

    @Override
    public CompletableFuture<Boolean> openDecisionGUI(DecisionMessage message) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            Logger.errorLog("Player is null");
            return CompletableFuture.completedFuture(false);
        }

        GUIManager.openGUI(new DecisionGUI(message), player);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public void runCommand(String command) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.performCommand(command);
        }
    }
}
