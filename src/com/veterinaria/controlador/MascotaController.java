package com.veterinaria.controlador;

import com.veterinaria.estructuras.ArbolBinarioBusqueda;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Mascota;
import com.veterinaria.service.VeterinariaService;

public class MascotaController {
    private final VeterinariaService service;

    public MascotaController(VeterinariaService service) {
        this.service = service;
    }

    public void registrarMascota(String id, String nombre, String especie, String raza, String edadStr, String pesoStr, String idPropietario) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de mascota es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (idPropietario == null || idPropietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe asociar un propietario a la mascota.");
        }

        int edad;
        double peso;
        try {
            edad = Integer.parseInt(edadStr.trim());
            if (edad < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad debe ser un número entero positivo.");
        }

        try {
            peso = Double.parseDouble(pesoStr.trim());
            if (peso <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El peso debe ser un número decimal mayor a 0.");
        }

        service.registrarMascota(id.trim(), nombre.trim(), especie.trim(), raza.trim(), edad, peso, idPropietario.trim());
    }

    public void actualizarMascota(String id, String nombre, String especie, String raza, String edadStr, String pesoStr, String idPropietario) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio para actualizar.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (idPropietario == null || idPropietario.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe asociar un propietario.");
        }

        int edad;
        double peso;
        try {
            edad = Integer.parseInt(edadStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La edad debe ser un número entero.");
        }

        try {
            peso = Double.parseDouble(pesoStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El peso debe ser un número decimal.");
        }

        service.actualizarMascota(id.trim(), nombre.trim(), especie.trim(), raza.trim(), edad, peso, idPropietario.trim());
    }

    public boolean eliminarMascota(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return service.eliminarMascota(id.trim());
    }

    public Mascota buscarMascota(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return service.obtenerMascota(id.trim());
    }

    public ListaEnlazada<Mascota> listarMascotas() {
        return service.listarMascotas();
    }

    public ArbolBinarioBusqueda<Mascota> obtenerArbolMascotas() {
        return service.obtenerArbolMascotas();
    }

    public ListaEnlazada<Mascota> obtenerMascotasPorPropietario(String idPropietario) {
        if (idPropietario == null || idPropietario.trim().isEmpty()) {
            return new ListaEnlazada<>();
        }
        return service.obtenerMascotasPorPropietario(idPropietario.trim());
    }
}
