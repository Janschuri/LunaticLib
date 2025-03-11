package de.janschuri.lunaticlib;

import java.util.List;
import java.util.Map;

public interface MessageKey extends LanguageKey {
    boolean isWithPrefix();
    MessageKey noPrefix();
    MessageKey withPrefix();
    MessageKey defaultMessage(String lang, String defaultMessage);
    String getDefaultMessage(String lang);

    @Override
    MessageKey keyInlineComment(String comment);
    @Override
    MessageKey keyBlockComment(String comment);
    @Override
    MessageKey valueInlineComment(String comment);
    @Override
    MessageKey valueBlockComment(String comment);
}
