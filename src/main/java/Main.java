import controller.MonumentoController;
import dao.MonumentoDAOimpl;
import dao.Neo4jConnection;
import dao.UsuarioDAOImpl;
import spark.Spark;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Spark.port(8080); // Configura el puerto antes de cualquier ruta
        Spark.staticFiles.externalLocation(new File("src/main/webapp").getAbsolutePath());

        Neo4jConnection neo4jConnection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "monumento");
        try {
            MonumentoDAOimpl monumentoDAOimpl = new MonumentoDAOimpl(neo4jConnection.getDriver());
            UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl(neo4jConnection.getDriver());

            // Inicializar el controlador con los DAOs
            new MonumentoController(monumentoDAOimpl);

            Spark.get("/", (req, res) -> {
                res.redirect("index.html"); // Redirige la ruta raíz al archivo HTML
                return null;
            });

            System.out.println("Servidor corriendo en puerto 8080");
        } finally {
            //neo4jConnection.close(); // Asegúrate de cerrar la conexión
        }
    }
}
