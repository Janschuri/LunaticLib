package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Objects;

public class VelocitySender implements Sender<CommandSource> {

    CommandSource source;

    public VelocitySender(CommandSource source) {
        this.source = Objects.requireNonNull(source, "CommandSource cannot be null");
    }

    @Override
    public boolean hasPermission(String permission) {
        if (source == null) {
            return false;
        }

        return source.hasPermission(permission);
    }

    @Override
    public boolean sendMessage(String message) {
        if (source == null) {
            return false;
        }

        TextComponent msg = LegacyComponentSerializer.legacy('§').deserialize(message);
        source.sendMessage(msg);
        return true;
    }

    public boolean sendMessage(Component message) {
        if (source == null) {
            return false;
        }

        source.sendMessage(message);
        return true;
    }

    @Override
    public CommandSource getHandle() {
        return source;
    }
}
