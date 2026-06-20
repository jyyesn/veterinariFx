package com.veterinaria.dao;

import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Cita;

public interface CitaDAO extends DAO<Cita, String> {
    MapaAsociativo<String, Cita> obtenerMapaCache();
}
