package com.veterinaria.service;

import com.veterinaria.dao.*;
import com.veterinaria.estructuras.ArbolBinarioBusqueda;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Cita;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.Propietario;

public class VeterinariaService {
    private final PropietarioDAO propietarioDAO;
    private final MascotaDAO mascotaDAO;
    private final CitaDAO citaDAO;

    public VeterinariaService() {
        this.propietarioDAO = new PropietarioDAOImpl();
        this.mascotaDAO = new MascotaDAOImpl();
        this.citaDAO = new CitaDAOImpl();
    }

    // Constructor para inyección si se requiere testing
    public VeterinariaService(PropietarioDAO propietarioDAO, MascotaDAO mascotaDAO, CitaDAO citaDAO) {
        this.propietarioDAO = propietarioDAO;
        this.mascotaDAO = mascotaDAO;
        this.citaDAO = citaDAO;
    }

    // ==========================================
    // SERVICIOS DE PROPIETARIOS
    // ==========================================

    public void registrarPropietario(String id, String nombre, String telefono, String email) throws IllegalArgumentException {
        if (propietarioDAO.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un propietario registrado con el ID: " + id);
        }
        Propietario p = new Propietario(id, nombre, telefono, email);
        propietarioDAO.guardar(p);
    }

    public Propietario obtenerPropietario(String id) {
        return propietarioDAO.buscarPorId(id);
    }

    public ListaEnlazada<Propietario> listarPropietarios() {
        return propietarioDAO.buscarTodos();
    }

