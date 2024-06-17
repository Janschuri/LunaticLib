package de.janschuri.lunaticlib.common.database;

import de.janschuri.lunaticlib.common.config.LunaticDatabaseConfigImpl;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {

    protected Connection connection;

    private final Table[] tables;

    public Database(Table[] tables) {
        this.tables = tables;
    }

    public static Database getDatabase(LunaticDatabaseConfigImpl config, Table[] tables) {
        Database db;
        if (config.isUseMySQL()) {
            db = new MySQL(config, tables);
            if (db.getSQLConnection() == null) {
                Logger.errorLog("Error initializing MySQL database.");
                return null;
            }
            Logger.infoLog("Successfully initialized MySQL database: " + config.getDatabase());
        } else {
            db = new SQLite(config, tables);
            if (db.getSQLConnection() == null) {
                Logger.errorLog("Error initializing SQLite database.");
                return null;
            }
            Logger.infoLog("Successfully initialized SQLite database: " + config.getFilename() + ".db");
        }
        db.load();
        return db;
    }

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
