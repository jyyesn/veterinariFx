package com.veterinaria.model;

public class Propietario {
    private String id; // Cédula o DNI
    private String nombre;
    private String telefono;
    private String email;

    public Propietario(String id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Convierte el objeto a formato de línea CSV
    public String toCSV() {
        return id + "," + escapeCSV(nombre) + "," + escapeCSV(telefono) + "," + escapeCSV(email);
    }

    // Parsea una línea CSV a un objeto Propietario
    public static Propietario fromCSV(String lineaCsv) {
        String[] campos = lineaCsv.split(",");
        if (campos.length < 4) {
            throw new IllegalArgumentException("Línea CSV inválida para Propietario: " + lineaCsv);
        }
        return new Propietario(campos[0].trim(), campos[1].trim(), campos[2].trim(), campos[3].trim());
    }

    private String escapeCSV(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";"); // Reemplazar comas internas por punto y coma para evitar romper el CSV
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Nombre: %s | Tel: %s | Email: %s", id, nombre, telefono, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Propietario otro = (Propietario) obj;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
