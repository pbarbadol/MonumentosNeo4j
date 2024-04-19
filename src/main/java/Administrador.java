import dao.Neo4jConnection;
import dao.MonumentoDAOimpl;
import lectura.MonumentoParser;
import model.Monumento;

import java.util.List;

public class Administrador {

    public static void main(String[] args) {
        Neo4jConnection neo4jConnection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "monumento"); // Cambia estos datos si es necesario
        MonumentoDAOimpl monumentoDAOimpl = new MonumentoDAOimpl(neo4jConnection.getDriver());
        try {

            List<Monumento> monumentos = MonumentoParser.parseMonumentos("src/main/resources/monumentos.json");

            monumentoDAOimpl.insertMonumentos(monumentos);

            monumentos.clear();

            List<Monumento> arcos = monumentoDAOimpl.getMonumentosByClase("arco ");
            for (Monumento arco : arcos) {
                System.out.println(arco);
            }

            monumentoDAOimpl.connectNearbyMonuments(50);
        } finally {
            neo4jConnection.close(); // Siempre hay que cerrar la conexión
        }
    }
}