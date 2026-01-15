package com.example.rdbms.repl;

import com.example.rdbms.parser.SqlParser;
import com.example.rdbms.engine.Database;

import java.util.Scanner;

public class Repl {

    private final SqlParser parser;

    public Repl(Database db){
        this.parser = new SqlParser(db);
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Simple RDBMS. Type 'exit' to quit");

        while (true){
            System.out.println("rdbms> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")){
                System.out.println("Goodbye!!");
                break;
            }
            if (input.isEmpty()){
                continue;
            }

            try {
                parser.execute(input);
            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}