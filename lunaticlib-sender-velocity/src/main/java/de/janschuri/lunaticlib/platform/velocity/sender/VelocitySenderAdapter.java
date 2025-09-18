package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.sender.SenderAdapter;

public class VelocitySenderAdapter implements SenderAdapter<PluginContainer, CommandSource> {

    private final ProxyServer proxy;

    public VelocitySenderAdapter(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public Sender getSender(CommandSource sender) {
        if (sender instanceof Player) {
            return new VelocityPlayerSender((Player) sender);
        }

        return new VelocitySender(sender);
    }

    ProxyServer proxy() {
        return proxy;
    }
}
