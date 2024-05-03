package de.janschuri.lunaticlib.database.columns;

import de.janschuri.lunaticlib.database.Datatype;

public class ForeignKey extends Column {

    private final String tableName;
    private final String columnName;
    private final String onDelete;

    public ForeignKey(String name, Datatype datatype, boolean nullable, String defaultValue, String tableName, String columnName, String onDelete) {
        super(name, datatype, nullable, defaultValue);
        this.tableName = tableName;
        this.columnName = columnName;
        this.onDelete = onDelete;
    }

    public ForeignKey(String name, Datatype datatype, boolean nullable, String tableName, String columnName, String onDelete) {
        super(name, datatype, nullable);
        this.tableName = tableName;
        this.columnName = columnName;
        this.onDelete = onDelete;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOnDelete() {
        return onDelete;
    }
}
