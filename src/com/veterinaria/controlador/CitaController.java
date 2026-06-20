package com.veterinaria.controlador;

import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Cita;
import com.veterinaria.service.VeterinariaService;

public class CitaController {
    private final VeterinariaService service;

    public CitaController(VeterinariaService service) {
        this.service = service;
    }

    public void programarCita(String id, String fecha, String hora, String idMascota, String motivo) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de cita es obligatorio.");
        }
        if (fecha == null || fecha.trim().isEmpty() || !fecha.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new IllegalArgumentException("La fecha es obligatoria y debe tener formato AAAA-MM-DD.");
        }
        if (hora == null || hora.trim().isEmpty() || !hora.matches("^\\d{2}:\\d{2}$")) {
            throw new IllegalArgumentException("La hora es obligatoria y debe tener formato HH:MM.");
        }
        if (idMascota == null || idMascota.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una mascota.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de consulta es obligatorio.");
        }

        service.programarCita(id.trim(), fecha.trim(), hora.trim(), idMascota.trim(), motivo.trim());
    }

    public void actualizarCita(String id, String fecha, String hora, String idMascota, String motivo) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de cita es obligatorio para actualizar.");
        }
        if (fecha == null || fecha.trim().isEmpty() || !fecha.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD.");
        }
        if (hora == null || hora.trim().isEmpty() || !hora.matches("^\\d{2}:\\d{2}$")) {
            throw new IllegalArgumentException("La hora debe tener formato HH:MM.");
        }
        if (idMascota == null || idMascota.trim().isEmpty()) {
            throw new IllegalArgumentException("La mascota es obligatoria.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo es obligatorio.");
        }

        service.actualizarCita(id.trim(), fecha.trim(), hora.trim(), idMascota.trim(), motivo.trim());
    }

    public boolean cancelarCita(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return service.cancelarCita(id.trim());
    }

    public Cita buscarCita(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return service.obtenerCita(id.trim());
    }

    public ListaEnlazada<Cita> listarCitas() {
        return service.listarCitas();
    }

    public ListaEnlazada<Cita> obtenerCitasDeMascota(String idMascota) {
        if (idMascota == null || idMascota.trim().isEmpty()) {
            return new ListaEnlazada<>();
        }
        return service.obtenerCitasDeMascota(idMascota.trim());
    }
}
