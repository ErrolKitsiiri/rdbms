package com.example.rdbms.parser;

import com.example.rdbms.engine.Database;
import com.example.rdbms.model.Column;
import com.example.rdbms.model.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlParser {
    
    private final Database db;
    
    public SqlParser(Database db){
        this.db = db;
    }
    
    public void execute(String sql){
        sql = sql.trim();
        
        if (sql.toUpperCase().startsWith("CREATE TABLE")){
            parseCreateTable(sql);
        } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
            parseInsert(sql);
        } else if (sql.toUpperCase().startsWith("SELECT")) {
            parseSelect(sql);
        } else {
            System.out.println("Unknown command: " + sql);
        }
    }

    // ========================= CREATE TABLE =========================
    private void parseCreateTable(String sql) {
        try {
            // Example: CREATE TABLE users (id INT PRIMARY KEY, email STRING UNIQUE)
            String inside = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")")).trim();
            String tableName = sql.split("\\s+")[2];

            String[] cols = inside.split(",");
            List<Column> columns = new ArrayList<>();

            for (String colDef : cols) {
                colDef = colDef.trim();
                String[] parts = colDef.split("\\s+");
                String name = parts[0];
                DataType type = DataType.valueOf(parts[1].toUpperCase());
                boolean primaryKey = false;
                boolean unique = false;

                for (int i = 2; i < parts.length; i++) {
                    if (parts[i].equalsIgnoreCase("PRIMARY")) primaryKey = true;
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
            // Example: INSERT INTO users VALUES (1, 'a@mail.com')
            String tableName = sql.split("\\s+")[2];

            String valuesPart = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")")).trim();
            String[] valuesArr = valuesPart.split(",");

            Map<String, Object> valuesMap = new java.util.HashMap<>();
            List<Column> cols = db.selectAllColumns(tableName); // helper we will add

            if (valuesArr.length != cols.size()) {
                System.out.println("Column count does not match values count");
                return;
            }

            for (int i = 0; i < cols.size(); i++) {
                Column col = cols.get(i);
                String val = valuesArr[i].trim();

                Object parsedValue;
                switch (col.getType()) {
                    case INT -> parsedValue = Integer.parseInt(val);
                    case BOOLEAN -> parsedValue = Boolean.parseBoolean(val);
                    case STRING -> parsedValue = val.replaceAll("^'|'$", "");
                    default -> parsedValue = val;
                }

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
            // Only support: SELECT * FROM tableName
            String[] parts = sql.split("\\s+");
            String tableName = parts[3];
            db.selectAll(tableName).forEach(row -> System.out.println(row.getValues()));
        } catch (Exception e) {
            System.out.println("Failed to select: " + e.getMessage());
        }
    }

}