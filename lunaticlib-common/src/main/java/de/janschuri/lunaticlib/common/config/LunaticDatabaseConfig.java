package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.ConfigKey;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.PlatformType;

import java.nio.file.Path;

public class LunaticDatabaseConfig extends LunaticConfig {

    private String host, database, username, password, filename;
    private int port;
    private boolean useMySQL;

    private boolean missingSQLite = false;

    protected ConfigKey enabledKey = new LunaticConfigKey("MySQL.enabled").defaultValue(false);
    protected ConfigKey hostKey = new LunaticConfigKey("MySQL.host").defaultValue("localhost");
    protected ConfigKey portKey = new LunaticConfigKey("MySQL.port").defaultValue(3306);
    protected ConfigKey databaseKey = new LunaticConfigKey("MySQL.database").defaultValue(defaultName());
    protected ConfigKey usernameKey = new LunaticConfigKey("MySQL.username").defaultValue("root");
    protected ConfigKey passwordKey = new LunaticConfigKey("MySQL.password").defaultValue("");
    protected ConfigKey sqliteKey = new LunaticConfigKey("SQLite.filename").defaultValue(defaultName());

    protected LunaticDatabaseConfig(Path dataDirectory, String filepath) {
        super(dataDirectory, filepath);

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            sqliteKey = null;
            missingSQLite = true;
        }
    }

    public void load() {
        super.load(null);
        host = getString("MySQL.host", "localhost");
        port = getInt("MySQL.port", 3306);
        database = getString("MySQL.database", defaultName());
        username = getString("MySQL.username", "root");
        password = getString("MySQL.password", "");
        useMySQL = getBoolean("MySQL.enabled", false);

        if (missingSQLite) {
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
