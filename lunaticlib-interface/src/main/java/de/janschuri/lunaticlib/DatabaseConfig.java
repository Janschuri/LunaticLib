package de.janschuri.lunaticlib;

import java.nio.file.Path;

public interface DatabaseConfig extends Config {

    String getHost();
    String getDatabase();
    String getUsername();
    String getPassword();
    String getFilename();
    int getPort();
    boolean isUseMySQL();
    Path getDataDirectory();
}
