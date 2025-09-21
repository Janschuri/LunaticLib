package de.janschuri.lunaticlib.platform.velocity.proxyrequests.sender;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;

public class VelocityProxyRequestsSender extends VelocitySender implements ProxyRequestsSender<CommandSource> {

    public VelocityProxyRequestsSender(CommandSource source) {
        super(source);
    }
}
