package controller;

import com.google.gson.Gson;
import dao.UsuarioDAO;
import model.Usuario;
import static spark.Spark.*;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private Gson gson = new Gson();

    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        //PARA CREAR CUENTA
        post("/usuarios/signup", (req, res) -> {
            Usuario usuario = gson.fromJson(req.body(), Usuario.class);
            usuarioDAO.insertUsuario(usuario);
            res.status(201); // HTTP 201 Created
            return gson.toJson(usuario);
        });

        //PARA OBTENER TODOS LOS USUARIOS
        get("/usuarios", (req, res) -> {
            res.type("application/json");
            return gson.toJson(usuarioDAO.getAllUsuarios());
        });

        //PARA OBTENER USUARIOS POR EMAIL
        get("/usuarios/:email", (req, res) -> {
            res.type("application/json");
            Usuario usuario = usuarioDAO.findUsuarioByEmail(req.params(":email"));
            if (usuario != null) {
                return gson.toJson(usuario);
            }
            res.status(404); // HTTP 404 Not Found
            return "{}";
        });

        //PARA INICIAR SESION
        get("/usuarios/login/:email/:password", (req, res) -> {
            res.type("application/json");
            Usuario usuario = usuarioDAO.findUsuarioByEmail(req.params(":email"));
            if (usuario != null && usuario.getPassword().equals(req.params(":password"))) {
                return gson.toJson(usuario);
            }
            res.status(404); // HTTP 404 Not Found
            return "{}";
        });

        //PARA ACTUALIZAR USUARIOS
        put("/usuarios/:email", (req, res) -> {
            String email = req.params(":email");
            Usuario updatedUsuario = gson.fromJson(req.body(), Usuario.class);
            Usuario originalUsuario = usuarioDAO.findUsuarioByEmail(email);
            if (originalUsuario != null) {
                originalUsuario.setNombre(updatedUsuario.getNombre());
                originalUsuario.setEmail(updatedUsuario.getEmail());
                originalUsuario.setPassword(updatedUsuario.getPassword());
                usuarioDAO.updateUsuario(originalUsuario);
                return gson.toJson(originalUsuario);
            }
            res.status(404); // HTTP 404 Not Found
            return "{}";
        });

        //PARA ELIMINAR USUARIOS
        delete("/usuarios/:email", (req, res) -> {
            usuarioDAO.deleteUsuarioByEmail(req.params(":email"));
            res.status(204); // HTTP 204 No Content
            return "{}";
        });
    }
}
