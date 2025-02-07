package de.janschuri.lunaticlib.common.database;


import de.janschuri.lunaticlib.common.database.columns.Column;
import de.janschuri.lunaticlib.common.database.columns.ForeignKey;
import de.janschuri.lunaticlib.common.database.columns.PrimaryKey;
import de.janschuri.lunaticlib.common.config.LunaticDatabaseConfigImpl;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;


public class SQLite extends DatabaseRepository {
    private final String filename;
    private final Path dataDirectory;
    private final Table[] tables;

    public SQLite(LunaticDatabaseConfigImpl config, Table[] tables) {
        super(tables);
        this.filename = config.getFilename();
        this.dataDirectory = config.getDataDirectory();
        this.tables = tables;
    }

    @Override
    public void createTables() {
        connection = getSQLConnection();
        for (Table table : tables) {
            try {
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
                } else {
                    sqlBuilder.append(" NULL");
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



                sqlBuilder.append(");");

                String sql = sqlBuilder.toString();

                stmt.execute(sql);

                stmt.close();

            } catch(SQLException e){
                Logger.errorLog("Error creating table: " + table.getName());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getSQLConnection() {
        File dataFolder = new File(dataDirectory.toFile(), filename + ".db");
        if (!dataFolder.exists()) {
            try {
                Logger.debugLog("Creating SQLite file: " + dataDirectory + "\\" + filename + ".db");
                dataFolder.createNewFile();
            } catch (IOException e) {
                Logger.errorLog("File write error: " + dataDirectory + "\\" + filename + ".db");
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
            Logger.errorLog("Class not found exception: org.sqlite.JDBC");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        createTables();
        addMissingColumns();
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
            case VARBINARY:
                return "VARBINARY";
            default:
                return "VARCHAR(255)";
        }
    }

    @Override
    protected void addMissingColumns() {
        for (Table table : tables) {

            if(connection == null) {
                connection = getSQLConnection();
            }

            for (Column column : table.getColumns()) {
                try {
                    StringBuilder sqlBuilder = new StringBuilder();
                    sqlBuilder.append("ALTER TABLE ")
                            .append(table.getName())
                            .append(" ADD COLUMN ")
                            .append(column.getName())
                            .append(" ")
                            .append(getDatatypeString(column.getDatatype()));

                    if (!column.isNullable()) {
                        sqlBuilder.append(" NOT NULL");
                    }

                    if (column.getDefaultValue() != null) {
                        sqlBuilder.append(" DEFAULT ").append(column.getDefaultValue());
                    }

                    if (column instanceof ForeignKey) {
                        ForeignKey foreignKey = (ForeignKey) column;
                        sqlBuilder.append(" REFERENCES ")
                                .append(foreignKey.getTableName())
                                .append("(")
                                .append(foreignKey.getColumnName())
                                .append(")");

                        if (foreignKey.getOnDelete() != null) {
                            sqlBuilder.append(" ON DELETE ").append(foreignKey.getOnDelete());
                        }
                    }

                    sqlBuilder.append(";");

                    Logger.debugLog(sqlBuilder.toString());

                    PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString());
                    ps.execute();
                    ps.close();

                } catch (SQLException e) {
                    if (!e.getMessage().contains("duplicate column")) {
                        Error.addForeignKey(e);
                    }
                }
            }
        }
    }
}
