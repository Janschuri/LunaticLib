package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public interface HasSubcommands extends Command {

    @Override
    default boolean execute(Sender sender, String[] args) {
        if (args.length == 0) {
            return handleNoMatchingSubcommand(sender, args);
        }

        final String subcommand = args[0];

        for (Command sc : getSubcommands()) {
            if (checkIsSubcommand(sc, subcommand)) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                return sc.execute(sender, newArgs);
            }
        }

        return handleNoMatchingSubcommand(sender, args);
    }

    default boolean handleNoMatchingSubcommand(Sender sender, String[] args) {
        if (this instanceof HasHelpCommand hasHelpCommand) {
            hasHelpCommand.getHelpCommand().execute(sender, args);
        } else {
            sender.sendMessage(wrongUsageMessage(sender, args));
        }
        return true;
    }

    List<Command> getSubcommands();
    default boolean checkIsSubcommand(Command subcommand, String arg) {
        return subcommand.isAlias(arg);
    }

    default List<String> subcommandsTabComplete(Sender sender, String[] args) {
        List<String> list = new ArrayList<>();
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        for (Command subcommand : getSubcommands()) {
            list.addAll(subcommand.tabComplete(sender, newArgs));
        }

        return list;
    }
}
