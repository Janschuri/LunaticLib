package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.nio.file.Path;
import java.util.*;

public class LunaticLanguageConfigImpl extends LunaticConfigImpl implements de.janschuri.lunaticlib.LunaticLanguageConfig {

    private final String language;

    public LunaticLanguageConfigImpl(Path dataDirectory, String language) {
        super(dataDirectory, "lang.yml");
        this.language = language;
    }

    public void load() {
        super.load("lang/" + language + ".yml");
    }

    @Override
    public Component getMessage(MessageKey key, boolean withPrefix) {
        String keyString = key.toString().toLowerCase();

        String message = getString(keyString);
        if (message == null) {
            Logger.warnLog("Message not found: " + keyString);
            message = key.getDefaultMessage();
        }
        if (message == null) {
            message = "Message not found: " + keyString;
        }

        if (withPrefix) {
            return getPrefix().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    @Override
    public Component getMessage(MessageKey key) {
        return getMessage(key, true);
    }

    @Override
    public String getMessageAsString(MessageKey key) {
        return getMessageAsString(key, false);
    }

    @Override
    public String getMessageAsString(MessageKey key, boolean withPrefix) {
        return ((TextComponent) getMessage(key, withPrefix)).content();
    }

    public String getMessageAsLegacyString(MessageKey key, boolean withPrefix) {
        return LegacyComponentSerializer.legacySection().serialize(getMessage(key, withPrefix));
    }

    public String getMessageAsLegacyString(MessageKey key) {
        return getMessageAsLegacyString(key, false);
    }

    @Override
    public List<String> getAliases(String command) {
        List<String> list;
        list = getStringList("commands." + command + ".aliases");

        if (list == null) {
            return List.of(command);
        }

        return list;
    }
    @Override
    public List<String> getAliases(String command, String subcommand) {
        List<String> list;
        list = getStringList("commands." + command + ".subcommands." + subcommand + ".aliases");

        if (list == null) {
            return List.of(subcommand);
        }

        return list;
    }

    @Override
    public Component getHelpHeader(String command) {
        String headerRaw = getString("help_header");

        if (headerRaw == null) {
            headerRaw = "&7%header%";
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(headerRaw
                .replace("%header%", getCommandHelpHeader(command))
        );
    }

    @Override
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

    @Override
    public Component getPrefix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("prefix"));
    }
}