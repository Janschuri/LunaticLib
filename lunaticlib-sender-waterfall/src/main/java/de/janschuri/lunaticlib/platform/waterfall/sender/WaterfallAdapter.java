package de.janschuri.lunaticlib.platform.waterfall.sender;

import de.janschuri.lunaticlib.sender.SenderAdapter;
import de.janschuri.lunaticlib.sender.Sender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class WaterfallAdapter implements SenderAdapter<Plugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new PlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }
}
