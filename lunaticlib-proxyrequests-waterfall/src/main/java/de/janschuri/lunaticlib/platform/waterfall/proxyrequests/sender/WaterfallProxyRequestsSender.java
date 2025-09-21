package de.janschuri.lunaticlib.platform.waterfall.proxyrequests.sender;

import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;
import net.md_5.bungee.api.CommandSender;

public class WaterfallProxyRequestsSender extends WaterfallSender implements ProxyRequestsSender<CommandSender> {

    public WaterfallProxyRequestsSender(CommandSender sender) {
        super(sender);
    }
}
