package com.example.rdbms;

import com.example.rdbms.engine.Database;
//import com.example.rdbms.parser.SqlParser;
import com.example.rdbms.repl.Repl;

public class Main {

    public static void main(String[] args) {

        Database db = new Database();
        Repl repl = new Repl(db);
        repl.start();
    }
}
