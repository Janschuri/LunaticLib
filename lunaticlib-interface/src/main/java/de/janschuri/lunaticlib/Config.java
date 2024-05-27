package de.janschuri.lunaticlib;

import java.util.List;
import java.util.Map;

public interface Config {

    void load();

    String getString(String path, String defaultValue);
    String getString(String path);

    int getInt(String path, int defaultValue);

    int getInt(String path);

    double getDouble(String path, double defaultValue);

    double getDouble(String path);

    boolean getBoolean(String path, boolean defaultValue);

    boolean getBoolean(String path);

    List<String> getStringList(String path);

    Map<String, Object> getMap(String path);

    Map<String, String> getStringMap(String path);

    Map<String, Double> getDoubleMap(String path);

    Map<String, List<String>> getStringListMap(String path);

    Map<String, Integer> getIntMap(String path);

    Map<String, Boolean> getBooleanMap(String path);

    List<String> getKeys(String path);
}
