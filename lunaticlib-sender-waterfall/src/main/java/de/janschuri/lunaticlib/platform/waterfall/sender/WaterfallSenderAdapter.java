package de.janschuri.lunaticlib.platform.waterfall.sender;

import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.SenderAdapter;
import de.janschuri.lunaticlib.sender.Sender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WaterfallSenderAdapter implements SenderAdapter<CommandSender> {

    private final Plugin plugin;

    public WaterfallSenderAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new WaterfallPlayerSender((ProxiedPlayer) sender);
        }
        return new WaterfallSender(sender);
    }

    @Override
    public @Nullable PlayerSender getPlayerSender(UUID uuid) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
        if (player != null) {
            return new WaterfallPlayerSender(player);
        }

        return null;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
