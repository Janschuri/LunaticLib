package de.janschuri.lunaticlib;

public interface CommandMessageKey extends MessageKey {
    Command getCommand();


    @Override
    CommandMessageKey keyInlineComment(String comment);
    @Override
    CommandMessageKey keyBlockComment(String comment);
    @Override
    CommandMessageKey valueInlineComment(String comment);
    @Override
    CommandMessageKey valueBlockComment(String comment);
}
