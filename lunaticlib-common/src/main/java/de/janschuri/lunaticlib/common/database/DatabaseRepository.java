package de.janschuri.lunaticlib.common.database;
//
//import de.janschuri.lunaticlib.common.LunaticLib;
//import de.janschuri.lunaticlib.common.config.LunaticDatabaseConfigImpl;
//import de.janschuri.lunaticlib.common.logger.Logger;
//import io.ebean.Database;
//import io.ebean.DatabaseBuilder;
//import io.ebean.DatabaseFactory;
//import io.ebean.config.DatabaseConfig;
//import io.ebean.datasource.DataSourceConfig;
//import io.ebean.platform.sqlite.SQLitePlatform;

import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.config.LunaticDatabaseConfigImpl;
import de.janschuri.lunaticlib.common.logger.Logger;
import io.ebean.Database;
import io.ebean.DatabaseBuilder;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.platform.sqlite.SQLitePlatform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static io.ebean.DatabaseFactory.*;

public abstract class DatabaseRepository {

    protected Connection connection;

    private final Table[] tables;

    public DatabaseRepository(Table[] tables) {
        this.tables = tables;
    }

//    public static Database getDatabase(LunaticDatabaseConfigImpl databaseConfig, Set<Class<?>> classes) {
//        Logger.infoLog("Loading database...");
//        databaseConfig.load();
//
//        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setUsername(databaseConfig.getUsername());
//        dataSourceConfig.setPassword(databaseConfig.getPassword());
//        dataSourceConfig.setUrl("jdbc:sqlite:./plugins/LunaticFamily/" + databaseConfig.getFilename() + ".db");
//        dataSourceConfig.setDriver("org.sqlite.JDBC");
//
//        DatabaseConfig config = new io.ebean.config.DatabaseConfig();
//        config.setName("db");
//        config.setDataSourceConfig(dataSourceConfig);
//        config.setDefaultServer(true);
//        config.setRegister(true);
//        config.setDatabasePlatform(new SQLitePlatform());
//
//        // Add entity classes
//        config.setClasses(classes);
//
//        // Enable automatic DDL generation and execution
//        config.setDdlGenerate(true);
//        config.setDdlRun(true);
//
//        DatabaseBuilder.Settings settings = config.settings();
//
//
//        ClassLoader classLoader = LunaticLib.class.getClassLoader();
//
//        Logger.infoLog("Classloader: " + classLoader);
//
//        // Create the Ebean database instance
//        Database db = createWithContextClassLoader(config, classLoader);
////        Database db = DatabaseFactory.create(config);
//
//        Logger.infoLog("Database loaded.");
//        return db;
//    }

//    public static Database getDatabase(LunaticDatabaseConfigImpl config, Table[] tables) {
//        Database db;
//        if (config.isUseMySQL()) {
//            db = new MySQL(config, tables);
//            if (db.getSQLConnection() == null) {
//                Logger.errorLog("Error initializing MySQL database.");
//                return null;
//            }
//            Logger.infoLog("Successfully initialized MySQL database: " + config.getDatabase());
//        } else {
//            db = new SQLite(config, tables);
//            if (db.getSQLConnection() == null) {
//                Logger.errorLog("Error initializing SQLite database.");
//                return null;
//            }
//            Logger.infoLog("Successfully initialized SQLite database: " + config.getFilename() + ".db");
//        }
//        db.load();
//        return db;
//    }

    public void initialize() {
        connection = getSQLConnection();
        try {
            for (Table table : tables) {
                PreparedStatement psPlayerData = connection.prepareStatement("SELECT * FROM " + table.getName());
                ResultSet rsPlayerData = psPlayerData.executeQuery();
                close(psPlayerData, rsPlayerData);
            }

        } catch (SQLException ex) {
            Error.noConnection(ex);
        }
    }

    public abstract Connection getSQLConnection();

    protected abstract void load();
    protected abstract String getDatatypeString(Datatype datatype);
    protected abstract void addMissingColumns();
    protected abstract void createTables();

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(ex);
        }
    }
}
