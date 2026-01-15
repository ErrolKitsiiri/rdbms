package com.example.rdbms.parser;

import com.example.rdbms.engine.Database;
import com.example.rdbms.model.Column;
import com.example.rdbms.model.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlParser {

    private final Database db;

    public SqlParser(Database db) {
        this.db = db;
    }

    // ========================= EXECUTE =========================
    public void execute(String sql) {
        sql = sql
                .trim()
                .replaceAll("\\s+", " ")
                .replace(" (", "(")
                .replace(" )", ")");

        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            parseCreateTable(sql);

        } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
            parseInsert(sql);

        } else if (sql.toUpperCase().startsWith("DELETE FROM")) {
            parseDelete(sql);

        } else if (sql.toUpperCase().startsWith("UPDATE")) {
            parseUpdate(sql);

        } else if (sql.toUpperCase().startsWith("SELECT")) {
            parseSelect(sql);

        } else {
            System.out.println("Unknown command: " + sql);
        }
    }

    // ========================= VALUE PARSER =========================
    private Object parseValue(String tableName, String columnName, String rawValue) {
        List<Column> columns = db.selectAllColumns(tableName);

        Column column = columns.stream()
                .filter(c -> c.getName().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Column not found: " + columnName));

        rawValue = rawValue.trim();

        return switch (column.getType()) {
            case INT -> Integer.parseInt(rawValue);
            case BOOLEAN -> Boolean.parseBoolean(rawValue);
            case STRING -> rawValue.replaceAll("^'|'$", "");
        };
    }

    // ========================= CREATE TABLE =========================
    private void parseCreateTable(String sql) {
        try {
            // Extract table name
            String afterCreate = sql.substring("CREATE TABLE".length()).trim();
            String tableName = afterCreate.substring(0, afterCreate.indexOf("(")).trim();

            // Extract columns
            String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")")).trim();
            String[] cols = columnsPart.split(",");
            List<Column> columns = new ArrayList<>();

            for (String colDef : cols) {
                String[] parts = colDef.trim().split("\\s+");
                String name = parts[0];
                DataType type = DataType.valueOf(parts[1].toUpperCase());

                boolean primaryKey = false;
                boolean unique = false;

                for (int i = 2; i < parts.length; i++) {
                    if (parts[i].equalsIgnoreCase("PRIMARY") && i + 1 < parts.length && parts[i + 1].equalsIgnoreCase("KEY")) {
                        primaryKey = true;
                        i++; // skip "KEY"
                    }
                    if (parts[i].equalsIgnoreCase("UNIQUE")) unique = true;
                }

                columns.add(new Column(name, type, primaryKey, unique));
            }

            db.createTable(tableName, columns);
            System.out.println("Table created: " + tableName);

        } catch (Exception e) {
            System.out.println("Failed to create table: " + e.getMessage());
        }
    }


    // ========================= INSERT =========================
    private void parseInsert(String sql) {
        try {
            String tableName = sql.split("\\s+")[2];

            String valuesPart = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
            String[] valuesArr = valuesPart.split(",");

            Map<String, Object> valuesMap = new java.util.HashMap<>();
            List<Column> cols = db.selectAllColumns(tableName);

            if (valuesArr.length != cols.size()) {
                System.out.println("Column count does not match values count");
                return;
            }

            for (int i = 0; i < cols.size(); i++) {
                Column col = cols.get(i);
                Object parsedValue = parseValue(tableName, col.getName(), valuesArr[i]);
                valuesMap.put(col.getName(), parsedValue);
            }

            db.insert(tableName, valuesMap);
            System.out.println("Row inserted into " + tableName);

        } catch (Exception e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    // ========================= SELECT =========================
    private void parseSelect(String sql) {
        try {
            String upper = sql.toUpperCase();
            String[] parts = sql.split("\\s+");

            // SELECT * FROM table
            if (!upper.contains("WHERE")) {
                String tableName = parts[3];
                db.selectAll(tableName)
                        .forEach(row -> System.out.println(row.getValues()));
                return;
            }

            // SELECT * FROM table WHERE column = value
            String tableName = parts[3];

            String whereClause = sql.substring(upper.indexOf("WHERE") + 5).trim();
            String[] condition = whereClause.split("=");

            if (condition.length != 2) {
                System.out.println("Only simple WHERE conditions supported");
                return;
            }

            String columnName = condition[0].trim();
            String rawValue = condition[1].trim();

            Object value = parseValue(tableName, columnName, rawValue);

            db.selectWhere(tableName, columnName, value)
                    .forEach(row -> System.out.println(row.getValues()));

        } catch (Exception e) {
            System.out.println("Failed to select: " + e.getMessage());
        }
    }


    // ========================= UPDATE =========================
    private void parseUpdate(String sql) {
        try {
            String upper = sql.toUpperCase();

            if (!upper.contains("SET") || !upper.contains("WHERE")) {
                System.out.println("Invalid UPDATE syntax");
                return;
            }

            String tableName = sql.split("\\s+")[1];

            String setPart = sql.substring(upper.indexOf("SET") + 3, upper.indexOf("WHERE")).trim();
            String wherePart = sql.substring(upper.indexOf("WHERE") + 5).trim();

            String[] setTokens = setPart.split("=");
            String[] whereTokens = wherePart.split("=");

            String targetColumn = setTokens[0].trim();
            String newValueRaw = setTokens[1].trim();

            String whereColumn = whereTokens[0].trim();
            String whereValueRaw = whereTokens[1].trim();

            Object newValue = parseValue(tableName, targetColumn, newValueRaw);
            Object whereValue = parseValue(tableName, whereColumn, whereValueRaw);

            db.update(tableName, whereColumn, whereValue, targetColumn, newValue);
            System.out.println("Update executed successfully");

        } catch (Exception e) {
            System.out.println("Failed to update: " + e.getMessage());
        }
    }

    // ========================= DELETE =========================
    private void parseDelete(String sql) {
        try {
            String tableName = sql.split("\\s+")[2];

            String whereClause = sql.substring(sql.toUpperCase().indexOf("WHERE") + 5).trim();
            String[] condition = whereClause.split("=");

            String columnName = condition[0].trim();
            String rawValue = condition[1].trim();

            Object value = rawValue.startsWith("'")
                    ? rawValue.replaceAll("^'|'$", "")
                    : Integer.parseInt(rawValue);

            db.delete(tableName, columnName, value);
            System.out.println("Delete executed successfully");

        } catch (Exception e) {
            System.out.println("Failed to delete: " + e.getMessage());
        }
    }
}