    public void actualizarPropietario(String id, String nombre, String telefono, String email) throws IllegalArgumentException {
        Propietario p = propietarioDAO.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró el propietario con el ID: " + id);
        }
        p.setNombre(nombre);
        p.setTelefono(telefono);
        p.setEmail(email);
        propietarioDAO.actualizar(p);
    }

    /**
     * Elimina un propietario y aplica eliminación en cascada para sus mascotas y citas.
     */
    public boolean eliminarPropietario(String id) {
        Propietario p = propietarioDAO.buscarPorId(id);
        if (p == null) return false;

        // 1. Buscar y eliminar todas las mascotas asociadas
        ListaEnlazada<Mascota> mascotas = listarMascotas();
        ListaEnlazada<Mascota> mascotasAEliminar = new ListaEnlazada<>();
        
        for (Mascota m : mascotas) {
            if (m.getIdPropietario().equals(id)) {
                mascotasAEliminar.insertarAlFinal(m);
            }
        }

        for (Mascota m : mascotasAEliminar) {
            eliminarMascota(m.getId()); // Elimina la mascota y sus citas
        }

        // 2. Eliminar al propietario
        return propietarioDAO.eliminar(id);
    }

    // ==========================================
    // SERVICIOS DE MASCOTAS
    // ==========================================

    public void registrarMascota(String id, String nombre, String especie, String raza, int edad, double peso, String idPropietario) throws IllegalArgumentException {
        if (mascotaDAO.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe una mascota registrada con el código: " + id);
        }
        if (propietarioDAO.buscarPorId(idPropietario) == null) {
            throw new IllegalArgumentException("No existe ningún propietario con el ID: " + idPropietario);
        }
        Mascota m = new Mascota(id, nombre, especie, raza, edad, peso, idPropietario);
        mascotaDAO.guardar(m);
    }

    public Mascota obtenerMascota(String id) {
        return mascotaDAO.buscarPorId(id);
    }

    public ListaEnlazada<Mascota> listarMascotas() {
        return mascotaDAO.buscarTodos();
    }

    public void actualizarMascota(String id, String nombre, String especie, String raza, int edad, double peso, String idPropietario) throws IllegalArgumentException {
        Mascota m = mascotaDAO.buscarPorId(id);
        if (m == null) {
            throw new IllegalArgumentException("No se encontró la mascota con código: " + id);
        }
        if (propietarioDAO.buscarPorId(idPropietario) == null) {
            throw new IllegalArgumentException("No existe ningún propietario con el ID: " + idPropietario);
        }
        Mascota nuevaMascota = new Mascota(id, nombre, especie, raza, edad, peso, idPropietario);
        mascotaDAO.actualizar(nuevaMascota);
    }

    /**
     * Elimina una mascota y todas sus citas programadas.
     */
    public boolean eliminarMascota(String id) {
        Mascota m = mascotaDAO.buscarPorId(id);
        if (m == null) return false;

        // 1. Eliminar todas las citas asociadas a esta mascota
        ListaEnlazada<Cita> citas = citaDAO.buscarTodos();
        ListaEnlazada<Cita> citasAEliminar = new ListaEnlazada<>();
        
        for (Cita c : citas) {
            if (c.getIdMascota().equals(id)) {
                citasAEliminar.insertarAlFinal(c);
            }
        }

        for (Cita c : citasAEliminar) {
            citaDAO.eliminar(c.getId());
        }

        // 2. Eliminar mascota
        return mascotaDAO.eliminar(id);
    }

    public ArbolBinarioBusqueda<Mascota> obtenerArbolMascotas() {
        return mascotaDAO.obtenerArbolMascotas();
    }

    public ListaEnlazada<Mascota> obtenerMascotasPorPropietario(String idPropietario) {
        ListaEnlazada<Mascota> mascotasFiltradas = new ListaEnlazada<>();
        ListaEnlazada<Mascota> todas = listarMascotas();
        for (Mascota m : todas) {
            if (m.getIdPropietario().equals(idPropietario)) {
                mascotasFiltradas.insertarAlFinal(m);
            }
        }
        return mascotasFiltradas;
    }

    // ==========================================
    // SERVICIOS DE CITAS
    // ==========================================

    public void programarCita(String id, String fecha, String hora, String idMascota, String motivo) throws IllegalArgumentException {
        if (citaDAO.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe una cita programada con el ID: " + id);
        }
        if (mascotaDAO.buscarPorId(idMascota) == null) {
            throw new IllegalArgumentException("No existe ninguna mascota registrada con el código: " + idMascota);
        }
        Cita c = new Cita(id, fecha, hora, idMascota, motivo);
        citaDAO.guardar(c);
    }

    public Cita obtenerCita(String id) {
        return citaDAO.buscarPorId(id);
    }

    public ListaEnlazada<Cita> listarCitas() {
        return citaDAO.buscarTodos();
    }

    public void actualizarCita(String id, String fecha, String hora, String idMascota, String motivo) throws IllegalArgumentException {
        Cita c = citaDAO.buscarPorId(id);
        if (c == null) {
            throw new IllegalArgumentException("No se encontró la cita con ID: " + id);
        }
        if (mascotaDAO.buscarPorId(idMascota) == null) {
            throw new IllegalArgumentException("No existe ninguna mascota registrada con el código: " + idMascota);
        }
        c.setFecha(fecha);
        c.setHora(hora);
        c.setIdMascota(idMascota);
        c.setMotivo(motivo);
        citaDAO.actualizar(c);
    }

    public boolean cancelarCita(String id) {
        return citaDAO.eliminar(id);
    }

    public ListaEnlazada<Cita> obtenerCitasDeMascota(String idMascota) {
        ListaEnlazada<Cita> filtradas = new ListaEnlazada<>();
        ListaEnlazada<Cita> todas = citaDAO.buscarTodos();
        for (Cita c : todas) {
            if (c.getIdMascota().equals(idMascota)) {
                filtradas.insertarAlFinal(c);
            }
        }
        return filtradas;
    }

    public MapaAsociativo<String, Propietario> obtenerMapaPropietarios() {
        return propietarioDAO.obtenerMapaCache();
    }

    public MapaAsociativo<String, Mascota> obtenerMapaMascotas() {
        return ((MascotaDAO) mascotaDAO).obtenerMapaCache();
    }

    public MapaAsociativo<String, Cita> obtenerMapaCitas() {
        return ((CitaDAO) citaDAO).obtenerMapaCache();
    }
}
