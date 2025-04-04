package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.LanguageKey;
import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.Placeholder;
import de.janschuri.lunaticlib.common.command.HasHelpCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public abstract class LunaticLanguageConfig extends LunaticConfig {

    private final String language;
    private List<LanguageKey> messageKeys;
    private List<Command> commands;

    public LunaticLanguageConfig(Path dataDirectory, String language) {
        super(dataDirectory, "lang.yml");
        this.language = language;
    }

    public void load() {
        String path = "lang/" + language + ".yml";

        if (isDefaultFilePathInResources(path)) {
            super.load(path);
        } else {
            super.load(null);
        }

        if (getString("prefix") == null) {
            setString("prefix", getDefaultPrefix());
        }

        this.messageKeys = getLanguageKeys(getPackage());

        for (LanguageKey key : messageKeys) {
            addCommentsFromKey(key);

            String value = getString(key.asString().toLowerCase(), key.getDefault(language));
            if (value == null) {
                Logger.errorLog("Missing message for key without default value: " + key.asString().toLowerCase());
                continue;
            }

            if (value.equals(key.getDefault(language))) {
                setString(key.asString().toLowerCase(), key.getDefault(language));
            }
        }

        this.commands = getCommands(getPackage());

        for (Command command : commands) {
            List<String> aliases = command.getDefaultAliases();
            setStringList(command.getPath() + ".aliases", aliases);
        }

        save();
    }

    abstract protected String getPackage();

    public List<LanguageKey> getLanguageKeys(String packageName) {
        if (messageKeys != null) {
            return messageKeys;
        }

        ClassLoader pluginClassLoader = this.getClass().getClassLoader();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName, pluginClassLoader))
                .setScanners(new SubTypesScanner(false))
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
                .addClassLoaders(pluginClassLoader)
        );

        Set<Class<? extends HasMessageKeys>> matchingClasses = reflections.getSubTypesOf(HasMessageKeys.class);

        List<LanguageKey> messageKeys = new ArrayList<>();

        for (Class<?> clazz : matchingClasses) {
            messageKeys.addAll(getLanguageKeys(clazz));
        }

        Logger.infoLog("Found " + messageKeys.size() + " message keys in package " + packageName);

        return messageKeys;
    }

    private List<LanguageKey> getLanguageKeys(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> LanguageKey.class.isAssignableFrom(field.getType()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(null);
                        if (value == null) {
                            Logger.errorLog("Warning: Field " + field.getName() + " is null in " + clazz.getName());
                            return null;
                        }
                        return (LanguageKey) value;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access MessageKey field: " + field.getName(), e);
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());
    }

    public List<Command> getCommands(String packageName) {
        if (this.commands != null) {
            return this.commands;
        }

        ClassLoader pluginClassLoader = this.getClass().getClassLoader();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName, pluginClassLoader))
                .setScanners(new SubTypesScanner(false))
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
                .addClassLoaders(pluginClassLoader)
        );

        Set<Class<? extends Command>> matchingClasses = reflections.getSubTypesOf(Command.class);
        List<Command> commands = new ArrayList<>();

        for (Class<? extends Command> clazz : matchingClasses) {
            try {
                Command command = clazz.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception e) {
            }
        }

        return commands;
    }

    public Component getMessage(MessageKey key, Placeholder... placeholders) {
        Component messageComponent = getLang(key, placeholders);

        if (key.isWithPrefix()) {
            return getPrefix().append(messageComponent);
        }

        return messageComponent;
    }

    public Component getLang(LanguageKey key, Placeholder... placeholders) {
        String keyString = key.asString().toLowerCase();

        String message = getString(keyString);
        if (message == null) {
            message = key.getDefault(language);

            if (message != null) {
                setString(keyString, message);
                save();
            } else {
                Logger.errorLog("Missing value for key without default: " + keyString);
                return Component.text("Missing value for key: " + keyString);
            }
        }

        Component messageComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

        for (Placeholder placeholder : placeholders) {
            TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                    .match(placeholder.getKey())
                    .replacement(placeholder.getValue())
                    .build();

            messageComponent = messageComponent.replaceText(replacementConfig);
        }

        return messageComponent;
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

    public Component getHelpFooter(HasHelpCommand command, int page, int maxPage) {
        String fallback = "&7%header% &8| &7Page %page% of %pages%";

        String headerRaw = getString("help_footer", fallback);

        return LegacyComponentSerializer.legacyAmpersand().deserialize(headerRaw
                .replace("%header%", getMessageAsLegacyString(command.getHelpHeader()))
                .replace("%page%", String.valueOf(page))
                .replace("%pages%", String.valueOf(maxPage))
        );
    }

    protected String getDefaultPrefix() {
        return getPackage() == null ? "null" : getPackage();
    }

    public Component getPrefix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(getString("prefix", getDefaultPrefix()));
    }
}