package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.sender.SenderAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VelocitySenderAdapter implements SenderAdapter<CommandSource> {

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

    @Override
    public @Nullable PlayerSender getPlayerSender(UUID uuid) {
        Player player = proxy.getPlayer(uuid).orElse(null);
        if (player != null) {
            return new VelocityPlayerSender(player);
        }
        return null;
    }

    ProxyServer getProxy() {
        return proxy;
    }
}
