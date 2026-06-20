package com.veterinaria.dao;

import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Cita;

import java.io.*;

public class CitaDAOImpl implements CitaDAO {
    private final String rutaArchivo;
    private final MapaAsociativo<String, Cita> cache;

    public CitaDAOImpl(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.cache = new MapaAsociativo<>();
        inicializarArchivo();
        cargarDatos();
    }

    public CitaDAOImpl() {
        this("data/citas.csv");
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
                System.err.println("Error al crear el archivo de citas: " + e.getMessage());
            }
        }
    }

    private void cargarDatos() {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    Cita c = Cita.fromCSV(linea);
                    cache.put(c.getId(), c);
                } catch (Exception e) {
                    System.err.println("Error al cargar cita: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de citas: " + e.getMessage());
        }
    }

    private void guardarTodosEnCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            ListaEnlazada<Cita> citas = cache.obtenerValores();
            for (Cita c : citas) {
                pw.println(c.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Cita cita) {
        cache.put(cita.getId(), cita);
        guardarTodosEnCSV();
    }

    @Override
    public Cita buscarPorId(String id) {
        return cache.get(id);
    }

    @Override
    public ListaEnlazada<Cita> buscarTodos() {
        return cache.obtenerValores();
    }

    @Override
    public void actualizar(Cita cita) {
        cache.put(cita.getId(), cita);
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
    public MapaAsociativo<String, Cita> obtenerMapaCache() {
        return cache;
    }
}
