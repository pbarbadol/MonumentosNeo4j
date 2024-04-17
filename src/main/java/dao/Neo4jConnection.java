package dao;

import model.Monumento;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;

public class Neo4jConnection {
    private final Driver driver;

    // Constructor para inicializar la conexión con Neo4j
    public Neo4jConnection(String uri, String username, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

    // Método para cerrar la conexión
    public void close() {
        if (driver != null) {
            driver.close();
        }
    }

    public Driver getDriver() {
        return driver;
    }

}
