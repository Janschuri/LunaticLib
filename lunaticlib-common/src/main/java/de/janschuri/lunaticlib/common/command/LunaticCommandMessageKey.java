package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.CommandMessageKey;

public class LunaticCommandMessageKey extends LunaticMessageKey implements CommandMessageKey {

    private final LunaticCommand command;

    public LunaticCommandMessageKey(LunaticCommand command, String key) {
        super(key);
        this.command = command;
    }

    @Override
    public String asString() {
        if (command instanceof HasParentCommand hasParentCommand && !hasParentCommand.isPrimaryCommand()) {
            return "commands." + hasParentCommand.getParentCommand().getName() + ".subcommands." + command.getName() + ".messages." + getKey();
        } else {
            return "commands." + command.getName() + ".messages." + getKey();
        }
    }

    @Override
    public CommandMessageKey defaultValue(String lang, String defaultMessage) {
        super.defaultValue(lang, defaultMessage);
        return this;
    }

    @Override
    public CommandMessageKey defaultValue(String defaultMessage) {
        super.defaultValue(defaultMessage);
        return this;
    }

    @Override
    public Command getCommand() {
        return command;
    }
}
