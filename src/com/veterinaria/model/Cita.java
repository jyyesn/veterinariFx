package com.veterinaria.model;

public class Cita {
    private String id; // ID único de la cita
    private String fecha; // Formato AAAA-MM-DD
    private String hora; // Formato HH:MM
    private String idMascota; // Relación con Mascota
    private String motivo;

    public Cita(String id, String fecha, String hora, String idMascota, String motivo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.idMascota = idMascota;
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    // Convierte el objeto a formato de línea CSV
    public String toCSV() {
        return id + "," + fecha + "," + hora + "," + idMascota + "," + escapeCSV(motivo);
    }

    // Parsea una línea CSV a un objeto Cita
    public static Cita fromCSV(String lineaCsv) {
        String[] campos = lineaCsv.split(",");
        if (campos.length < 5) {
            throw new IllegalArgumentException("Línea CSV inválida para Cita: " + lineaCsv);
        }
        return new Cita(
            campos[0].trim(),
            campos[1].trim(),
            campos[2].trim(),
            campos[3].trim(),
            campos[4].trim()
        );
    }

    private String escapeCSV(String valor) {
        if (valor == null) return "";
        return valor.replace(",", ";");
    }

    @Override
    public String toString() {
        return String.format("Cita ID: %s | Fecha: %s | Hora: %s | Mascota ID: %s | Motivo: %s",
                id, fecha, hora, idMascota, motivo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita otra = (Cita) obj;
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
