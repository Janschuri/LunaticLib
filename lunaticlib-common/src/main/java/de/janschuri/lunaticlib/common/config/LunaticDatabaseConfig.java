package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.platform.PlatformType;

import java.nio.file.Path;

public class LunaticDatabaseConfig extends LunaticConfig {

    private String host, database, username, password, filename;
    private int port;
    private boolean useMySQL;

    protected LunaticDatabaseConfig(Path dataDirectory, String filepath) {
        super(dataDirectory, filepath);
    }

    public void load(String defaultDatabaseFile) {
        super.load(defaultDatabaseFile);
        host = getString("MySQL.host", "localhost");
        port = getInt("MySQL.port", 3306);
        database = getString("MySQL.database", defaultName());
        username = getString("MySQL.username", "root");
        password = getString("MySQL.password", "");
        useMySQL = getBoolean("MySQL.enabled", false);

        if (LunaticLib.getPlatform().getPlatformType() == PlatformType.VELOCITY) {
            useMySQL = true;
        } else {
            filename = getString("SQLite.filename", defaultName());
        }
    }

    protected String defaultName() {
        return "database";
    }

    public String getType() {
        return useMySQL ? "mysql" : "sqlite";
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
}
