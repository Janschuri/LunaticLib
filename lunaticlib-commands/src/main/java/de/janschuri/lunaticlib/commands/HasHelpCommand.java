package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.config.LanguageConfig;
import net.kyori.adventure.text.Component;

public interface HasHelpCommand extends HasSubcommands {

    default LunaticHelpCommand getHelpCommand() {
        return new LunaticHelpCommand(this);
    }

    LanguageConfig getLanguageConfig();

    Component getPageParam();

    Component getHelpHeader();

    Component getHelpFooter(int page, int pageSize);
}
