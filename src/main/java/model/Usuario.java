package model;

public class Usuario {
    protected String nombre;
    protected String email;
    protected char[] password;

    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        setEmail(email);
        this.password = password.toCharArray();
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

    public String getPassword() {
        return new String(password);
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    // Método para mostrar información básica del usuario
    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre + ", Email: " + email);
    }

    // Método abstracto que debe ser implementado por las subclases
    public void describirRol(){

    }
}
