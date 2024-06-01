package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.MessageKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.nio.file.Path;
import java.util.*;

public abstract class LanguageConfig extends AbstractConfig implements de.janschuri.lunaticlib.LanguageConfig {

    public LanguageConfig(Path dataDirectory, String language) {
        super(dataDirectory, "lang.yml", "lang/" + language + ".yml");
    }

    @Override
    public Component getMessage(MessageKey key, boolean withPrefix) {
        String keyString = key.toString().toLowerCase();

        if (withPrefix) {
            return getPrefix().append(LegacyComponentSerializer.legacyAmpersand().deserialize(getString(keyString)));
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString(keyString));
    }



    @Override
    public Component getMessage(MessageKey key) {
        return getMessage(key, true);
    }

    @Override
    public List<String> getAliases(String command) {
        List<String> list;
        list = getStringList("commands." + command + ".aliases");

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }
    @Override
    public List<String> getAliases(String command, String subcommand) {
        List<String> list;
        list = getStringList("commands." + command + ".subcommands." + subcommand + ".aliases");

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    @Override
    public Component getHelpHeader(String command) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("help_header").replace("%header%", getString("commands." + command + ".help_header")));
    }

    @Override
    public Component getHelpFooter(String command) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("help_footer").replace("%header%", getString("commands." + command + ".help_header")));
    }

    @Override
    public Component getPrefix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("prefix"));
    }
}