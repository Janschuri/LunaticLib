package de.janschuri.lunaticlib;

import java.util.List;
import java.util.Map;

public interface LunaticConfig {

    void load();

    String getString(String path, String defaultValue);
    String getString(String path);

    Integer getInt(String path, int defaultValue);

    Integer getInt(String path);

    Double getDouble(String path, double defaultValue);

    Double getDouble(String path);

    Boolean getBoolean(String path, boolean defaultValue);

    Boolean getBoolean(String path);

    List<String> getStringList(String path);

    Map<String, Object> getMap(String path);

    Map<String, String> getStringMap(String path);

    Map<String, Double> getDoubleMap(String path);

    Map<String, List<String>> getStringListMap(String path);

    Map<String, Integer> getIntMap(String path);

    Map<String, Boolean> getBooleanMap(String path);

    List<String> getKeys(String path);
}
