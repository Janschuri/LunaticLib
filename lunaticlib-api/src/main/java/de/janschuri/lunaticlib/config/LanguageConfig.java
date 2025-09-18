package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.utils.Placeholder;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface LanguageConfig extends Config {

    void load();

    List<LanguageKey> getLanguageKeys(String packageName);

    List<Command> getCommands(String packageName);

    Component getMessage(MessageKey key, Placeholder... placeholders);

    Component getLang(LanguageKey key, Placeholder... placeholders);

    String getMessageAsString(MessageKey key, Placeholder... placeholders);

    String getMessageAsLegacyString(MessageKey key, Placeholder... placeholders);

    List<String> getAliases(String command);

    List<String> getAliases(String command, String subcommand);

    Component getPrefix();
}
