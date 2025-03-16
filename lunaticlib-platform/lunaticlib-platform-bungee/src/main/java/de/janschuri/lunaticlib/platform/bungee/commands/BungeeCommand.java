package de.janschuri.lunaticlib.platform.bungee.commands;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.bungee.BungeeLunaticLib;
import de.janschuri.lunaticlib.platform.bungee.PlatformImpl;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommand extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    private final de.janschuri.lunaticlib.Command lunaticCommand;

    public BungeeCommand(Command lunaticCommand) {
        super(lunaticCommand.getName(), lunaticCommand.getPermission(), lunaticCommand.getAliases().toArray(new String[0]));
        this.lunaticCommand = lunaticCommand;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender commandSender = new PlatformImpl().getSender(sender);

        ProxyServer.getInstance().getScheduler().runAsync(BungeeLunaticLib.getInstance(), () -> {
            if (!lunaticCommand.checkAndExecute(commandSender, args)) {
                sender.sendMessage(ChatColor.RED + "Internal server error. Please check the console for more information.");
            }
        });
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = lunaticCommand.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        Sender commandSender = new PlatformImpl().getSender(sender);
        return lunaticCommand.tabComplete(commandSender, newArgs);
    }
}