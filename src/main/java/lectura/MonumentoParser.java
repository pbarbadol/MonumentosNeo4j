package lectura;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Monumento;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MonumentoParser {

    static private String normalizeClassName(String className) {
        return className.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    static public List<Monumento> parseMonumentos(String filePath) {
        List<Monumento> monumentos = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader reader = new FileReader(filePath)) {
            // Parsea el JSON desde el archivo y obtén el array 'results'
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonObject("results").getAsJsonArray("bindings");

            for (JsonElement resultElement : results) {
                JsonObject result = resultElement.getAsJsonObject();

                Monumento monumento = new Monumento();
                monumento.setUri(result.getAsJsonObject("uri").get("value").getAsString());
                monumento.setGeoLong(result.getAsJsonObject("geo_long").get("value").getAsDouble());
                monumento.setGeoLat(result.getAsJsonObject("geo_lat").get("value").getAsDouble());
                monumento.setClase(normalizeClassName(result.getAsJsonObject("clase").get("value").getAsString()));
                monumento.setRdfsLabel(result.getAsJsonObject("rdfs_label").get("value").getAsString().trim());
                monumento.setTieneEnlaceSIG(result.getAsJsonObject("tieneEnlaceSIG").get("value").getAsString().trim());

                monumentos.add(monumento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return monumentos;
    }
}
