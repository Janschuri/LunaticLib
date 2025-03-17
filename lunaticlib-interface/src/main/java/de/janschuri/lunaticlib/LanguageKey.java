package de.janschuri.lunaticlib;

import java.util.Map;

public interface LanguageKey extends ConfigKey<Map<String,String>> {

    String getDefault(String lang);
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
