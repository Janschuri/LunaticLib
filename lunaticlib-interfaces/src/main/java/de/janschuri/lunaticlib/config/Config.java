package de.janschuri.lunaticlib.config;

import java.nio.file.Path;
import java.util.*;

interface Config {

    void load(String defaultFilePath);

    Path getDataDirectory();

    String getFilePath();

    Path getPath();

    void save();

    String getString(String path, String defaultValue);

    String getString(String path);

    void setString(String path, String value);

    Integer getInt(String path, int defaultValue);

    Integer getInt(String path);

    void setInt(String path, int value);

    Double getDouble(String path, double defaultValue);

    Double getDouble(String path);

    void setDouble(String path, double value);

    Boolean getBoolean(String path, boolean defaultValue);

    Boolean getBoolean(String path);

    void setBoolean(String path, boolean value);

    Float getFloat(String path, float defaultValue);

    Float getFloat(String path);

    void setFloat(String path, float value);

    List<String> getStringList(String path);

    List<Map<String, Object>> getMapList(String path);

    void setStringList(String path, List<String> value);

    Map<String, Object> getMap(String path);

    void setMap(String path, Map<String, Object> value);

    Map<String, String> getStringMap(String path);

    void setStringMap(String path, Map<String, String> value);

    Map<String, Double> getDoubleMap(String path);

    void setDoubleMap(String path, Map<String, Double> value);

    Map<String, List<String>> getStringListMap(String path);

    void setStringListMap(String path, Map<String, List<String>> value);

    Map<String, Integer> getIntMap(String path);

    void setIntMap(String path, Map<String, Integer> value);

    Map<String, Boolean> getBooleanMap(String path);

    void setBooleanMap(String path, Map<String, Boolean> value);

    List<String> getKeys(String path);
}
