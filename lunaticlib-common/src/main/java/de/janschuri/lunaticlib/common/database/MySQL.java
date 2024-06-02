package de.janschuri.lunaticlib.common.database;

import de.janschuri.lunaticlib.common.database.columns.Column;
import de.janschuri.lunaticlib.common.database.columns.ForeignKey;
import de.janschuri.lunaticlib.common.database.columns.PrimaryKey;
import de.janschuri.lunaticlib.common.config.LunaticDatabaseConfigImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database {
    private final String host, database, username, password;
    private final int port;
    private final Table[] tables;

    public MySQL(LunaticDatabaseConfigImpl config, Table[] tables) {
        super(tables);
        this.host = config.getHost();
        this.database = config.getDatabase();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.port = config.getPort();
        this.tables = tables;
    }

    public void createTables() {
        connection = getSQLConnection();
        try {
            Statement stmt = connection.createStatement();
            for (Table table : tables) {
                PrimaryKey primaryKey = table.getPrimaryKey();
                Column[] columns = table.getColumns();

                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("CREATE TABLE IF NOT EXISTS ")
                        .append(table.getName())
                        .append(" (")
                        .append("`").append(primaryKey.getName()).append("` ")
                        .append(getDatatypeString(primaryKey.getDatatype()));

                if (primaryKey.isAutoIncrement()) {
                    sqlBuilder.append(" AUTO_INCREMENT");
                }

                sqlBuilder.append(" PRIMARY KEY");

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


                sqlBuilder.append(")");

                if (primaryKey.isAutoIncrement()) {
                    sqlBuilder.append(" AUTO_INCREMENT=1");
                }

                sqlBuilder.append(";");

                String sql = sqlBuilder.toString();

                stmt.execute(sql);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getSQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", username, password);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
            stmt.close();

            conn.setCatalog(database);

            return conn;
        } catch (SQLException | ClassNotFoundException ex) {
            Error.errorOnInitilization(ex);
        }
        return null;
    }

    protected String getDatatypeString(Datatype datatype) {
        switch (datatype) {
            case INTEGER:
                return "INT";
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
                return "VARCHAR(255)";
            case VARBINARY:
                return "VARBINARY(1000)";
            default:
                return "VARCHAR(255)";
        }
    }

    public void load() {
        connection = getSQLConnection();
        createTables();
        initialize();
    }
}

