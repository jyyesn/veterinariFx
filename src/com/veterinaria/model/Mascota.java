package com.veterinaria.model;

public class Mascota implements Comparable<Mascota> {
    private String id; // Código único
    private String nombre;
    private String especie; // Perro, Gato, Ave, etc.
    private String raza;
    private int edad;
    private double peso;
    private String idPropietario; // Cédula del dueño

    public Mascota(String id, String nombre, String especie, String raza, int edad, double peso, String idPropietario) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.peso = peso;
        this.idPropietario = idPropietario;
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

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(String idPropietario) {
        this.idPropietario = idPropietario;
    }

    // Convierte el objeto a formato de línea CSV
    public String toCSV() {
        return id + "," + escapeCSV(nombre) + "," + escapeCSV(especie) + "," + escapeCSV(raza) + "," + edad + "," + peso + "," + idPropietario;
    }

    // Parsea una línea CSV a un objeto Mascota
    public static Mascota fromCSV(String lineaCsv) {
        String[] campos = lineaCsv.split(",");
        if (campos.length < 7) {
            throw new IllegalArgumentException("Línea CSV inválida para Mascota: " + lineaCsv);
        }
        return new Mascota(
            campos[0].trim(),
            campos[1].trim(),
            campos[2].trim(),
            campos[3].trim(),
            Integer.parseInt(campos[4].trim()),
            Double.parseDouble(campos[5].trim()),
            campos[6].trim()
        );
    }

    private String escapeCSV(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";");
    }

    @Override
    public int compareTo(Mascota otra) {
        // Ordenamiento por nombre (case-insensitive)
        int compNombre = this.nombre.compareToIgnoreCase(otra.nombre);
        if (compNombre != 0) {
            return compNombre;
        }
        // Si tienen el mismo nombre, desempatar por ID único para evitar perder duplicados de nombre en el árbol
        return this.id.compareTo(otra.id);
    }

    @Override
    public String toString() {
        return String.format("Código: %s | Nombre: %s | Especie: %s | Raza: %s | Edad: %d años | Peso: %.2f kg | Dueño ID: %s",
                id, nombre, especie, raza, edad, peso, idPropietario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mascota otra = (Mascota) obj;
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
