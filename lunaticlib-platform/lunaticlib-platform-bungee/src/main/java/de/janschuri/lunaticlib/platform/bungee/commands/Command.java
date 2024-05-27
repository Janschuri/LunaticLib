package de.janschuri.lunaticlib.platform.bungee.commands;

import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.Subcommand;
import de.janschuri.lunaticlib.platform.bungee.PlatformImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

public class Command extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    private final Subcommand subcommand;

    public Command(Subcommand subcommand) {
        super(subcommand.getName(), subcommand.getPermission(), subcommand.getAliases().toArray(new String[0]));
        this.subcommand = subcommand;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender commandSender = new PlatformImpl().getSender(sender);
        subcommand.execute(commandSender, args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = subcommand.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        Sender commandSender = new PlatformImpl().getSender(sender);
        return subcommand.tabComplete(commandSender, newArgs);
    }
}