package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.UsuarioDAO;
import model.Monumento;
import model.Usuario;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private Gson gson = new Gson();

    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        //INICIAR SESION
        post("/usuarios/login", (req, res) -> {
            //Obtenemos el email del json devuelto por el cliente:
            JsonObject jsonObject = gson.fromJson(req.body(), JsonObject.class);
            String email = jsonObject.get("email").getAsString();
            System.out.println("Email introducido por el usuario: " + email);
            Usuario user = usuarioDAO.findUsuarioByEmail(email);
            if (user != null) {
                String password = jsonObject.get("password").getAsString();
                if (user.getPassword().equals(password)) {
                    System.out.println("Usuario autenticado: " + user.getEmail());
                    req.session(true).attribute("usuario", user); // Crear sesión y almacenar usuario
                    res.status(200); // HTTP 200 OK
                    return gson.toJson(user);
                }else
                    System.out.println("Contraseña incorrecta para el usuario: " + user.getEmail());
                    res.status(401); // HTTP 401 Unauthorized
            }else
                System.out.println("Usuario no encontrado: " + email);
                res.status(404); // HTTP 404 Not Found
            return "{}";
        });

        // REGISTRAR USUARIO
        post("/usuarios/registro", (req, res) -> {
            // Parsear el cuerpo de la solicitud a un JsonObject
            JsonObject jsonObject = gson.fromJson(req.body(), JsonObject.class);
            String nombre = jsonObject.get("name").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();

            // Verificar si el usuario ya existe
            Usuario usuarioExistente = usuarioDAO.findUsuarioByEmail(email);
            if (usuarioExistente != null) {
                System.out.println("Email ya registrado: " + email);
                res.status(409); // HTTP 409 Conflict
                return "{}";
            }

            // Crear el nuevo usuario
            Usuario nuevoUsuario = new Usuario(nombre, email, password);
            usuarioDAO.insertUsuario(nuevoUsuario);
            System.out.println("Nuevo usuario registrado: " + nuevoUsuario.getEmail());
            req.session(true).attribute("usuario", nuevoUsuario);
            res.status(201); // HTTP 201 Created
            return gson.toJson(nuevoUsuario);
        });


        get("/usuarios/logueado", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            res.type("application/json");
            if (usuario != null) {
                System.out.println("Usuario logueado: " + usuario.getNombre());
                return "{ \"logueado\": true, \"usuario\": \"" + usuario.getNombre() + "\" }";
            } else {
                System.out.println("Ningún usuario logueado");
                return "{ \"logueado\": false }";
            }
        });

        get("/usuarios/perfil", (req, res) -> {
            res.type("application/json");
            Usuario usuario = req.session().attribute("usuario");
            if (usuario != null) {
                return gson.toJson(usuario);
            } else {
                res.status(401); // HTTP 401 Unauthorized
                return "{}";
            }
        });

        post("/usuarios/actualizar", (req, res) -> {
            res.type("application/json");
            Usuario usuarioAntiguo = req.session().attribute("usuario");
            if (usuarioAntiguo == null) {
                System.out.println("Usuario no autorizado");
                res.status(401); // HTTP 401 Unauthorized
                return "{}";
            }
            JsonObject jsonObject = gson.fromJson(req.body(), JsonObject.class);
            String nuevoNombre = jsonObject.get("nombre").getAsString();
            String nuevoEmail = jsonObject.get("email").getAsString();
            String nuevaPassword = jsonObject.get("password").getAsString();

            // Actualizar la información en la base de datos
            if(!nuevoNombre.isEmpty() && !nuevoEmail.isEmpty() && !nuevaPassword.isEmpty()){
                System.out.println("Actualizando perfil de usuario: " + usuarioAntiguo.getEmail());
                for(Usuario u : usuarioDAO.getAllUsuarios()){
                    if(u.getEmail().equals(nuevoEmail)){
                        System.out.println("Email ya registrado: " + nuevoEmail);
                        res.status(409); // HTTP 409 Conflict
                        return "{}";
                    }
                }

            Usuario usuarioNuevo = new Usuario(nuevoNombre, nuevoEmail, nuevaPassword);
            usuarioDAO.updateUsuario(usuarioAntiguo, usuarioNuevo); //se manda el usuario antiguo(el de la request) y el nuevo

            System.out.println("Perfil actualizado: " + usuarioNuevo.getEmail());
            req.session().attribute("usuario", usuarioNuevo);
            return gson.toJson(usuarioNuevo);
            }else{
                res.status(400); // HTTP 400 Bad Request
                return "{}";
            }
        });

        post("/usuarios/logout", (req, res) -> {
            req.session().removeAttribute("usuario");
            req.session().invalidate();
            System.out.println("Sesión cerrada");
            res.status(200); // HTTP 200 OK
            return "Sesión cerrada exitosamente";
        });

        post("/usuarios/eliminar", (req, res) -> {
            Usuario usuario = req.session().attribute("usuario");
            if (usuario == null) {
                System.out.println("Operación no autorizada: Usuario no logueado");
                res.status(401); // HTTP 401 Unauthorized
                return "Usuario no autorizado";
            }

            usuarioDAO.deleteUsuarioByEmail(usuario.getEmail());
            req.session().removeAttribute("usuario");
            req.session().invalidate();
            System.out.println("Usuario eliminado: " + usuario.getEmail());
            res.status(200); // HTTP 200 OK
            return "Usuario eliminado exitosamente";
        });

        // Añadir a favoritos
        post("/usuarios/favoritos/add/:uri", (req, res) -> {
            String uri = req.params(":uri");
            Usuario user = req.session().attribute("usuario");
            System.out.println("Usuario: " + user.getEmail());
            System.out.println("Monumento: " + uri);
            boolean success = usuarioDAO.addFavoriteMonument(user.getEmail(), uri); // Supone manejo de sesión
            if (success) {
                res.status(201); // HTTP 201 Created
                return "{}";
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                return "{}";
            }
        });

        get("/usuarios/favoritos", (req, res) -> {
            Usuario user = req.session().attribute("usuario");
            if (user == null) {
                System.out.println("Operación no autorizada: Usuario no logueado");
                res.status(401); // HTTP 401 Unauthorized
                return "Usuario no autorizado";
            }

            try {
                System.out.println("Obteniendo favoritos para el usuario: " + user.getEmail());
                List<Monumento> favoritos = usuarioDAO.getFavoriteMonuments(user.getEmail());
                if (favoritos.isEmpty()) {
                    System.out.println("No se encontraron favoritos para el usuario: " + user.getEmail());
                    return "No hay monumentos favoritos registrados";
                }
                for(Monumento m : favoritos){
                    System.out.println("Monumento favorito: " + m.getRdfsLabel());
                }
                res.type("application/json");
                return gson.toJson(favoritos);
            } catch (Exception e) {
                System.out.println("Error al obtener los favoritos: " + e.getMessage());
                res.status(500); // HTTP 500 Internal Server Error
                return "{}";
            }
        });

        // Eliminar de favoritos
        post("/usuarios/favoritos/delete/:uri", (req, res) -> {
            String uri = req.params(":uri");
            Usuario user = req.session().attribute("usuario");
            System.out.println("Usuario: " + user.getEmail());
            System.out.println("Monumento: " + uri);
            boolean success = usuarioDAO.removeFavoriteMonument(user.getEmail(), uri); // Supone manejo de sesión
            if (success) {
                res.status(200); // HTTP 200 OK
                return "{}";
            } else {
                res.status(500); // HTTP 500 Internal Server Error
                return "{}";
            }
        });

    }
}
