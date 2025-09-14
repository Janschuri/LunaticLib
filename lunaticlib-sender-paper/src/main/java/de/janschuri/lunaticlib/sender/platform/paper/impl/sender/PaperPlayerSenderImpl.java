package de.janschuri.lunaticlib.sender.platform.paper.impl.sender;

import de.janschuri.lunaticlib.sender.PlayerSender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperPlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public PaperPlayerSenderImpl(Player sender) {
        super(sender);
        this.uuid = sender.getUniqueId();
    }

    public PaperPlayerSenderImpl(UUID uuid) {
        super(Bukkit.getPlayer(uuid));
        this.uuid = uuid;
    }

    @Override
    public String getServerName() {
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
    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
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
    public boolean isSameServer(UUID uuid) {
        return true;
    }

    @Override
    public void runCommand(String command) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.performCommand(command);
        }
    }
}
