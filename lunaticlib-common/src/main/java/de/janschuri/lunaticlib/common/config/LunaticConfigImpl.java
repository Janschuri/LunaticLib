package de.janschuri.lunaticlib.common.config;

import com.google.common.base.Preconditions;
import de.janschuri.lunaticlib.common.logger.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LunaticConfigImpl implements de.janschuri.lunaticlib.LunaticConfig {

    private final String filePath;
    private final String defaultFilePath;
    private final Path dataDirectory;
    private Map<String, Object> yamlMap = new LinkedHashMap<>();

    public LunaticConfigImpl(Path dataDirectory, String filepath, String defaultFilePath) {
        this.filePath = filepath;
        this.defaultFilePath = defaultFilePath;
        this.dataDirectory = dataDirectory;
    }

    @Override
    public void load() {

        File file = new File(dataDirectory.toFile(), filePath);

        if (!dataDirectory.resolve(filePath).getParent().toFile().exists()) {
            try {
                Files.createDirectories(dataDirectory.resolve(filePath).getParent().toFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        if (!file.exists()) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath)) {
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    throw new IOException("Resource '" + defaultFilePath + "' not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {

            LoaderOptions loaderOptions = new LoaderOptions();
            loaderOptions.setProcessComments(true);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setProcessComments(true);
            dumperOptions.setSplitLines(false); // remove the line breaks
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // remove quotes
            dumperOptions.setIndent(2);
            Yaml yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
            Node root;

            try (FileInputStream inputStream = new FileInputStream(file)) {
                try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    root = yaml.compose(reader);
                }
            }

            Node defaultRoot = null;

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath)) {
                if (inputStream != null) {
                    try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                        defaultRoot = yaml.compose(reader);
                    }
                }
            }

            Node newNode;

            if (root == null) {
                Logger.errorLog("Error while loading config file: " + dataDirectory.resolve(filePath).getParent() + "/" + file.getName());
                newNode = defaultRoot;
            } else {
                Logger.infoLog("Loaded config file: " + dataDirectory.resolve(filePath).getParent() + "/" + file.getName());
                newNode = mergeNodes(root, defaultRoot);
            }

            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                 yaml.serialize(newNode, writer);
            }

            yamlMap = loadYamlMap(newNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> loadYamlMap(Node root) {
        Map<String, Object> yamlMap = new HashMap<>();
        MappingNode mappingNode = (MappingNode) root;
        List<NodeTuple> list = mappingNode.getValue();
        for (NodeTuple node : list) {
            yamlMap.put(((ScalarNode) node.getKeyNode()).getValue(), loadNodeTuple(node));
        }
        return yamlMap;
    }

    private void save() {
        File file = new File(dataDirectory.toFile(), filePath);

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setProcessComments(true);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setProcessComments(true);
        dumperOptions.setSplitLines(false); // remove the line breaks
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // remove quotes
        dumperOptions.setIndent(2);
        Yaml yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
        Node root = null;

        yaml.dump(yamlMap);
        Node savingValues = yaml.compose(new StringReader(yaml.dump(yamlMap)));

        try (FileInputStream inputStream = new FileInputStream(file)) {
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                root = yaml.compose(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node newNode;

        if (root == null) {
            Logger.errorLog("Error while loading config file: " + filePath);
            newNode = savingValues;
        } else {
            Logger.infoLog("Loaded config file: " + filePath);
            newNode = mergeNodes(savingValues, root);
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
             yaml.serialize(newNode, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object loadNodeTuple(NodeTuple nodeTuple) {
        if (nodeTuple.getValueNode() instanceof MappingNode) {
            Map<String, Object> yamlMap = new HashMap<>();
            MappingNode mappingNode = (MappingNode) nodeTuple.getValueNode();
            List<NodeTuple> list = mappingNode.getValue();
            for (NodeTuple node : list) {
                yamlMap.put(((ScalarNode) node.getKeyNode()).getValue(), loadNodeTuple(node));
            }
            return yamlMap;
        }
        if (nodeTuple.getValueNode() instanceof ScalarNode) {
            return ((ScalarNode) nodeTuple.getValueNode()).getValue();
        }
        if (nodeTuple.getValueNode() instanceof SequenceNode) {
            List<Node> nodes = ((SequenceNode) nodeTuple.getValueNode()).getValue();
            List<Object> list = new ArrayList<>();
            for (Node node : nodes) {
                if (node instanceof MappingNode) {
                    list.add(loadYamlMap(node));
                }
                if (node instanceof ScalarNode) {
                    list.add(((ScalarNode) node).getValue());
                }
            }
            return list;
        }
        return null;
    }

    private static Node mergeNodes (Node node, Node defaultNode) {
        if (node instanceof MappingNode && defaultNode instanceof MappingNode) {
            MappingNode mappingNode = (MappingNode) node;
            MappingNode defaultMappingNode = (MappingNode) defaultNode;
            List<NodeTuple> list = mappingNode.getValue();
            List<NodeTuple> defaultList = defaultMappingNode.getValue();

            mappingNode.setValue(mergeMappingNodes(list, defaultList));

            return mappingNode;
        }

        return null;
    }

    private static List<NodeTuple> mergeMappingNodes(List<NodeTuple> list, List<NodeTuple> defaultList) {
        Map<String, NodeTuple> map = new HashMap<>();
        Map<String, NodeTuple> defaultMap = new HashMap<>();
        List<String> index = new ArrayList<>();
        List<NodeTuple> mergedList = new ArrayList<>();
        for (NodeTuple n : defaultList) {
            defaultMap.put(((ScalarNode) n.getKeyNode()).getValue(), n);
            index.add(((ScalarNode) n.getKeyNode()).getValue());
        }
        for (NodeTuple n : list) {
            map.put(((ScalarNode) n.getKeyNode()).getValue(), n);
            if (!index.contains(((ScalarNode) n.getKeyNode()).getValue())) {
                index.add(((ScalarNode) n.getKeyNode()).getValue());
            }
        }

        for (String key : index) {
            Logger.debugLog("Merging key: " + key);

            if (!map.containsKey(key)) {
                Logger.errorLog("Missing key in config: " + key);
                Logger.errorLog("Using default value: " + loadNodeTuple(defaultMap.get(key)));
                mergedList.add(defaultMap.get(key));
            } else {
                Logger.debugLog("Key found in map: " + key);
                if (!defaultMap.containsKey(key)) {
                    mergedList.add(map.get(key));
                } else {
                    Node node = map.get(key).getValueNode();
                    Node defaultNode = defaultMap.get(key).getValueNode();
                    if (node instanceof MappingNode && defaultNode instanceof MappingNode) {
                        MappingNode newMappingNode = (MappingNode) node;
                        MappingNode newDefaultMappingNode = (MappingNode) defaultNode;
                        List<NodeTuple> newList = newMappingNode.getValue();
                        List<NodeTuple> newDefaultList = newDefaultMappingNode.getValue();

                        newMappingNode.setValue(mergeMappingNodes(newList, newDefaultList));

                        NodeTuple newNodeTuple = new NodeTuple(defaultMap.get(key).getKeyNode(), newMappingNode);
                        mergedList.add(newNodeTuple);
                    } else {
                        mergedList.add(map.get(key));
                    }
                }
            }
        }
        return mergedList;
    }

    protected Map<String, Object> getYamlMap() {
        return yamlMap;
    }

    private Object get(String path) {
        String[] parts = path.split("\\.");
        Object current = yamlMap;
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                Logger.errorLog("Error while getting config value: " + path);
                return null;
            }
        }

        return current;
    }

    @Override
    public String getString(String path, String defaultValue) {
        try {
            return get(path) == null ? defaultValue : Objects.requireNonNull(get(path)).toString();
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path + "\n Returning default value: " + defaultValue);
            return defaultValue;
        }
    }

    @Override
    public String getString(String path) {
        try {
            return Objects.requireNonNull(get(path)).toString();
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setString(String path, String value) {
        Logger.debugLog("Setting value: " + path + " = " + value);
        String[] parts = path.split("\\.");
        Map<String, Object> current = yamlMap;
        Logger.debugLog("Current map: " + current.toString());
        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1) {
                current.put(parts[i], value);
            } else {
                if (current.containsKey(parts[i])) {
                    if (current.get(parts[i]) instanceof Map) {
                        current = (Map<String, Object>) current.get(parts[i]);
                    } else {
                        Map<String, Object> newMap = new LinkedHashMap<>();
                        current.put(parts[i], newMap);
                        current = newMap;
                    }
                } else {
                    Map<String, Object> newMap = new LinkedHashMap<>();
                    current.put(parts[i], newMap);
                    current = newMap;
                }
            }
        }

        save();
    }

    @Override
    public Integer getInt(String path, int defaultValue) {
        try {
            return get(path) == null ? defaultValue : Integer.parseInt(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path + "\n Returning default value: " + defaultValue);
            return defaultValue;
        }
    }

    @Override
    public Integer getInt(String path) {
        try {
            return Integer.parseInt(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setInt(String path, int value) {
        setString(path, String.valueOf(value));
    }

    @Override
    public Double getDouble(String path, double defaultValue) {
        try {
            return get(path) == null ? defaultValue : Double.parseDouble(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path + "\n Returning default value: " + defaultValue);
            return defaultValue;
        }
    }

    @Override
    public Double getDouble(String path) {
        try {
            return Double.parseDouble(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setDouble(String path, double value) {
        setString(path, String.valueOf(value));
    }

    @Override
    public Boolean getBoolean(String path, boolean defaultValue) {
        try {
            return get(path) == null ? defaultValue : Boolean.parseBoolean(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path + "\n Returning default value: " + defaultValue);
            return defaultValue;
        }
    }

    @Override
    public Boolean getBoolean(String path) {
        try {
            return Boolean.parseBoolean(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setBoolean(String path, boolean value) {
        setString(path, String.valueOf(value));
    }

    public Float getFloat(String path, float defaultValue) {
        try {
            return get(path) == null ? defaultValue : Float.parseFloat(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path + "\n Returning default value: " + defaultValue);
            return defaultValue;
        }
    }

    public Float getFloat(String path) {
        try {
            return Float.parseFloat(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setFloat(String path, float value) {
        setString(path, String.valueOf(value));
    }

    @Override
    public List<String> getStringList(String path) {
        try {
            return (List<String>) get(path);
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setStringList(String path, List<String> value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = yamlMap;
        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1) {
                current.put(parts[i], value);
            } else {
                if (current.containsKey(parts[i])) {
                    if (current.get(parts[i]) instanceof Map) {
                        current = (Map<String, Object>) current.get(parts[i]);
                    } else {
                        Map<String, Object> newMap = new LinkedHashMap<>();
                        current.put(parts[i], newMap);
                        current = newMap;
                    }
                } else {
                    Map<String, Object> newMap = new LinkedHashMap<>();
                    current.put(parts[i], newMap);
                    current = newMap;
                }
            }
        }

        save();
    }

    @Override
    public Map<String, Object> getMap(String path) {
        try {
            return (Map<String, Object>) get(path);
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setMap(String path, Map<String, Object> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            if (obj instanceof Map) {
                setMap(path + "." + key, (Map<String, Object>) obj);
            } else if (obj instanceof List) {
                setStringList(path + "." + key, (List<String>) obj);
            } else {
                setString(path + "." + key, obj.toString());
            }
        }

        save();
    }

    @Override
    public Map<String, String> getStringMap(String path) {
        try {
            Map<String, Object> map = getMap(path);
            Map<String, String> stringMap = new HashMap<>();
            for (Object key : map.keySet()) {
                stringMap.put(key.toString(), map.get(key).toString());
            }
            return stringMap;
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setStringMap(String path, Map<String, String> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            setString(path + "." + key, obj.toString());
        }

        save();
    }

    @Override
    public Map<String, Double> getDoubleMap(String path) {
        try {
            Map<String, Object> map = getMap(path);
            Map<String, Double> doubleMap = new HashMap<>();
            for (Object key : map.keySet()) {
                doubleMap.put(key.toString(), Double.parseDouble(map.get(key).toString()));
            }
            return doubleMap;
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setDoubleMap(String path, Map<String, Double> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            setString(path + "." + key, obj.toString());
        }

        save();
    }

    @Override
    public Map<String, List<String>> getStringListMap(String path) {
        try {
            Map<String, Object> map = getMap(path);
            Map<String, List<String>> stringListMap = new HashMap<>();
            for (Object key : map.keySet()) {
                stringListMap.put(key.toString(), (List<String>) map.get(key));
            }
            return stringListMap;
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setStringListMap(String path, Map<String, List<String>> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            setStringList(path + "." + key, (List<String>) obj);
        }

        save();
    }

    @Override
    public Map<String, Integer> getIntMap(String path) {
        try {
            Map<String,Object> map = getMap(path);
            Map<String, Integer> intMap = new HashMap<>();
            for (Object key : map.keySet()) {
                intMap.put(key.toString(), Integer.parseInt(map.get(key).toString()));
            }
            return intMap;
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setIntMap(String path, Map<String, Integer> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            setString(path + "." + key, obj.toString());
        }

        save();
    }

    @Override
    public Map<String, Boolean> getBooleanMap(String path) {
        try {
            Map<String,Object> map = getMap(path);
            Map<String, Boolean> booleanMap = new HashMap<>();
            for (Object key : map.keySet()) {
                booleanMap.put(key.toString(), Boolean.parseBoolean(map.get(key).toString()));
            }
            return booleanMap;
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setBooleanMap(String path, Map<String, Boolean> value) {
        for (String key : value.keySet()) {
            Object obj = value.get(key);
            setString(path + "." + key, obj.toString());
        }

        save();
    }

    @Override
    public List<String> getKeys(String path) {
        try {
            return new ArrayList<>(((Map<String, Object>) get(path)).keySet());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    protected static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        Preconditions.checkArgument(textToTranslate != null, "Cannot translate null text");
        char[] b = textToTranslate.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx#".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }
}
