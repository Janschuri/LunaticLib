package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.CommandMessageKey;
import de.janschuri.lunaticlib.common.command.HasParentCommand;
import de.janschuri.lunaticlib.common.command.LunaticCommand;

public class LunaticCommandMessageKey extends LunaticMessageKey implements CommandMessageKey {

    private final LunaticCommand command;

    public LunaticCommandMessageKey(LunaticCommand command, String key) {
        super(key);
        this.command = command;
    }

    @Override
    public String asString() {
        return command.getPath() + ".messages." + getKey();
    }

    @Override
    public LunaticCommandMessageKey defaultMessage(String lang, String defaultMessage) {
        super.defaultMessage(lang, defaultMessage);
        return this;
    }

    @Override
    public LunaticCommandMessageKey defaultMessage(String defaultMessage) {
        super.defaultMessage(defaultMessage);
        return this;
    }

    @Override
    public Command getCommand() {
        return command;
    }
}
