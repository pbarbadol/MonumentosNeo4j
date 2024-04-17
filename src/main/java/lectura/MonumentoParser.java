package lectura;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Monumento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de parsear los monumentos de un archivo JSON
 */
public class MonumentoParser {

    static private String normalizeClassName(String className) {
        return className.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    static public List<Monumento> parseMonumentos(String filePath) {
        List<Monumento> monumentos = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode results = root.path("results").path("bindings");

            for (JsonNode result : results) {
                Monumento monumento = new Monumento();

                monumento.setUri(result.path("uri").path("value").asText());
                monumento.setGeoLong(result.path("geo_long").path("value").asDouble());
                monumento.setGeoLat(result.path("geo_lat").path("value").asDouble());
                monumento.setClase(normalizeClassName(result.path("clase").path("value").asText()));
                monumento.setRdfsLabel(result.path("rdfs_label").path("value").asText().trim());
                monumento.setTieneEnlaceSIG(result.path("tieneEnlaceSIG").path("value").asText().trim());

                monumentos.add(monumento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return monumentos;
    }
}
