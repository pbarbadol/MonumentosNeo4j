package controller;

import com.google.gson.Gson;
import dao.MonumentoDAO;
import model.Monumento;
import static spark.Spark.*;

public class MonumentoController {
    private MonumentoDAO monumentoDAO;
    private Gson gson = new Gson();

    public MonumentoController(MonumentoDAO monumentoDAO) {
        this.monumentoDAO = monumentoDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post("/monumentos", (req, res) -> {
            Monumento monumento = gson.fromJson(req.body(), Monumento.class);
            monumentoDAO.insertMonumento(monumento); //Se inserta el monumento en la BD
            res.status(201); // HTTP 201 Created
            return gson.toJson(monumento); //Se devuelve como respuesata el monumento insertado en formato JSON
        });

        get("/monumentos", (req, res) -> {
            res.type("application/json");
            return gson.toJson(monumentoDAO.getAllMonumentos());
        });

        get("/monumentos/clase/:clase", (req, res) -> {
            res.type("application/json");
            return gson.toJson(monumentoDAO.getMonumentosByClase(req.params(":clase")));
        });

        get("/monumentos/:uri", (req, res) -> {
            res.type("application/json");
            Monumento monumento = monumentoDAO.findMonumentoByUri(req.params(":uri"));
            if (monumento != null) {
                return gson.toJson(monumento);
            }
            res.status(404); // HTTP 404 Not Found
            return "{}";
        });

        put("/monumentos/:uri", (req, res) -> {
            String uri = req.params(":uri");
            Monumento updatedMonumento = gson.fromJson(req.body(), Monumento.class);
            updatedMonumento.setUri(uri); // Asegúrate de que la URI no cambia en una actualización
            monumentoDAO.updateMonumento(updatedMonumento);
            return gson.toJson(updatedMonumento);
        });

        delete("/monumentos/:uri", (req, res) -> {
            monumentoDAO.deleteMonumentoByUri(req.params(":uri"));
            res.status(204); // HTTP 204 No Content
            return "{}";
        });

        post("/monumentos/connect", (req, res) -> {
            double threshold = Double.parseDouble(req.queryParams("threshold"));
            monumentoDAO.connectNearbyMonuments(threshold);
            res.status(200); // HTTP 200 OK
            return "{}";
        });

        delete("/monumentos/relationships", (req, res) -> {
            monumentoDAO.deleteAllRelationships();
            res.status(204); // HTTP 204 No Content
            return "{}";
        });

        delete("/monumentos/all", (req, res) -> {
            monumentoDAO.deleteAllMonumentos();
            res.status(204); // HTTP 204 No Content
            return "{}";
        });
    }
}
