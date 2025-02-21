package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public interface Command {
    String getName();

    List<String> getAliases();

    String getPermission();

    boolean checkPermission(Sender commandSender, String[] args);

    Component noPermissionMessage(Sender sender, String[] args);

    Component wrongUsageMessage(Sender sender, String[] args);

    String getFullCommand();

    boolean checkAndExecute(Sender commandSender, String[] args);

    boolean execute(Sender commandSender, String[] args);

    List<String> tabComplete(Sender commandSender, String[] newArgs);

    List<Component> getFormattedAliasesList(Sender sender);

    boolean isAlias(String arg);

    Component getMessage(MessageKey key, Placeholder... placeholders);

    Map<CommandMessageKey, String> getHelpMessages();
}
