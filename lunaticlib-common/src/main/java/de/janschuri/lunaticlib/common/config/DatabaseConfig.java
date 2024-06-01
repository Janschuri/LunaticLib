package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.platform.PlatformType;

import java.nio.file.Path;

public class DatabaseConfig extends AbstractConfig implements de.janschuri.lunaticlib.DatabaseConfig {

    private final String NAME;
    private String host, database, username, password, filename;
    private int port;
    private boolean useMySQL;
    private final Path dataDirectory;

    protected DatabaseConfig(String name, Path dataDirectory, String DATABASE_FILE, String DEFAULT_DATABASE_FILE) {
        super(dataDirectory, DATABASE_FILE, DEFAULT_DATABASE_FILE);
        this.NAME = name;
        this.dataDirectory = dataDirectory;
    }

    @Override
    public void load() {
        super.load();
        host = getString("MySQL.host", "localhost");
        port = getInt("MySQL.port", 3306);
        database = getString("MySQL.database", NAME);
        username = getString("MySQL.username", "root");
        password = getString("MySQL.password", "");
        filename = getString("SQLite.filename", NAME);
        useMySQL = getBoolean("MySQL.enabled", false);

        if (LunaticLib.getPlatform().getPlatformType() == PlatformType.VELOCITY) {
            useMySQL = true;
        }
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFilename() {
        return filename;
    }

    public int getPort() {
        return port;
    }

    public boolean isUseMySQL() {
        return useMySQL;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }
}
