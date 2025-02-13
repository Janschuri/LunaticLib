package de.janschuri.lunaticlib;

import java.util.List;

public interface ConfigKey {
    Object getDefault();
    String asString();
    String getKey();
    List<String> getKeyInlineComments();
    List<String> getKeyBlockComments();
    List<String> getValueInlineComments();
    List<String> getValueBlockComments();
}
