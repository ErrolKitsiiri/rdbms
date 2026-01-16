# Simple RDBMS in Java

## Overview
This project is a **mini relational database management system (RDBMS)** implemented in **Java**.  
It supports:

- Declaring tables with columns and data types (`INT`, `STRING`, `BOOLEAN`)
- CRUD operations:
    - `CREATE TABLE`
    - `INSERT INTO`
    - `SELECT * FROM`
    - `SELECT * FROM table WHERE column = value`
    - `UPDATE table SET column = value WHERE column = value`
    - `DELETE FROM table WHERE column = value`
- Primary key and unique key constraints
- A simple **interactive REPL** (Read-Evaluate-Print Loop)
- SQL-like syntax for commands
- Designed for demonstration and learning purposes

The purpose of this project is to **demonstrate understanding of database concepts, Java programming, and system design**. It is **in-memory**, so all data is lost when the application stops.

---

## Project Structure
simple-rdbms
├── src/main/java/com/example/rdbms
│ ├── model # Table, Column(Schema metadata is immutable by design), Row, DataType classes
│ ├── engine # Database engine (CRUD operations)
│ ├── parser # SQL-like parser
│ ├── repl # Interactive REPL interface
│ └── Main.java # Program entry point
└── README.md

**Package Roles:**

- `model` → Represents tables, rows, columns, and data types
- `engine` → Handles CRUD operations and enforces constraints
- `parser` → Converts SQL-like text commands into actions
- `repl` → Interactive command-line interface

---

## Features

1. **Create tables dynamically**
   ```sql
   CREATE TABLE users (id INT PRIMARY KEY, email STRING UNIQUE)
   
2. **Insert records**
   ```sql
   INSERT INTO users VALUES (1, 'a@mail.com')
   INSERT INTO users VALUES (2, 'b@mail.com')
   
3. **Select records**
    ```sql
   SELECT * FROM users
   
4. **Update records**
    ```sql
   UPDATE users SET email = 'x@mail.com' WHERE id = 1
   
5. **Delete records**
    ```sql
   DELETE FROM users WHERE id = 2

6. **Interactive REPL** (Before maven compilation)
    - Start the program and run the commands directly
    - Type exit to quit

## Requirements
- Java JDK 17 or higher, tested with 21
- Maven 3.x
- Windows or Linux, (tested on Windows PowerShell)

## Compiling and Running

### Using Maven
1. Compile the project - mvn clean compile
2. Run the REPL directly via maven - mvn exec:java

This uses the exec-maven-plugin and the compiled classes in target\classes. No need to manually manage classpaths

### Using javac and java (Manual)

1. Compile model classes - javac src\main\java\com\example\rdbms\model\*.java
2. Compile engine classes - javac -cp src\main\java src\main\java\com\example\rdbms\engine\*.java
3. Compile parser classes- javac -cp src\main\java src\main\java\com\example\rdbms/parser/*.java
4. Compile main class- javac -cp src\main\java src\main\java\com\example\rdbms\Main.java

## Running the Program
1. java -cp src\main\java com.example.rdbms.Main

## Example REPL Session
Welcome to Simple RDBMS. Type 'exit' to quit.
rdbms> CREATE TABLE users (id INT PRIMARY KEY, email STRING UNIQUE)
Table created: users
rdbms> INSERT INTO users VALUES (1, 'a@mail.com')
Row inserted into users
rdbms> INSERT INTO users VALUES (2, 'b@mail.com')
Row inserted into users
rdbms> SELECT * FROM users
{id=1, email=a@mail.com}
{id=2, email=b@mail.com}
rdbms> UPDATE users SET email='x@mail.com' WHERE id=1
Update executed successfully
rdbms> SELECT * FROM users WHERE id=1
{id=1, email=x@mail.com}
rdbms> DELETE FROM users WHERE id=2
Delete executed successfully
rdbms> exit
Goodbye!


## Design 
- Clear separation of concerns (model, engine, parser, REPL)
- Immutable schema metadata for safety
- Minimal SQL parsing to focus on core database concepts
- No external dependencies

## Notes
- This RDBMS is in-memory only. No persistence to disk.
- SQL support is limited for demonstration:
  Supports CREATE TABLE, INSERT INTO, SELECT * FROM , SELECT * FROM table WHERE column = value, UPDATE table SET column = value WHERE column = value, DELETE FROM table WHERE column = value and 
  Basic primary key and unique constraints enforced
- Future extensions could include:
  - WHERE conditions
  - Joins 
  - Persistent storage 
  - More data types
- Ensure java.version is set to 17+ in pom.xml to support switch expressions.
- Use mvn exec:java for running the REPL instead of running raw .java files.
- Compiled classes are in target\classes.
  
### Commands Used While Creating the project
1. mkdir -p src/main/java/com/example/rdbms
2. java -cp src/main/java com.example.rdbms.Main
3. mkdir src\main\java\com\example\rdbms\model
4. mkdir src\main\java\com\example\rdbms\engine
5. mkdir src\main\java\com\example\rdbms\parser
6. mkdir src\main\java\com\example\rdbms\repl
