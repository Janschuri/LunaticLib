package de.janschuri.lunaticlib.platform.paper.proxyrequests;

import de.janschuri.lunaticlib.platform.paper.proxyrequests.external.PaperVault;
import de.janschuri.lunaticlib.platform.paper.proxyrequests.sender.PaperProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.platform.paper.proxyrequests.sender.PaperProxyRequestsSender;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSender;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderAdapter;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;
import de.janschuri.lunaticlib.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class PaperProxyRequestsAdapter extends PaperSenderAdapter implements ProxyRequestsAdapter<CommandSender> {

    private final JavaPlugin plugin;
    private final Vault vault;

    public PaperProxyRequestsAdapter(JavaPlugin plugin) {
        this.plugin = plugin;

        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            this.vault = new PaperVault();
        } else {
            this.vault = null;
        }
    }

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        plugin.getServer().sendPluginMessage(plugin, IDENTIFIER, message);
        return true;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        plugin.getServer().sendPluginMessage(plugin, IDENTIFIER, message);
        return true;
    }

    @Override
    public Vault getVault() {
        return vault;
    }

    @Override
    public ProxyRequestsSender getSender(CommandSender sender) {
        if (sender instanceof Player bukkitPlayer) {
            return new PaperProxyRequestsPlayerSender(bukkitPlayer);
        }

        return new PaperProxyRequestsSender(sender);
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return new PaperProxyRequestsPlayerSender(player);
        }
        return null;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
