package com.veterinaria.dao;

import com.veterinaria.estructuras.ArbolBinarioBusqueda;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Mascota;

import java.io.*;

public class MascotaDAOImpl implements MascotaDAO {
    private final String rutaArchivo;
    private final ArbolBinarioBusqueda<Mascota> arbolCache;
    private final MapaAsociativo<String, Mascota> mapaCache; // Para búsquedas O(1) por ID

    public MascotaDAOImpl(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.arbolCache = new ArbolBinarioBusqueda<>();
        this.mapaCache = new MapaAsociativo<>();
        inicializarArchivo();
        cargarDatos();
    }

    public MascotaDAOImpl() {
        this("data/mascotas.csv");
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
                System.err.println("Error al crear el archivo de mascotas: " + e.getMessage());
            }
        }
    }

    private void cargarDatos() {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    Mascota m = Mascota.fromCSV(linea);
                    arbolCache.insertar(m);
                    mapaCache.put(m.getId(), m);
                } catch (Exception e) {
                    System.err.println("Error al cargar mascota: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de mascotas: " + e.getMessage());
        }
    }

    private void guardarTodosEnCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            // El recorrido in-order nos devuelve la lista ordenada alfabéticamente
            ListaEnlazada<Mascota> listaMascotas = arbolCache.inOrder();
            for (Mascota m : listaMascotas) {
                pw.println(m.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar mascotas: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Mascota mascota) {
        arbolCache.insertar(mascota);
        mapaCache.put(mascota.getId(), mascota);
        guardarTodosEnCSV();
    }

    @Override
    public Mascota buscarPorId(String id) {
        return mapaCache.get(id);
    }

    @Override
    public ListaEnlazada<Mascota> buscarTodos() {
        return arbolCache.inOrder(); // Retorna todas las mascotas ordenadas alfabéticamente
    }

    @Override
    public void actualizar(Mascota mascota) {
        Mascota mascotaVieja = mapaCache.get(mascota.getId());
        if (mascotaVieja != null) {
            // En el árbol, eliminar la vieja y registrar la nueva (por si cambió el nombre, lo cual altera su orden)
            arbolCache.eliminar(mascotaVieja);
        }
        arbolCache.insertar(mascota);
        mapaCache.put(mascota.getId(), mascota);
        guardarTodosEnCSV();
    }

    @Override
    public boolean eliminar(String id) {
        Mascota mascota = mapaCache.get(id);
        if (mascota != null) {
            arbolCache.eliminar(mascota);
            mapaCache.remove(id);
            guardarTodosEnCSV();
            return true;
        }
        return false;
    }

    @Override
    public ArbolBinarioBusqueda<Mascota> obtenerArbolMascotas() {
        return arbolCache;
    }

    @Override
    public MapaAsociativo<String, Mascota> obtenerMapaCache() {
        return mapaCache;
    }
}
