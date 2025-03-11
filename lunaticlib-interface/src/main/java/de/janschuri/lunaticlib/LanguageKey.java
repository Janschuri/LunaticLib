package de.janschuri.lunaticlib;

import java.util.Map;

public interface LanguageKey extends ConfigKey {

    @Override
    Map<String, String> getDefault();

    String getDefault(String lang);
    LanguageKey defaultValues(Map<String, String> defaultMessages);
    LanguageKey defaultValue(String lang, String defaultMessage);

    @Override
    LanguageKey keyInlineComment(String comment);
    @Override
    LanguageKey keyBlockComment(String comment);
    @Override
    LanguageKey valueInlineComment(String comment);
    @Override
    LanguageKey valueBlockComment(String comment);
}
