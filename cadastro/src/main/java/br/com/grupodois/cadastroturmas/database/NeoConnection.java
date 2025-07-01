package br.com.grupodois.cadastroturmas.database;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class NeoConnection {
    private static final String URI = "bolt://localhost:7687"; // ou bolt+s:// para cloud
    private static final String USER = "neo4j";
    private static final String PASSWORD = "w1234567";

    private static Driver driver;

    public static Driver getDriver() {
        if (driver == null) {
            driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
        }
        return driver;
    }

    public static void close() {
        if (driver != null) {
            driver.close();
        }
    }
}
