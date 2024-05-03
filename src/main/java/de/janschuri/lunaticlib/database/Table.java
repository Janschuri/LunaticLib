package de.janschuri.lunaticlib.database;

import de.janschuri.lunaticlib.database.columns.Column;
import de.janschuri.lunaticlib.database.columns.PrimaryKey;

public class Table {

    private final String name;
    private final PrimaryKey primaryKey;
    private final Column[] columns;

    public Table(String name, PrimaryKey primaryKey, Column[] columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Column[] getColumns() {
        return columns;
    }
}
