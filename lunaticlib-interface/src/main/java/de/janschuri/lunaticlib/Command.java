package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public interface Command {
    String getName();

    List<String> getAliases();

    String getPermission();

    default boolean checkPermission(Sender commandSender, String[] args) {
        if (commandSender.hasPermission(getPermission())) {
            return true;
        } else {
            commandSender.sendMessage(noPermissionMessage(commandSender, args));
            return false;
        }
    }

    Component noPermissionMessage(Sender sender, String[] args);

    default boolean checkSource(Sender commandSender, String[] args) {
        return true;
    }

    Component wrongUsageMessage(Sender sender, String[] args);

    String getFullCommand();

    default boolean checkAndExecute(Sender commandSender, String[] args) {
        return false;
    }

    boolean execute(Sender commandSender, String[] args);

    List<String> tabComplete(Sender commandSender, String[] newArgs);

    List<Component> getFormattedAliasesList(Sender sender);

    boolean isAlias(String arg);

    Component getMessage(MessageKey key, Placeholder... placeholders);

    Map<CommandMessageKey, String> getHelpMessages();
}
