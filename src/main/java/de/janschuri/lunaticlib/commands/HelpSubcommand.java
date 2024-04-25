package de.janschuri.lunaticlib.commands;


import de.janschuri.lunaticlib.config.Language;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class HelpSubcommand extends Subcommand {

    private final Class<?> commandClass;

    public HelpSubcommand(String mainCommand, String name, String permission, Class<?> commandClass) {
        super(mainCommand, name, permission);
        this.commandClass = commandClass;
    }
    @Override
    public boolean execute(AbstractSender sender, String[] args) {
        if (!(sender instanceof AbstractPlayerSender)) {
            sender.sendMessage(Language.prefix + Language.getMessage("no_console_command"));
            return true;
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Language.prefix + Language.getMessage("no_permission"));
            return true;
        }

        AbstractPlayerSender playerCommandSender = (AbstractPlayerSender) sender;
        List<ClickableMessage> msg = new ArrayList<>();
        msg.add(new ClickableMessage(Language.prefix + Language.getMessage(mainCommand + "_help") + "\n"));

        try {
            Subcommand command = (Subcommand) commandClass.getDeclaredConstructor().newInstance();
            for (Subcommand subcommand : command.subcommands) {
                if (!(subcommand instanceof HelpSubcommand)) {
                    msg.add(subcommand.getHelp(sender));
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

        playerCommandSender.sendMessage(msg);

        return true;
    }
}
