package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface LunaticLanguageConfig extends LunaticConfig {

    Component getMessage(MessageKey key, boolean withPrefix);
    Component getMessage(MessageKey key);

    String getMessageAsString(MessageKey key);
    String getMessageAsString(MessageKey key, boolean withPrefix);

    List<String> getAliases(String command);

    List<String> getAliases(String command, String subcommand);

    Component getHelpHeader(String command);

    Component getHelpFooter(String command, int page, int maxPage);

    Component getPrefix();
}
