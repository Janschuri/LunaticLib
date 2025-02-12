package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.CommandMessageKey;

public class LunaticCommandMessageKey extends LunaticMessageKey implements CommandMessageKey {

    private final LunaticCommand command;
    private final String key;

    public LunaticCommandMessageKey(LunaticCommand command, String key) {
        super(key);
        this.command = command;
        this.key = key;
    }

    @Override
    public String toString() {
        if (command instanceof HasParentCommand hasParentCommand && !hasParentCommand.isPrimaryCommand()) {
            return "commands." + hasParentCommand.getParentCommand().getName() + ".subcommands." + command.getName() + ".messages." + key;
        } else {
            return "commands." + command.getName() + ".messages." + key;
        }
    }
}
