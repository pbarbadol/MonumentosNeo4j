package dao;

import model.Usuario;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final Driver driver;

    public UsuarioDAOImpl(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void insertUsuario(Usuario usuario) {
        try (Session session = driver.session()) {
            String query = "CREATE (u:Usuario {nombre: $nombre, email: $email, password: $password})";
            session.run(query, Values.parameters(
                    "nombre", usuario.getNombre(),
                    "email", usuario.getEmail(),
                    "password", usuario.getPassword()
            ));
        }
    }

    @Override
    public Usuario findUsuarioByEmail(String email) {
        try (Session session = driver.session()) {
            String query = "MATCH (u:Usuario {email: $email}) RETURN u.nombre as nombre, u.email as email, u.password as password";
            var result = session.run(query, Values.parameters("email", email));
            if (result.hasNext()) {
                var record = result.next();
                return new Usuario(
                        record.get("nombre").asString(),
                        record.get("email").asString(),
                        record.get("password").asString()
                );
            }
        }
        return null;
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = "MATCH (u:Usuario) RETURN u.nombre as nombre, u.email as email, u.password as password";
            var result = session.run(query);
            while (result.hasNext()) {
                var record = result.next();
                usuarios.add(new Usuario(
                        record.get("nombre").asString(),
                        record.get("email").asString(),
                        record.get("password").asString()
                ));
            }
        }
        return usuarios;
    }

    @Override
    public void updateUsuario(Usuario usuarioAntiguo, Usuario usuarioNuevo) {
        try (Session session = driver.session()) {
            String query = "MATCH (u:Usuario {email: $emailAntiguo}) SET u.nombre = $nombreNuevo, u.email = $emailNuevo, u.password = $passwordNueva";
            session.run(query, Values.parameters(
                    "emailAntiguo", usuarioAntiguo.getEmail(),
                    "emailNuevo", usuarioNuevo.getEmail(), // Se actualiza el email
                    "nombreNuevo", usuarioNuevo.getNombre(),
                    "passwordNueva", usuarioNuevo.getPassword()
            ));
        }
    }

    @Override
    public void deleteUsuarioByEmail(String email) {
        try (Session session = driver.session()) {
            String query = "MATCH (u:Usuario {email: $email}) DELETE u";
            session.run(query, Values.parameters("email", email));
        }
    }
}
