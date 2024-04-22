package model;

public class Usuario {
    protected String nombre;
    protected String email;
    protected char[] contrasena;

    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        setEmail(email);
        this.contrasena = contrasena.toCharArray();
    }

    // Métodos getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email inválido.");
        }
    }

    public String getContrasena() {
        return new String(contrasena);
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena.toCharArray();
    }

    // Método para mostrar información básica del usuario
    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre + ", Email: " + email);
    }

    // Método abstracto que debe ser implementado por las subclases
    public void describirRol(){

    }
}
