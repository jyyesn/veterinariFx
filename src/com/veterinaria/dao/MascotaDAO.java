package com.veterinaria.dao;

import com.veterinaria.estructuras.ArbolBinarioBusqueda;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Mascota;

public interface MascotaDAO extends DAO<Mascota, String> {
    ArbolBinarioBusqueda<Mascota> obtenerArbolMascotas();
    MapaAsociativo<String, Mascota> obtenerMapaCache();
}
