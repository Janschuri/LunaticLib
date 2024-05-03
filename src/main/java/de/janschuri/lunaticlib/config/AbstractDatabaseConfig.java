package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.utils.Mode;

import java.nio.file.Path;

public abstract class AbstractDatabaseConfig extends Config {

    private final String NAME;
    public String host, database, username, password, filename;
    public int port;
    public boolean useMySQL;
    private final Path dataDirectory;

    public AbstractDatabaseConfig(String name, Path dataDirectory, String DATABASE_FILE, String DEFAULT_DATABASE_FILE) {
        super(dataDirectory, DATABASE_FILE, DEFAULT_DATABASE_FILE);
        this.NAME = name;
        this.dataDirectory = dataDirectory;
        this.load();
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
