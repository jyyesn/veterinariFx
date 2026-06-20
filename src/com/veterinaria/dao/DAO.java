package com.veterinaria.dao;

import com.veterinaria.estructuras.ListaEnlazada;

public interface DAO<T, ID> {
    void guardar(T t);
    T buscarPorId(ID id);
    ListaEnlazada<T> buscarTodos();
    void actualizar(T t);
    boolean eliminar(ID id);
}
