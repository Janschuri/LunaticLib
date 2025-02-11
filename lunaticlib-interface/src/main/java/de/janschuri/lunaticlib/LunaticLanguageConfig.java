package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface LunaticLanguageConfig extends LunaticConfig {

    Component getMessage(MessageKey key, Placeholder... placeholders);

    String getMessageAsString(MessageKey key, Placeholder... placeholders);

    List<String> getAliases(String command);

    List<String> getAliases(String command, String subcommand);

    Component getHelpHeader(String command);

    Component getHelpFooter(String command, int page, int maxPage);

    Component getPrefix();
}
