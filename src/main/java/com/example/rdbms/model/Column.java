package com.example.rdbms.model;

public class Column {
    private final String name;
    private final DataType type;
    private final boolean primaryKey;
    private final boolean unique;

    public Column(String name, DataType type, boolean primaryKey, boolean unique){
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isUnique() {
        return unique;
    }
}