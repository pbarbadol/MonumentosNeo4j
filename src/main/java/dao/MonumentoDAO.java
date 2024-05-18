package dao;

import model.Monumento;

import java.util.List;

public interface MonumentoDAO {
    void insertMonumento(Monumento monumento);
    void insertMonumentos(List<Monumento> monumentos);
    List<Monumento> getAllMonumentos();
    List<Monumento> getMonumentosByClase(String className);
    Monumento findMonumentoByUri(String uri);
    void updateMonumento(Monumento monumento);
    void deleteMonumentoByUri(String uri);
    void deleteAllRelationships();
    void deleteAllMonumentos();
    void connectNearbyMonuments(double distanceThreshold);
    List<Monumento> findShortestPath(String startUri, String endUri);
}
