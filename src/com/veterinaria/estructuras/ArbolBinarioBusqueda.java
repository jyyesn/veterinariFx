package com.veterinaria.estructuras;

public class ArbolBinarioBusqueda<T extends Comparable<T>> {
    private static class NodoArbol<E> {
        E dato;
        NodoArbol<E> izquierdo;
        NodoArbol<E> derecho;

        NodoArbol(E dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private NodoArbol<T> raiz;
    private int tamaño;

    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.tamaño = 0;
    }

    public void insertar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser nulo");
        }
        raiz = insertarRec(raiz, dato);
    }

    private NodoArbol<T> insertarRec(NodoArbol<T> actual, T dato) {
        if (actual == null) {
            tamaño++;
            return new NodoArbol<>(dato);
        }

        int comparacion = dato.compareTo(actual.dato);
        if (comparacion < 0) {
            actual.izquierdo = insertarRec(actual.izquierdo, dato);
        } else if (comparacion > 0) {
            actual.derecho = insertarRec(actual.derecho, dato);
        }
        return actual;
    }

    public boolean buscar(T dato) {
        if (dato == null) return false;
        return buscarRec(raiz, dato) != null;
    }

    public T obtener(T dato) {
        if (dato == null) return null;
        NodoArbol<T> nodo = buscarRec(raiz, dato);
        return nodo != null ? nodo.dato : null;
    }

    private NodoArbol<T> buscarRec(NodoArbol<T> actual, T dato) {
        if (actual == null) return null;

        int comparacion = dato.compareTo(actual.dato);
        if (comparacion == 0) {
            return actual;
        } else if (comparacion < 0) {
            return buscarRec(actual.izquierdo, dato);
        } else {
            return buscarRec(actual.derecho, dato);
        }
    }

    public void eliminar(T dato) {
        if (dato == null) return;
        raiz = eliminarRec(raiz, dato);
    }

    private NodoArbol<T> eliminarRec(NodoArbol<T> actual, T dato) {
        if (actual == null) return null;

        int comparacion = dato.compareTo(actual.dato);
        if (comparacion < 0) {
            actual.izquierdo = eliminarRec(actual.izquierdo, dato);
        } else if (comparacion > 0) {
            actual.derecho = eliminarRec(actual.derecho, dato);
        } else {
            tamaño--;
            if (actual.izquierdo == null) {
                return actual.derecho;
            } else if (actual.derecho == null) {
                return actual.izquierdo;
            }

            actual.dato = obtenerMinimo(actual.derecho);
            actual.derecho = eliminarRec(actual.derecho, actual.dato);
            tamaño++;
        }
        return actual;
    }

    private T obtenerMinimo(NodoArbol<T> actual) {
        T min = actual.dato;
        while (actual.izquierdo != null) {
            min = actual.izquierdo.dato;
            actual = actual.izquierdo;
        }
        return min;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public ListaEnlazada<T> inOrder() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        inOrderRec(raiz, lista);
        return lista;
    }

    private void inOrderRec(NodoArbol<T> actual, ListaEnlazada<T> lista) {
        if (actual != null) {
            inOrderRec(actual.izquierdo, lista);
            lista.insertarAlFinal(actual.dato);
            inOrderRec(actual.derecho, lista);
        }
    }

    public ListaEnlazada<T> preOrder() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        preOrderRec(raiz, lista);
        return lista;
    }

    private void preOrderRec(NodoArbol<T> actual, ListaEnlazada<T> lista) {
        if (actual != null) {
            lista.insertarAlFinal(actual.dato);
            preOrderRec(actual.izquierdo, lista);
            preOrderRec(actual.derecho, lista);
        }
    }

    public ListaEnlazada<T> postOrder() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        postOrderRec(raiz, lista);
        return lista;
    }

    private void postOrderRec(NodoArbol<T> actual, ListaEnlazada<T> lista) {
        if (actual != null) {
            postOrderRec(actual.izquierdo, lista);
            postOrderRec(actual.derecho, lista);
            lista.insertarAlFinal(actual.dato);
        }
    }

    public String representarJerarquia() {
        StringBuilder sb = new StringBuilder();
        representarJerarquiaRec(raiz, "", true, sb);
        return sb.toString();
    }

    private void representarJerarquiaRec(NodoArbol<T> actual, String prefijo, boolean esUltimo, StringBuilder sb) {
        if (actual != null) {
            sb.append(prefijo).append(esUltimo ? "└── " : "├── ").append(actual.dato.toString()).append("\n");
            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");
            
            int hijos = 0;
            if (actual.izquierdo != null) hijos++;
            if (actual.derecho != null) hijos++;

            if (actual.izquierdo != null) {
                representarJerarquiaRec(actual.izquierdo, nuevoPrefijo, hijos == 1, sb);
            }
            if (actual.derecho != null) {
                representarJerarquiaRec(actual.derecho, nuevoPrefijo, true, sb);
            }
        }
    }
}
