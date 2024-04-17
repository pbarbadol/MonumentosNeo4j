package model;
public class Monumento {
    private String uri;
    private double geoLong;
    private double geoLat;
    private String clase;
    private String rdfsLabel;
    private String tieneEnlaceSIG;

    public Monumento() {    }

    public Monumento(String uri, double geoLong, double geoLat, String clase, String rdfsLabel, String tieneEnlaceSIG) {
        this.uri = uri;
        this.geoLong = geoLong;
        this.geoLat = geoLat;
        this.clase = clase;
        this.rdfsLabel = rdfsLabel;
        this.tieneEnlaceSIG = tieneEnlaceSIG;
    }


    // Getters
    public String getUri() {
        return uri;
    }

    public double getGeoLong() {
        return geoLong;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public String getClase() {
        return clase;
    }

    public String getRdfsLabel() {
        return rdfsLabel;
    }

    public String getTieneEnlaceSIG() {
        return tieneEnlaceSIG;
    }

    // Setters
    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setGeoLong(double geoLong) {
        this.geoLong = geoLong;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public void setRdfsLabel(String rdfsLabel) {
        this.rdfsLabel = rdfsLabel;
    }

    public void setTieneEnlaceSIG(String tieneEnlaceSIG) {
        this.tieneEnlaceSIG = tieneEnlaceSIG;
    }

    @Override
    public String toString() {
        return "Monumento{" +
                "uri='" + uri + '\'' +
                ", geoLong=" + geoLong +
                ", geoLat=" + geoLat +
                ", clase='" + clase + '\'' +
                ", rdfsLabel='" + rdfsLabel + '\'' +
                ", tieneEnlaceSIG='" + tieneEnlaceSIG + '\'' +
                '}';
    }
}
