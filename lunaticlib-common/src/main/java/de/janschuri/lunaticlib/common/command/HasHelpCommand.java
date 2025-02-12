package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;
import net.kyori.adventure.text.Component;

public interface HasHelpCommand extends HasSubcommands {

    default LunaticHelpCommand getHelpCommand() {
        return new LunaticHelpCommand(getLanguageConfig(), this);
    }

    LunaticLanguageConfig getLanguageConfig();

    Component pageParamName();
}
