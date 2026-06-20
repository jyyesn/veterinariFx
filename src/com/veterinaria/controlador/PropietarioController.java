package com.veterinaria.controlador;

import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Propietario;
import com.veterinaria.service.VeterinariaService;

public class PropietarioController {
    private final VeterinariaService service;

    public PropietarioController(VeterinariaService service) {
        this.service = service;
    }

    public void registrarPropietario(String id, String nombre, String telefono, String email) throws IllegalArgumentException {
        // Validaciones básicas de entrada antes de enviar al servicio
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID/Cédula es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        service.registrarPropietario(id.trim(), nombre.trim(), telefono.trim(), email.trim());
    }

    public void actualizarPropietario(String id, String nombre, String telefono, String email) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID/Cédula es obligatorio para actualizar.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        service.actualizarPropietario(id.trim(), nombre.trim(), telefono.trim(), email.trim());
    }

    public boolean eliminarPropietario(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return service.eliminarPropietario(id.trim());
    }

    public Propietario buscarPropietario(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return service.obtenerPropietario(id.trim());
    }

    public ListaEnlazada<Propietario> listarPropietarios() {
        return service.listarPropietarios();
    }
}
