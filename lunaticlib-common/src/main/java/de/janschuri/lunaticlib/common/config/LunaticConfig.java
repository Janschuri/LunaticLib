package de.janschuri.lunaticlib.common.config;

import com.google.common.base.Preconditions;
import de.janschuri.lunaticlib.ConfigKey;
import de.janschuri.lunaticlib.common.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LunaticConfig {

    private final Path path;
    private final Path dataDirectory;
    private final String filePath;
    private Map<String, Object> yamlMap = new LinkedHashMap<>();
    private final Map<String, CommentTuple> commentsMap = new HashMap<>();

    public LunaticConfig(Path dataDirectory, String filePath) {
        if (dataDirectory.toFile().isFile()) {
            dataDirectory = dataDirectory.getParent();
        }

        this.path = Path.of(dataDirectory.toString(), filePath);
        this.dataDirectory = dataDirectory;
        this.filePath = filePath;
    }

    private static Yaml getYaml() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setProcessComments(true);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setProcessComments(true);
        dumperOptions.setSplitLines(false); // remove the line breaks
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // remove quotes
        dumperOptions.setIndent(2);
        return new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
    }

    protected static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        Preconditions.checkArgument(textToTranslate != null, "Cannot translate null text");
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx#".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public void load(String defaultFilePath) {

        if (!path.getParent().toFile().exists()) {
            try {
                Files.createDirectories(path.getParent().toFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        File file = path.toFile();

        if (!file.exists()) {
            if (defaultFilePath != null) {
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath)) {
                    if (inputStream != null) {
                        Files.copy(inputStream, file.toPath());
                    } else {
                        throw new IOException("Resource '" + defaultFilePath + "' not found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Files.createFile(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Node root = getNode(file);

            Node defaultRoot = getDefaultNode(defaultFilePath);

            Node newNode;

            if (root == null) {
                newNode = defaultRoot;
            } else {
                Logger.infoLog("Loaded config file: " + path.getParent() + "/" + file.getName());
                newNode = mergeNodes(root, defaultRoot, "");
            }

            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                Yaml yaml = getYaml();
                if (newNode != null) {
                    yaml.serialize(newNode, writer);
                }
            }

            yamlMap = loadYamlMap(newNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public String getFilePath() {
        return filePath;
    }

    public Path getPath() {
        return path;
    }

    private Node getNode(File file) throws IOException {
        Yaml yaml = getYaml();

        if (!file.exists()) {
            file.createNewFile();
        }

        if (file == null) {
            return null;
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                return yaml.compose(reader);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Node getDefaultNode(String defaultFilePath) throws IOException {
        Yaml yaml = getYaml();

        if (defaultFilePath == null) {
            return null;
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath)) {
            if (inputStream != null) {
                try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    return yaml.compose(reader);
                }
            } else {
                throw new IOException("Resource '" + defaultFilePath + "' not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> loadYamlMap(Node root) {
        Map<String, Object> yamlMap = new HashMap<>();

        if (root instanceof MappingNode mappingNode) {
            List<NodeTuple> list = mappingNode.getValue();
            for (NodeTuple node : list) {
                yamlMap.put(((ScalarNode) node.getKeyNode()).getValue(), loadNodeTuple(node));
            }
        }

        return yamlMap;
    }

    protected void save() {
        File file = path.toFile();

        Yaml yaml = getYaml();
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
            Logger.errorLog("Error while loading config file: " + path);
            newNode = savingValues;
        } else {
            Logger.infoLog("Loaded config file: " + path);
            newNode = mergeNodes(savingValues, root, "");
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            yaml.serialize(newNode, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object loadNodeTuple(NodeTuple nodeTuple) {
        if (nodeTuple.getValueNode() instanceof MappingNode mappingNode) {
            Map<String, Object> yamlMap = new HashMap<>();
            List<NodeTuple> list = mappingNode.getValue();
            for (NodeTuple node : list) {
                yamlMap.put(((ScalarNode) node.getKeyNode()).getValue(), loadNodeTuple(node));
            }
            return yamlMap;
        }
        if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode) {
            return scalarNode.getValue();
        }
        if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
            List<Node> nodes = sequenceNode.getValue();
            List<Object> list = new ArrayList<>();
            for (Node node : nodes) {
                if (node instanceof MappingNode) {
                    list.add(loadYamlMap(node));
                }
                if (node instanceof ScalarNode scalarNode) {
                    list.add(scalarNode.getValue());
                }
            }
            return list;
        }
        return null;
    }

    private Node mergeNodes(@NotNull Node node, @NotNull Node defaultNode, String path) {
        if (node == null && defaultNode == null) {
            return null;
        }

        if (defaultNode == null) {
            return node;
        }

        if (node == null) {
            return defaultNode;
        }

        if (node instanceof MappingNode mappingNode && defaultNode instanceof MappingNode defaultMappingNode) {
            List<NodeTuple> list = mappingNode.getValue();
            List<NodeTuple> defaultList = defaultMappingNode.getValue();

            mappingNode.setValue(mergeMappingNodes(list, defaultList, path));

            return mappingNode;
        }

        return null;
    }

    private List<NodeTuple> mergeMappingNodes(List<NodeTuple> list, List<NodeTuple> defaultList, String path) {
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
            String thisPath = path.isEmpty() ? key : path + "." + key;

            if (!map.containsKey(key)) {
                mergedList.add(mergeComments(defaultMap.get(key), null, thisPath));
            } else {
                if (!defaultMap.containsKey(key)) {
                    mergedList.add(mergeComments(map.get(key), null, thisPath));
                } else {
                    NodeTuple nodeTuple = map.get(key);
                    Node node = nodeTuple.getValueNode();
                    NodeTuple defaultNodeTuple = defaultMap.get(key);
                    Node defaultNode = defaultNodeTuple.getValueNode();
                    if (node instanceof MappingNode newMappingNode && defaultNode instanceof MappingNode newDefaultMappingNode) {
                        List<NodeTuple> newList = newMappingNode.getValue();
                        List<NodeTuple> newDefaultList = newDefaultMappingNode.getValue();

                        newMappingNode.setValue(mergeMappingNodes(newList, newDefaultList, thisPath));

                        Node keyNode = nodeTuple.getKeyNode();

                        NodeTuple newNodeTuple = mergeComments(nodeTuple, new NodeTuple(keyNode, newMappingNode), thisPath);
                        mergedList.add(newNodeTuple);
                    } else {
                        NodeTuple newNodeTuple = mergeComments(nodeTuple, defaultNodeTuple, thisPath);
                        mergedList.add(newNodeTuple);
                    }
                }
            }
        }
        return mergedList;
    }

    public NodeTuple mergeComments(NodeTuple nodeTuple, NodeTuple defaultNodeTuple, String path) {
        Node node = nodeTuple.getValueNode();
        Node keyNode = nodeTuple.getKeyNode();

        List<CommentLine> defaultInLineComments = defaultNodeTuple != null ? defaultNodeTuple.getValueNode().getInLineComments() : new ArrayList<>();
        List<CommentLine> defaultBlockComments = defaultNodeTuple != null ? defaultNodeTuple.getValueNode().getBlockComments() : new ArrayList<>();
        List<CommentLine> defaultKeyInLineComments = defaultNodeTuple != null ? defaultNodeTuple.getKeyNode().getInLineComments() : new ArrayList<>();
        List<CommentLine> defaultKeyBlockComments = defaultNodeTuple != null ? defaultNodeTuple.getKeyNode().getBlockComments() : new ArrayList<>();

        node.setInLineComments(mergeCommentLists(node.getInLineComments(), defaultInLineComments, path, true, true));
        node.setBlockComments(mergeCommentLists(node.getBlockComments(), defaultBlockComments, path, false, true));
        keyNode.setInLineComments(mergeCommentLists(keyNode.getInLineComments(), defaultKeyInLineComments, path, true, false));
        keyNode.setBlockComments(mergeCommentLists(keyNode.getBlockComments(), defaultKeyBlockComments, path, false, false));

        return new NodeTuple(keyNode, node);
    }

    private List<CommentLine> mergeCommentLists(List<CommentLine> primary, List<CommentLine> secondary, String path, boolean isInline, boolean isValueNode) {
        if (primary != null && !primary.isEmpty()) {
            return primary;
        }

        primary = secondary;

        if (primary == null) {
            primary = new ArrayList<>();
        }

        if (commentsMap.containsKey(path) && primary.isEmpty()) {
            CommentTuple commentTuple = commentsMap.get(path);

            List<CommentLine> additionalComments;

            if (isValueNode) {
                additionalComments = isInline ? commentTuple.getValueInlineComments() : commentTuple.getValueBlockComments();
            } else {
                additionalComments = isInline ? commentTuple.getKeyInlineComments() : commentTuple.getKeyBlockComments();
            }

            primary.addAll(additionalComments);
        }

        return primary;
    }

    protected void addCommentsFromKey(ConfigKey key) {
        for (String comment : key.getKeyInlineComments()) {
            addKeyComment(CommentType.IN_LINE, key.asString(), comment);
        }

        for (String comment : key.getKeyBlockComments()) {
            addKeyComment(CommentType.BLOCK, key.asString(), comment);
        }

        for (String comment : key.getValueInlineComments()) {
            addComment(CommentType.IN_LINE, key.asString(), comment);
        }

        for (String comment : key.getValueBlockComments()) {
            addComment(CommentType.BLOCK, key.asString(), comment);
        }
    }

    protected void addComment(CommentType type, String path, String comment) {
        commentsMap.computeIfAbsent(path, k -> new CommentTuple()).addComment(type, comment);
    }

    protected void addKeyComment(CommentType type, String path, String comment) {
        commentsMap.computeIfAbsent(path, k -> new CommentTuple()).addKeyComment(type, comment);
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
                return null;
            }
        }

        return current;
    }

    public String getString(String path, String defaultValue) {
        try {
            return get(path) == null ? defaultValue : Objects.requireNonNull(get(path)).toString();
        } catch (Exception e) {
            if (defaultValue != null) {
                return defaultValue;
            }
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public String getString(String path) {
        try {
            return Objects.requireNonNull(get(path)).toString();
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public void setString(String path, String value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = this.yamlMap;

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
    }

    public Integer getInt(String path, int defaultValue) {
        try {
            return get(path) == null ? defaultValue : Integer.parseInt(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

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

    public Double getDouble(String path, double defaultValue) {
        try {
            return get(path) == null ? defaultValue : Double.parseDouble(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

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

    public Boolean getBoolean(String path, boolean defaultValue) {
        try {
            return get(path) == null ? defaultValue : Boolean.parseBoolean(Objects.requireNonNull(get(path)).toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

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

    public List<String> getStringList(String path) {
        try {
            return (List<String>) get(path);
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    public List<Map<String, Object>> getMapList(String path) {
        try {
            return (List<Map<String, Object>>) get(path);
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
    }

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
    }

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
    }

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
    }

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
    }

    public Map<String, Integer> getIntMap(String path) {
        try {
            Map<String, Object> map = getMap(path);
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
    }

    public Map<String, Boolean> getBooleanMap(String path) {
        try {
            Map<String, Object> map = getMap(path);
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
    }

    public List<String> getKeys(String path) {
        try {
            return new ArrayList<>(((Map<String, Object>) get(path)).keySet());
        } catch (Exception e) {
            Logger.errorLog("Error while getting config value: " + path);
            return null;
        }
    }

    private static class CommentTuple {

        List<CommentLine> keyInlineComments = new ArrayList<>();
        List<CommentLine> keyBlockComments = new ArrayList<>();

        List<CommentLine> valueInlineComments = new ArrayList<>();
        List<CommentLine> valueBlockComments = new ArrayList<>();

        public CommentTuple addComment(@NotNull CommentType type, @NotNull String comment) {
            if (type == CommentType.IN_LINE) {
                valueInlineComments.add(new CommentLine(null, null, comment, type));
            } else if (type == CommentType.BLOCK) {
                valueBlockComments.add(new CommentLine(null, null, comment, type));
            }
            return this;
        }

        public CommentTuple addKeyComment(@NotNull CommentType type, @NotNull String comment) {
            if (type == CommentType.IN_LINE) {
                keyInlineComments.add(new CommentLine(null, null, comment, type));
            } else if (type == CommentType.BLOCK) {
                keyBlockComments.add(new CommentLine(null, null, comment, type));
            }
            return this;
        }

        public List<CommentLine> getKeyInlineComments() {
            return keyInlineComments;
        }

        public List<CommentLine> getKeyBlockComments() {
            return keyBlockComments;
        }

        public List<CommentLine> getValueInlineComments() {
            return valueInlineComments;
        }

        public List<CommentLine> getValueBlockComments() {
            return valueBlockComments;
        }

        @Override
        public String toString() {
            return "CommentTuple{" + "\n" +
                    Arrays.toString(keyInlineComments.toArray()) + "\n" +
                    Arrays.toString(keyBlockComments.toArray()) + "\n" +
                    Arrays.toString(valueInlineComments.toArray()) + "\n" +
                    Arrays.toString(valueBlockComments.toArray()) + "\n" +
                    '}';
        }
    }

    protected boolean isDefaultFilePathInResources(String defaultFilePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath)) {
            return inputStream != null;
        } catch (IOException e) {
            return false;
        }
    }
}
