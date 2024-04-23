package dao;

import model.Monumento;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Values;

import java.util.ArrayList;
import java.util.List;

public class MonumentoDAOimpl implements MonumentoDAO {
    private final Driver driver;

    public MonumentoDAOimpl(Driver driver) {
        this.driver = driver;
    }

    private String normalizeClassName(String className) {
        return className.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    @Override
    public void insertMonumento(Monumento monumento) {
        try (Session session = driver.session()) {
            String normalizedClass = normalizeClassName(monumento.getClase());
            String query = String.format("CREATE (m:%s:MONUMENT {uri: $uri, location: point({longitude: $geoLong, latitude: $geoLat}), clase: $clase, rdfsLabel: $rdfsLabel, tieneEnlaceSIG: $tieneEnlaceSIG})", normalizedClass);
            session.run(query, Values.parameters(
                    "uri", monumento.getUri(),
                    "geoLong", monumento.getGeoLong(),
                    "geoLat", monumento.getGeoLat(),
                    "clase", monumento.getClase(),
                    "rdfsLabel", monumento.getRdfsLabel(),
                    "tieneEnlaceSIG", monumento.getTieneEnlaceSIG()
            ));
        }
    }

    @Override
    public void insertMonumentos(List<Monumento> monumentos) {
        try (Session session = driver.session()) {
            for (Monumento monumento : monumentos) {
                String normalizedClass = normalizeClassName(monumento.getClase());
                String query = String.format("CREATE (m:%s:MONUMENT {uri: $uri, location: point({longitude: $geoLong, latitude: $geoLat}), clase: $clase, rdfsLabel: $rdfsLabel, tieneEnlaceSIG: $tieneEnlaceSIG})", normalizedClass);
                session.run(query, Values.parameters(
                        "uri", monumento.getUri(),
                        "geoLong", monumento.getGeoLong(),
                        "geoLat", monumento.getGeoLat(),
                        "clase", monumento.getClase(),
                        "rdfsLabel", monumento.getRdfsLabel(),
                        "tieneEnlaceSIG", monumento.getTieneEnlaceSIG()
                ));
            }
        }
    }

    @Override
    public List<Monumento> getAllMonumentos() {
        List<Monumento> monumentos = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = "MATCH (m:MONUMENT) RETURN m.uri as uri, m.location.longitude as geoLong, m.location.latitude as geoLat, m.clase as clase, m.rdfsLabel as rdfsLabel, m.tieneEnlaceSIG as tieneEnlaceSIG";
            Result result = session.run(query);
            while (result.hasNext()) {
                var record = result.next();
                Monumento monumento = new Monumento(
                        record.get("uri").asString(),
                        record.get("geoLong").asDouble(),
                        record.get("geoLat").asDouble(),
                        record.get("clase").asString(),
                        record.get("rdfsLabel").asString(),
                        record.get("tieneEnlaceSIG").asString()
                );
                monumentos.add(monumento);
            }
        }
        return monumentos;
    }

    @Override
    public List<Monumento> getMonumentosByClase(String className) {
        List<Monumento> monumentos = new ArrayList<>();
        String clase = normalizeClassName(className);
        try (Session session = driver.session()) {
            String query = String.format("MATCH (m:%s:MONUMENT) RETURN m.uri as uri, m.location.longitude as geoLong, m.location.latitude as geoLat, m.clase as clase, m.rdfsLabel as rdfsLabel, m.tieneEnlaceSIG as tieneEnlaceSIG", clase);
            Result result = session.run(query);
            while (result.hasNext()) {
                var record = result.next();
                Monumento monumento = new Monumento(
                        record.get("uri").asString(),
                        record.get("geoLong").asDouble(),
                        record.get("geoLat").asDouble(),
                        record.get("clase").asString(),
                        record.get("rdfsLabel").asString(),
                        record.get("tieneEnlaceSIG").asString()
                );
                monumentos.add(monumento);
            }
        }
        return monumentos;
    }

    @Override
    public Monumento findMonumentoByUri(String uri) {
        try (Session session = driver.session()) {
            String query = "MATCH (m:MONUMENT {uri: $uri}) RETURN m.uri as uri, m.location.longitude as geoLong, m.location.latitude as geoLat, m.clase as clase, m.rdfsLabel as rdfsLabel, m.tieneEnlaceSIG as tieneEnlaceSIG";
            Result result = session.run(query, Values.parameters("uri", uri));
            if (result.hasNext()) {
                var record = result.next();
                return new Monumento(
                        record.get("uri").asString(),
                        record.get("geoLong").asDouble(),
                        record.get("geoLat").asDouble(),
                        record.get("clase").asString(),
                        record.get("rdfsLabel").asString(),
                        record.get("tieneEnlaceSIG").asString()
                );
            }
        }
        return null;
    }

    @Override
    public void updateMonumento(Monumento monumento) {
        try (Session session = driver.session()) {
            String normalizedClass = normalizeClassName(monumento.getClase());
            String query = String.format("MATCH (m:%s:MONUMENT {uri: $uri}) " +
                    "SET m.location = point({longitude: $geoLong, latitude: $geoLat}), " +
                    "m.clase = $clase, m.rdfsLabel = $rdfsLabel, m.tieneEnlaceSIG = $tieneEnlaceSIG", normalizedClass);
            session.run(query, Values.parameters(
                    "uri", monumento.getUri(),
                    "geoLong", monumento.getGeoLong(),
                    "geoLat", monumento.getGeoLat(),
                    "clase", monumento.getClase(),
                    "rdfsLabel", monumento.getRdfsLabel(),
                    "tieneEnlaceSIG", monumento.getTieneEnlaceSIG()
            ));
        }
    }

    @Override
    public void deleteMonumentoByUri(String uri) {
        try (Session session = driver.session()) {
            String query = "MATCH (m:MONUMENT {uri: $uri}) DELETE m";
            session.run(query, Values.parameters("uri", uri));
        }
    }

    @Override
    public void deleteAllRelationships() {
        try (Session session = driver.session()) {
            String query = "MATCH ()-[r]-() DELETE r";
            session.run(query);
        }
    }

    @Override
    public void deleteAllMonumentos() {
        try (Session session = driver.session()) {
            String query = "MATCH (m:MONUMENT) DELETE m";
            session.run(query);
        }
    }

    @Override
    public void connectNearbyMonuments(double distanceThreshold) {
        try (Session session = driver.session()) {
            String query = "MATCH (m1:MONUMENT), (m2:MONUMENT) " +
                    "WHERE m1 <> m2 AND point.distance(m1.location, m2.location) < $distance " +
                    "MERGE (m1)-[r:CERCANO_A]->(m2) " +
                    "SET r.distance = point.distance(m1.location, m2.location)";
            session.run(query, Values.parameters("distance", distanceThreshold));
        }
    }
}
