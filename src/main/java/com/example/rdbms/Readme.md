### RDBMS Project
This project simulates a sample RDBMS for MySQL
### Running the project

javac src\main\java\com\example\rdbms\model\*.java
javac -cp src\main\java src\main\java\com\example\rdbms\engine\*.java
javac -cp src\main\java src\main\java\com\example\rdbms/parser/*.java
javac -cp src\main\java src\main\java\com\example\rdbms\Main.java

java -cp src\main\java com.example.rdbms.Main

### Commands Used
1. mkdir -p src/main/java/com/example/rdbms
2. java -cp src/main/java com.example.rdbms.Main
3. mkdir src\main\java\com\example\rdbms\model
4. mkdir src\main\java\com\example\rdbms\engine
5. mkdir src\main\java\com\example\rdbms\parser
6. mkdir src\main\java\com\example\rdbms\repl
