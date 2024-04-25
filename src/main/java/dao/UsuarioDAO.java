package dao;

import model.Monumento;
import model.Usuario;

import java.util.List;

public interface UsuarioDAO {
    void insertUsuario(Usuario usuario);
    Usuario findUsuarioByEmail(String email);
    List<Usuario> getAllUsuarios();
    void updateUsuario(Usuario usuario, Usuario usuarioNuevo);
    void deleteUsuarioByEmail(String email);
    boolean addFavoriteMonument(String email, String uri);
    List<Monumento> getFavoriteMonuments(String email);
    boolean removeFavoriteMonument(String email, String uri);
}
