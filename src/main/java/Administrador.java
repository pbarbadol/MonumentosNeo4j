import dao.Neo4jConnection;
import dao.MonumentoDAOimpl;
import dao.UsuarioDAOImpl;
import lectura.MonumentoParser;
import model.Monumento;
import model.Usuario;

import java.util.List;

public class Administrador {

    public static void main(String[] args) {
        Neo4jConnection neo4jConnection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "monumento"); // Cambia estos datos si es necesario
        MonumentoDAOimpl monumentoDAOimpl = new MonumentoDAOimpl(neo4jConnection.getDriver());
        UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl(neo4jConnection.getDriver());
        try {

            List<Monumento> monumentos = MonumentoParser.parseMonumentos("src/main/resources/monumentos.json");

            for(Monumento monumento : monumentos) {
                System.out.println(monumento);
            }

            monumentoDAOimpl.insertMonumentos(monumentos);
            usuarioDAOImpl.insertUsuario(new Usuario("Pablo", "pp@pp.com", "pp123"));
            usuarioDAOImpl.insertUsuario(new Usuario("Iker", "ii@ii.com", "ii123"));
            usuarioDAOImpl.insertUsuario(new Usuario("Fran", "ff@ff.com", "ff123"));
            usuarioDAOImpl.insertUsuario(new Usuario("Alberto", "aa@aa.com", "aa123"));
            usuarioDAOImpl.insertUsuario(new Usuario("Javier", "jj@jj.com", "jj123"));

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