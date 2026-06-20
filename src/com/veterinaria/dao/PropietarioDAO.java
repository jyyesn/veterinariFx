package com.veterinaria.dao;

import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Propietario;

public interface PropietarioDAO extends DAO<Propietario, String> {
    MapaAsociativo<String, Propietario> obtenerMapaCache();
}
