package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.nio.file.Path;
import java.util.*;

public class LunaticLanguageConfig extends LunaticConfig {

    private final String language;

    public LunaticLanguageConfig(Path dataDirectory, String language) {
        super(dataDirectory, "lang.yml");
        this.language = language;
    }

    public void load() {
        super.load("lang/" + language + ".yml");
    }

    public Component getMessage(MessageKey key, Placeholder... placeholders) {
        String keyString = key.toString().toLowerCase();

        String message = getString(keyString);
        if (message == null) {
            Logger.warnLog("Message not found: " + keyString);
            message = key.getDefaultMessage();
        }
        if (message == null) {
            message = "Message not found: " + keyString;
        }

        Component messageComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

        for (Placeholder placeholder : placeholders) {
            messageComponent = messageComponent.replaceText(TextReplacementConfig.builder()
                    .match(placeholder.getKey())
                    .replacement(placeholder.getValue())
                    .build());
        }

        if (key.isWithPrefix()) {
            return getPrefix().append(messageComponent);
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }


    public String getMessageAsString(MessageKey key, Placeholder... placeholders) {
        return ((TextComponent) getMessage(key, placeholders)).content();
    }

    public String getMessageAsLegacyString(MessageKey key, Placeholder... placeholders) {
        return LegacyComponentSerializer.legacySection().serialize(getMessage(key, placeholders));
    }

    public List<String> getAliases(String command) {
        List<String> list;
        list = getStringList("commands." + command + ".aliases");

        if (list == null) {
            return List.of(command);
        }

        return list;
    }

    public List<String> getAliases(String command, String subcommand) {
        List<String> list;
        list = getStringList("commands." + command + ".subcommands." + subcommand + ".aliases");

        if (list == null) {
            return List.of(subcommand);
        }

        return list;
    }

    public Component getHelpHeader(String command) {
        String headerRaw = getString("help_header");

        if (headerRaw == null) {
            headerRaw = "&7%header%";
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(headerRaw
                .replace("%header%", getCommandHelpHeader(command))
        );
    }

    public Component getHelpFooter(String command, int page, int maxPage) {
        String headerRaw = getString("help_footer");

        if (headerRaw == null) {
            headerRaw = "&7%header% &8| &7Page %page% of %pages%";
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(headerRaw
                .replace("%header%", getCommandHelpHeader(command))
                .replace("%page%", String.valueOf(page))
                .replace("%pages%", String.valueOf(maxPage))
        );
    }

    public String getCommandHelpHeader(String command) {
        String commandHelpHeader = getString("commands." + command + ".help_header");

        if (commandHelpHeader == null) {
            commandHelpHeader = command;
        }

        return commandHelpHeader;
    }

    public Component getPrefix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("prefix"));
    }
}