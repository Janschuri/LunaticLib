package de.janschuri.lunaticlib.commands;


import de.janschuri.lunaticlib.config.Language;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHelpSubcommand extends AbstractSubcommand {

    private final Class<?> commandClass;
    private final Language language;

    public AbstractHelpSubcommand(Language language, String mainCommand, String name, String permission, Class<?> commandClass) {
        super(language, mainCommand, name, permission);
        this.language = language;
        this.commandClass = commandClass;
    }
    @Override
    public boolean execute(AbstractSender sender, String[] args) {
        if (!(sender instanceof AbstractPlayerSender)) {
            sender.sendMessage(language.getPrefix() + language.getMessage("no_console_command"));
            return true;
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(language.getPrefix() + language.getMessage("no_permission"));
            return true;
        }

        AbstractPlayerSender playerCommandSender = (AbstractPlayerSender) sender;
        List<ClickableMessage> msg = new ArrayList<>();
        msg.add(new ClickableMessage(language.getPrefix() + language.getMessage(mainCommand + "_help") + "\n"));

        try {
            AbstractSubcommand command = (AbstractSubcommand) commandClass.getDeclaredConstructor().newInstance();
            for (AbstractSubcommand subcommand : command.subcommands) {
                if (!(subcommand instanceof AbstractHelpSubcommand)) {
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
