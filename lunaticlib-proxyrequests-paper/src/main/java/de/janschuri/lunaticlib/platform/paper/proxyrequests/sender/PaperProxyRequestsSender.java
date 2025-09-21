package de.janschuri.lunaticlib.platform.paper.proxyrequests.sender;

import de.janschuri.lunaticlib.platform.paper.sender.PaperSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;
import org.bukkit.command.CommandSender;

public class PaperProxyRequestsSender extends PaperSender implements ProxyRequestsSender<CommandSender> {
    public PaperProxyRequestsSender(CommandSender sender) {
        super(sender);
    }
}
