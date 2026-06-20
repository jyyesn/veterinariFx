package com.veterinaria.dao;

import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Propietario;

import java.io.*;

public class PropietarioDAOImpl implements PropietarioDAO {
    private final String rutaArchivo;
    private final MapaAsociativo<String, Propietario> cache;

    public PropietarioDAOImpl(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.cache = new MapaAsociativo<>();
        inicializarArchivo();
        cargarDatos();
    }

    public PropietarioDAOImpl() {
        this("data/propietarios.csv");
    }

    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de propietarios: " + e.getMessage());
            }
        }
    }

    private void cargarDatos() {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    Propietario p = Propietario.fromCSV(linea);
                    cache.put(p.getId(), p);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al cargar propietario: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de propietarios: " + e.getMessage());
        }
    }

    private void guardarTodosEnCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            ListaEnlazada<Propietario> propietarios = cache.obtenerValores();
            for (Propietario p : propietarios) {
                pw.println(p.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar propietarios: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Propietario propietario) {
        cache.put(propietario.getId(), propietario);
        guardarTodosEnCSV();
    }

    @Override
    public Propietario buscarPorId(String id) {
        return cache.get(id);
    }

    @Override
    public ListaEnlazada<Propietario> buscarTodos() {
        return cache.obtenerValores();
    }

    @Override
    public void actualizar(Propietario propietario) {
        cache.put(propietario.getId(), propietario);
        guardarTodosEnCSV();
    }

    @Override
    public boolean eliminar(String id) {
        if (cache.contieneClave(id)) {
            cache.remove(id);
            guardarTodosEnCSV();
            return true;
        }
        return false;
    }

    @Override
    public MapaAsociativo<String, Propietario> obtenerMapaCache() {
        return cache;
    }
}
