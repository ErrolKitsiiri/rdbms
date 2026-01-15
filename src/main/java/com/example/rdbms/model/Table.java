package com.example.rdbms.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String name;
    private final List<Column> columns = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();

    public Table(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void addRow(Row row) {
        rows.add(row);
    }

}