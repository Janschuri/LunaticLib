package de.janschuri.lunaticlib.common.database.columns;

import de.janschuri.lunaticlib.common.database.Datatype;

public class PrimaryKey extends Column {

    private final boolean autoIncrement;
    private final String name;
    private final Datatype datatype;

    public PrimaryKey(String name, Datatype datatype, boolean autoIncrement) {
        super(name, datatype, autoIncrement, null);
        this.name = name;
        this.datatype = datatype;
        this.autoIncrement = autoIncrement;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getName() {
        return name;
    }

    public Datatype getDatatype() {
        return datatype;
    }
}
