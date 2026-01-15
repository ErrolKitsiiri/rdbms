package com.example.rdbms.engine;

import com.example.rdbms.model.Column;
import com.example.rdbms.model.Row;
import com.example.rdbms.model.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Database {
    private final Map<String, Table> tables = new HashMap<>();

    /**
    * Create a new table
    * */

    public void createTable(String tableName, List<Column> columns){
        if (tables.containsKey(tableName)){
            throw new RuntimeException("Table already exists: " + tableName);
        }

        Table table = new Table(tableName);
        for (Column column : columns){
            table.addColumn(column);
        }
        tables.put(tableName, table);
    }


    /**
     * Inserting a row into a table
     *
     * */

    public void insert(String tablename, Map<String, Object> values){
        Table table = getTable(tablename);

        Row row = new Row();

        //validation to ensure all columns are provided
        for (Column column : table.getColumns()){
            String columnName = column.getName();

            if (!values.containsKey(columnName)) {
                throw new RuntimeException("Missing value for column: " + columnName);
            }

            row.setValue(columnName, values.get(columnName));
        }

        // Enforce primary key / unique constraints (naive check)
        for (Column column : table.getColumns()) {
            if (column.isPrimaryKey() || column.isUnique()) {
                Object newValue = row.getValue(column.getName());

                for (Row existingRow : table.getRows()) {
                    Object existingValue = existingRow.getValue(column.getName());
                    if (existingValue != null && existingValue.equals(newValue)) {
                        throw new RuntimeException(
                                "Duplicate value for unique column: " + column.getName()
                        );
                    }
                }
            }
        }

        table.addRow(row);
    }

    /**
     * Select all rows from a table
     */
    public List<Row> selectAll(String tableName) {
        Table table = getTable(tableName);
        return table.getRows();
    }

    /**
     * Select a single record from a table
     */
    public List<Row> selectWhere(String tableName, String columnName, Object value) {
        Table table = getTable(tableName);
        List<Row> result = new ArrayList<>();

        for (Row row : table.getRows()) {
            Object cell = row.getValue(columnName);
            if (cell != null && cell.equals(value)) {
                result.add(row);
            }
        }
        return result;
    }


    /**
     * Update a single row in the Database
     */
    public void update(String tableName, String whereColumn, Object whereValue, String targetColumn, Object newValue){
        Table table = getTable(tableName);

        for (Row row : table.getRows()){
            Object existingValue = row.getValue(whereColumn);
            if (existingValue != null && existingValue.equals(whereValue)){
                row.setValue(targetColumn, newValue);
            }
        }
    }

    public List<Column> selectAllColumns(String tableName) {
        Table table = getTable(tableName);
        return table.getColumns();
    }

    /**
     * Delete rows where column = value
     */
    public void delete(String tableName, String columnName, Object value) {
        Table table = getTable(tableName);

        table.getRows().removeIf(row -> {
            Object rowValue = row.getValue(columnName);
            return rowValue != null && rowValue.equals(value);
        });
    }

    private Table getTable(String tableName) {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new RuntimeException("Table not found: " + tableName);
        }
        return table;
    }


}