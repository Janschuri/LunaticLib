package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface Subcommand {

    List<String> tabComplete(Sender sender, String[] args);

    Component getHelp(Sender sender);

    String getPermission();

    String getName();

    boolean execute(Sender sender, String[] args);

    List<String> getAliases();
}
