package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.CommandMessageKey;
import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface HasHelpCommand extends HasSubcommands {

    default LunaticHelpCommand getHelpCommand() {
        return new LunaticHelpCommand(this);
    }

    LunaticLanguageConfig getLanguageConfig();

    MessageKey pageParamName();

    MessageKey getHelpHeader();
}
