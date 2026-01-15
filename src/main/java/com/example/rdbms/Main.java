package com.example.rdbms;

import com.example.rdbms.engine.Database;
import com.example.rdbms.parser.SqlParser;


public class Main {

    public static void main(String[] args) {

        Database db = new Database();
        SqlParser parser = new SqlParser(db);

        parser.execute("CREATE TABLE users (id INT PRIMARY KEY, email STRING UNIQUE)");
        parser.execute("INSERT INTO users VALUES (1, 'a@mail.com')");
        parser.execute("INSERT INTO users VALUES (2, 'b@mail.com')");
        parser.execute("SELECT * FROM users");
    }
}
