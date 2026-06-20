package com.veterinaria.vista;

import com.veterinaria.controlador.MascotaController;
import com.veterinaria.controlador.PropietarioController;
import com.veterinaria.controlador.CitaController;
import com.veterinaria.estructuras.ArbolBinarioBusqueda;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.estructuras.MapaAsociativo;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.Propietario;
import com.veterinaria.model.Cita;
import com.veterinaria.service.VeterinariaService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VistaEstructuras extends BorderPane {
    private final VeterinariaService service;
    private final MascotaController mascotaController;

    private final TextArea txtArbol;
    private final TextArea txtHash;
    private final ComboBox<String> cbTablas;
    private final Label lblStatsHash;

    public VistaEstructuras(VeterinariaService service, MascotaController mascotaController) {
        this.service = service;
        this.mascotaController = mascotaController;
        this.setPadding(new Insets(20));

        // Título de la sección
        Label lblTitulo = new Label("DIAGNÓSTICO Y VISUALIZACIÓN DE ESTRUCTURAS DE DATOS");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.web("#2c3e50"));
        
        HBox topBar = new HBox(lblTitulo);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        this.setTop(topBar);

        // --- TabPane Interno para separar Árbol y Tabla Hash ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // 1. Pestaña del Árbol Binario de Búsqueda
        Tab tabArbol = new Tab("Árbol Binario (Mascotas)");
        VBox layoutArbol = new VBox(15);
        layoutArbol.setPadding(new Insets(15));

        Label lblArbol = new Label("Estructura No Lineal: Árbol Binario de Búsqueda (Ordenado Alfabéticamente)");
        lblArbol.setFont(Font.font("System", FontWeight.BOLD, 14));

        txtArbol = new TextArea();
        txtArbol.setEditable(false);
        txtArbol.setFont(Font.font("Monospaced", 13));
        VBox.setVgrow(txtArbol, Priority.ALWAYS);

        Button btnRefrescarArbol = new Button("Actualizar Árbol");
        btnRefrescarArbol.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnInOrder = new Button("Recorrido In-Order");
        btnInOrder.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        Button btnPreOrder = new Button("Recorrido Pre-Order");
        btnPreOrder.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");
        Button btnPostOrder = new Button("Recorrido Post-Order");
        btnPostOrder.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");

        HBox btnsArbol = new HBox(10, btnRefrescarArbol, btnInOrder, btnPreOrder, btnPostOrder);
        layoutArbol.getChildren().addAll(lblArbol, txtArbol, btnsArbol);
        tabArbol.setContent(layoutArbol);

        // 2. Pestaña del Mapa Asociativo (Tabla Hash)
        Tab tabHash = new Tab("Mapa Asociativo (Tabla Hash)");
        VBox layoutHash = new VBox(15);
        layoutHash.setPadding(new Insets(15));

        Label lblHash = new Label("Estructura Asociativa: Tabla Hash con Encadenamiento (Resolución de Colisiones)");
        lblHash.setFont(Font.font("System", FontWeight.BOLD, 14));

        HBox controlHash = new HBox(15);
        controlHash.setAlignment(Pos.CENTER_LEFT);
        
        Label lblSeleccion = new Label("Seleccione Mapa a Inspeccionar:");
        cbTablas = new ComboBox<>();
        cbTablas.getItems().addAll("Propietarios", "Mascotas", "Citas");
        cbTablas.setValue("Propietarios");
        cbTablas.setPrefWidth(150);

        Button btnRefrescarHash = new Button("Analizar Cubetas (Buckets)");
        btnRefrescarHash.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");

        controlHash.getChildren().addAll(lblSeleccion, cbTablas, btnRefrescarHash);

        txtHash = new TextArea();
        txtHash.setEditable(false);
        txtHash.setFont(Font.font("Monospaced", 13));
        VBox.setVgrow(txtHash, Priority.ALWAYS);

        lblStatsHash = new Label("Capacidad: - | Elementos: - | Factor de Carga: -");
        lblStatsHash.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblStatsHash.setTextFill(Color.web("#e67e22"));

        layoutHash.getChildren().addAll(lblHash, controlHash, txtHash, lblStatsHash);
        tabHash.setContent(layoutHash);

        tabPane.getTabs().addAll(tabArbol, tabHash);
        this.setCenter(tabPane);

        // --- MANEJO DE EVENTOS ---

        // Eventos del Árbol
        btnRefrescarArbol.setOnAction(e -> refrescarEstructuraArbol());
        
        btnInOrder.setOnAction(e -> {
            ArbolBinarioBusqueda<Mascota> arbol = mascotaController.obtenerArbolMascotas();
            if (arbol.estaVacio()) {
                txtArbol.setText("El árbol de mascotas está vacío.");
                return;
            }
            StringBuilder sb = new StringBuilder("=== RECORRIDO IN-ORDER (IZQUIERDA -> RAÍZ -> DERECHA) ===\n\n");
            ListaEnlazada<Mascota> recorrido = arbol.inOrder();
            int index = 1;
            for (Mascota m : recorrido) {
                sb.append(String.format(" %02d. %s (Código: %s)\n", index++, m.getNombre(), m.getId()));
            }
            txtArbol.setText(sb.toString());
        });

        btnPreOrder.setOnAction(e -> {
            ArbolBinarioBusqueda<Mascota> arbol = mascotaController.obtenerArbolMascotas();
            if (arbol.estaVacio()) {
                txtArbol.setText("El árbol de mascotas está vacío.");
                return;
            }
            StringBuilder sb = new StringBuilder("=== RECORRIDO PRE-ORDER (RAÍZ -> IZQUIERDA -> DERECHA) ===\n\n");
            ListaEnlazada<Mascota> recorrido = arbol.preOrder();
            int index = 1;
            for (Mascota m : recorrido) {
                sb.append(String.format(" %02d. %s (Código: %s)\n", index++, m.getNombre(), m.getId()));
            }
            txtArbol.setText(sb.toString());
        });

        btnPostOrder.setOnAction(e -> {
            ArbolBinarioBusqueda<Mascota> arbol = mascotaController.obtenerArbolMascotas();
            if (arbol.estaVacio()) {
                txtArbol.setText("El árbol de mascotas está vacío.");
                return;
            }
            StringBuilder sb = new StringBuilder("=== RECORRIDO POST-ORDER (IZQUIERDA -> DERECHA -> RAÍZ) ===\n\n");
            ListaEnlazada<Mascota> recorrido = arbol.postOrder();
            int index = 1;
            for (Mascota m : recorrido) {
                sb.append(String.format(" %02d. %s (Código: %s)\n", index++, m.getNombre(), m.getId()));
            }
            txtArbol.setText(sb.toString());
        });

        // Eventos de la Tabla Hash
        btnRefrescarHash.setOnAction(e -> refrescarEstructuraHash());

        // Cargar datos por defecto al iniciar
        refrescarEstructuraArbol();
        refrescarEstructuraHash();
    }

    public void refrescarEstructuraArbol() {
        ArbolBinarioBusqueda<Mascota> arbol = mascotaController.obtenerArbolMascotas();
        if (arbol.estaVacio()) {
            txtArbol.setText("El árbol de mascotas está vacío. Registre mascotas en la pestaña de Mascotas para verlo crecer.");
        } else {
            String jerarquia = arbol.representarJerarquia();
            txtArbol.setText("=== ESTRUCTURA DEL ÁRBOL BINARIO DE BÚSQUEDA ===\n\n" + jerarquia);
        }
    }

    public void refrescarEstructuraHash() {
        String seleccion = cbTablas.getValue();
        if (seleccion == null) return;

        StringBuilder sb = new StringBuilder();
        int capacidad = 0;
        int tamaño = 0;

        if (seleccion.equals("Propietarios")) {
            MapaAsociativo<String, Propietario> mapa = service.obtenerMapaPropietarios();
            capacidad = mapa.getCapacidad();
            tamaño = mapa.tamaño();
            ListaEnlazada<MapaAsociativo.Entrada<String, Propietario>>[] buckets = mapa.getBuckets();
            
            sb.append("=== INSPECCIÓN DE CUBETAS: MAPA DE PROPIETARIOS ===\n\n");
            for (int i = 0; i < capacidad; i++) {
                sb.append(String.format("Cubeta [%02d]: ", i));
                if (buckets[i].estaVacia()) {
                    sb.append("(vacía)\n");
                } else {
                    boolean primero = true;
                    for (MapaAsociativo.Entrada<String, Propietario> entrada : buckets[i]) {
                        if (!primero) sb.append("  ==>  ");
                        sb.append(String.format("[%s : %s]", entrada.getClave(), entrada.getValor().getNombre()));
                        primero = false;
                    }
                    sb.append("\n");
                }
            }
        } else if (seleccion.equals("Mascotas")) {
            MapaAsociativo<String, Mascota> mapa = service.obtenerMapaMascotas();
            capacidad = mapa.getCapacidad();
            tamaño = mapa.tamaño();
            ListaEnlazada<MapaAsociativo.Entrada<String, Mascota>>[] buckets = mapa.getBuckets();

            sb.append("=== INSPECCIÓN DE CUBETAS: MAPA DE MASCOTAS ===\n\n");
            for (int i = 0; i < capacidad; i++) {
                sb.append(String.format("Cubeta [%02d]: ", i));
                if (buckets[i].estaVacia()) {
                    sb.append("(vacía)\n");
                } else {
                    boolean primero = true;
                    for (MapaAsociativo.Entrada<String, Mascota> entrada : buckets[i]) {
                        if (!primero) sb.append("  ==>  ");
                        sb.append(String.format("[%s : %s]", entrada.getClave(), entrada.getValor().getNombre()));
                        primero = false;
                    }
                    sb.append("\n");
                }
            }
        } else if (seleccion.equals("Citas")) {
            MapaAsociativo<String, Cita> mapa = service.obtenerMapaCitas();
            capacidad = mapa.getCapacidad();
            tamaño = mapa.tamaño();
            ListaEnlazada<MapaAsociativo.Entrada<String, Cita>>[] buckets = mapa.getBuckets();

            sb.append("=== INSPECCIÓN DE CUBETAS: MAPA DE CITAS ===\n\n");
            for (int i = 0; i < capacidad; i++) {
                sb.append(String.format("Cubeta [%02d]: ", i));
                if (buckets[i].estaVacia()) {
                    sb.append("(vacía)\n");
                } else {
                    boolean primero = true;
                    for (MapaAsociativo.Entrada<String, Cita> entrada : buckets[i]) {
                        if (!primero) sb.append("  ==>  ");
                        sb.append(String.format("[%s : Mascota %s (%s)]", entrada.getClave(), entrada.getValor().getIdMascota(), entrada.getValor().getFecha()));
                        primero = false;
                    }
                    sb.append("\n");
                }
            }
        }

        txtHash.setText(sb.toString());
        double factorCarga = (double) tamaño / capacidad;
        lblStatsHash.setText(String.format("Capacidad del Arreglo: %d | Cantidad de Elementos: %d | Factor de Carga Actual: %.4f (Límite: 0.7500)", 
                capacidad, tamaño, factorCarga));
    }
}
