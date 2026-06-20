package com.veterinaria.estructuras;

public class MapaAsociativo<K, V> {
    public static class Entrada<K, V> {
        private K clave;
        private V valor;

        public Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }

        public K getClave() {
            return clave;
        }

        public V getValor() {
            return valor;
        }

        public void setValor(V valor) {
            this.valor = valor;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Entrada<?, ?> otra = (Entrada<?, ?>) obj;
            return clave.equals(otra.clave);
        }

        @Override
        public int hashCode() {
            return clave.hashCode();
        }
    }

    private ListaEnlazada<Entrada<K, V>>[] buckets;
    private int capacidad;
    private int tamaño;
    private static final double FACTOR_CARGA_LIMITE = 0.75;

    @SuppressWarnings("unchecked")
    public MapaAsociativo(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.buckets = new ListaEnlazada[capacidad];
        for (int i = 0; i < capacidad; i++) {
            buckets[i] = new ListaEnlazada<>();
        }
        this.tamaño = 0;
    }

    public MapaAsociativo() {
        this(16);
    }

    private int obtenerIndiceBucket(K clave) {
        if (clave == null) return 0;
        int hashCode = clave.hashCode();
        int indice = hashCode % capacidad;
        if (indice < 0) {
            indice = indice + capacidad;
        }
        return indice;
    }

    public void put(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser nula");
        }

        if ((double) tamaño / capacidad >= FACTOR_CARGA_LIMITE) {
            redimensionar();
        }

        int indice = obtenerIndiceBucket(clave);
        ListaEnlazada<Entrada<K, V>> bucket = buckets[indice];

        for (Entrada<K, V> entrada : bucket) {
            if (entrada.clave.equals(clave)) {
                entrada.valor = valor;
                return;
            }
        }

        bucket.insertarAlFinal(new Entrada<>(clave, valor));
        tamaño++;
    }

    public V get(K clave) {
        if (clave == null) return null;
        int indice = obtenerIndiceBucket(clave);
        ListaEnlazada<Entrada<K, V>> bucket = buckets[indice];

        for (Entrada<K, V> entrada : bucket) {
            if (entrada.clave.equals(clave)) {
                return entrada.valor;
            }
        }
        return null;
    }

    public boolean contieneClave(K clave) {
        return get(clave) != null;
    }

    public boolean remove(K clave) {
        if (clave == null) return false;
        int indice = obtenerIndiceBucket(clave);
        ListaEnlazada<Entrada<K, V>> bucket = buckets[indice];

        for (Entrada<K, V> entrada : bucket) {
            if (entrada.clave.equals(clave)) {
                bucket.eliminar(entrada);
                tamaño--;
                return true;
            }
        }
        return false;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean estaVacio() {
        return tamaño == 0;
    }

    public ListaEnlazada<K> obtenerClaves() {
        ListaEnlazada<K> claves = new ListaEnlazada<>();
        for (int i = 0; i < capacidad; i++) {
            for (Entrada<K, V> entrada : buckets[i]) {
                claves.insertarAlFinal(entrada.clave);
            }
        }
        return claves;
    }

    public ListaEnlazada<V> obtenerValores() {
        ListaEnlazada<V> valores = new ListaEnlazada<>();
        for (int i = 0; i < capacidad; i++) {
            for (Entrada<K, V> entrada : buckets[i]) {
                valores.insertarAlFinal(entrada.valor);
            }
        }
        return valores;
    }

    public ListaEnlazada<Entrada<K, V>>[] getBuckets() {
        return buckets;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @SuppressWarnings("unchecked")
    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        ListaEnlazada<Entrada<K, V>>[] viejosBuckets = buckets;

        this.capacidad = nuevaCapacidad;
        this.buckets = new ListaEnlazada[nuevaCapacidad];
        for (int i = 0; i < nuevaCapacidad; i++) {
            buckets[i] = new ListaEnlazada<>();
        }
        this.tamaño = 0;

        for (ListaEnlazada<Entrada<K, V>> bucket : viejosBuckets) {
            for (Entrada<K, V> entrada : bucket) {
                put(entrada.clave, entrada.valor);
            }
        }
    }
}
