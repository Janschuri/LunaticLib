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
    public Command getCommand() {
        return command;
    }

    @Override
    public LunaticCommandMessageKey keyInlineComment(String comment) {
        super.keyInlineComment(comment);
        return this;
    }

    @Override
    public LunaticCommandMessageKey keyBlockComment(String comment) {
        super.keyBlockComment(comment);
        return this;
    }

    @Override
    public LunaticCommandMessageKey valueInlineComment(String comment) {
        super.valueInlineComment(comment);
        return this;
    }

    @Override
    public LunaticCommandMessageKey valueBlockComment(String comment) {
        super.valueBlockComment(comment);
        return this;
    }
}
