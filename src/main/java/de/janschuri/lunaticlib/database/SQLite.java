package de.janschuri.lunaticlib.database;


import de.janschuri.lunaticlib.config.AbstractDatabaseConfig;
import de.janschuri.lunaticlib.database.columns.Column;
import de.janschuri.lunaticlib.database.columns.ForeignKey;
import de.janschuri.lunaticlib.database.columns.PrimaryKey;
import de.janschuri.lunaticlib.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLite extends Database {
    private final String filename;
    private final Path dataDirectory;
    Table[] tables;

    public SQLite(AbstractDatabaseConfig config, Table[] tables) {
        super(tables);
        this.filename = config.getFilename();
        this.dataDirectory = config.getDataDirectory();
        this.tables = tables;
    }

    public void createTables() {
        connection = getSQLConnection();
        for (Table table : tables) {
            try {
                Logger.infoLog("Creating table: " + table.getName());
                Statement stmt = connection.createStatement();

                PrimaryKey primaryKey = table.getPrimaryKey();
                Column[] columns = table.getColumns();

                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("CREATE TABLE IF NOT EXISTS ")
                        .append(table.getName())
                        .append(" (")
                        .append("`").append(primaryKey.getName()).append("` ")
                        .append(getDatatypeString(primaryKey.getDatatype()));

                if (!primaryKey.isNullable()) {
                    sqlBuilder.append(" NOT NULL");
                }

                sqlBuilder.append(" PRIMARY KEY");

                if (primaryKey.isAutoIncrement()) {
                    sqlBuilder.append(" AUTOINCREMENT");
                }

                for (Column column : columns) {
                    sqlBuilder.append(", ")
                            .append("`").append(column.getName()).append("` ")
                            .append(getDatatypeString(column.getDatatype()));

                    if (!column.isNullable()) {
                        sqlBuilder.append(" NOT NULL");
                    }

                    if (column.getDefaultValue() != null) {
                        sqlBuilder.append(" DEFAULT ").append(column.getDefaultValue());
                    }
                }


                for (Column column : columns) {
                    if (column instanceof ForeignKey) {
                        ForeignKey foreignKey = (ForeignKey) column;
                        sqlBuilder.append(", FOREIGN KEY (`").append(column.getName()).append("`) REFERENCES ")
                                .append(foreignKey.getTableName())
                                .append("(").append(foreignKey.getColumnName()).append(")");

                        if (foreignKey.getOnDelete() != null) {
                            sqlBuilder.append(" ON DELETE ").append(foreignKey.getOnDelete());
                        }
                    }
                }



                sqlBuilder.append(");");

                String sql = sqlBuilder.toString();

                Logger.debugLog(sql);

                stmt.execute(sql);

                stmt.close();

            } catch(SQLException e){
                Logger.errorLog("Error creating table: " + table.getName());
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(dataDirectory.toFile(), filename + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Logger.errorLog("File write error: " + filename + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            Logger.errorLog("SQLite exception on initialize");
        } catch (ClassNotFoundException ex) {
            Logger.errorLog("You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        createTables();
        initialize();
    }

    @Override
    protected String getDatatypeString(Datatype datatype) {
        switch (datatype) {
            case INTEGER:
                return "INTEGER";
            case BOOLEAN:
                return "BOOLEAN";
            case DOUBLE:
                return "DOUBLE";
            case FLOAT:
                return "FLOAT";
            case BIGINT:
                return "BIGINT";
            case TIMESTAMP:
                return "TIMESTAMP";
            case CHAR:
                return "CHAR";
            case VARCHAR:
                return "VARCHAR";
            default:
                return "VARCHAR(255)";
        }
    }
}
