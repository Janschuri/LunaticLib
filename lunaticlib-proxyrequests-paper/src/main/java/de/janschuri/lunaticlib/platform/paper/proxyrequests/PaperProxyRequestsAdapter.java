package de.janschuri.lunaticlib.platform.paper.proxyrequests;

import de.janschuri.lunaticlib.platform.paper.proxyrequests.external.PaperVault;
import de.janschuri.lunaticlib.platform.paper.proxyrequests.sender.PaperProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class PaperProxyRequestsAdapter implements ProxyRequestsAdapter<Player> {

    private final Plugin plugin;
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
    public ProxyRequestsPlayerSender getPlayerSender(Player player) {
        return new PaperProxyRequestsPlayerSender(player);
    }
}
