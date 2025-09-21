package de.janschuri.lunaticlib.platform.waterfall.commands;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderHandler;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public class WaterfallCommand extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    private final Plugin plugin;
    private final Command lunaticCommand;

    public WaterfallCommand(Plugin plugin, Command lunaticCommand) {
        super(lunaticCommand.getName(), lunaticCommand.getPermission(), lunaticCommand.getAliases().toArray(new String[0]));
        this.plugin = plugin;
        this.lunaticCommand = lunaticCommand;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            Sender commandSender = WaterfallSenderHandler.getAdapter().getSender(sender);
            if (!lunaticCommand.checkAndExecute(commandSender, args)) {
                Component errorMessage = Component.text("Internal server error. Please check the console for more information.")
                        .color(TextColor.fromHexString("#FF0000"));
                commandSender.sendMessage(errorMessage);
            }
        });
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = lunaticCommand.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        Sender commandSender = WaterfallSenderHandler.getAdapter().getSender(sender);
        return lunaticCommand.tabComplete(commandSender, newArgs);
    }
}