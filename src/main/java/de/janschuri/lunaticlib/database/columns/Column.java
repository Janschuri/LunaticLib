package de.janschuri.lunaticlib.database.columns;

import de.janschuri.lunaticlib.database.Datatype;

public class Column {

    private final String name;
    private final Datatype datatype;
    private final boolean nullable;
    private final String defaultValue;

    public Column(String name, Datatype datatype, boolean nullable, String defaultValue) {
        this.name = name;
        this.datatype = datatype;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
    }

    public Column(String name, Datatype datatype, boolean nullable) {
        this.name = name;
        this.datatype = datatype;
        this.nullable = nullable;
        this.defaultValue = null;
    }

    public Column(String name, Datatype datatype) {
        this.name = name;
        this.datatype = datatype;
        this.nullable = false;
        this.defaultValue = null;
    }

    public Column(String name) {
        this.name = name;
        this.datatype = Datatype.VARCHAR;
        this.nullable = false;
        this.defaultValue = null;
    }

    public Column(String name, boolean nullable) {
        this.name = name;
        this.datatype = Datatype.VARCHAR;
        this.nullable = nullable;
        this.defaultValue = null;
    }

    public String getName() {
        return name;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
