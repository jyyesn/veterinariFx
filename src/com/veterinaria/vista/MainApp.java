package com.veterinaria.vista;

import com.veterinaria.controlador.CitaController;
import com.veterinaria.controlador.MascotaController;
import com.veterinaria.controlador.PropietarioController;
import com.veterinaria.service.VeterinariaService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {
    private VeterinariaService service;
    private PropietarioController propietarioController;
    private MascotaController mascotaController;
    private CitaController citaController;

    private VistaPropietarios vistaPropietarios;
    private VistaMascotas vistaMascotas;
    private VistaCitas vistaCitas;
    private VistaEstructuras vistaEstructuras;

    // Elementos del Dashboard
    private Label lblCantPropietarios;
    private Label lblCantMascotas;
    private Label lblCantCitas;

    @Override
    public void start(Stage primaryStage) {
        // 1. Inicializar la capa de Modelo (Servicios) y Controladores
        this.service = new VeterinariaService();
        this.propietarioController = new PropietarioController(service);
        this.mascotaController = new MascotaController(service);
        this.citaController = new CitaController(service);

        // 2. Crear las Vistas correspondientes
        this.vistaPropietarios = new VistaPropietarios(propietarioController);
        this.vistaMascotas = new VistaMascotas(mascotaController, propietarioController);
        this.vistaCitas = new VistaCitas(citaController, mascotaController);
        this.vistaEstructuras = new VistaEstructuras(service, mascotaController);

        // 3. Configurar contenedor principal
        BorderPane root = new BorderPane();

        // Banner superior general
        HBox banner = new HBox();
        banner.setPadding(new Insets(15, 20, 15, 20));
        banner.setBackground(new Background(new BackgroundFill(Color.web("#2c3e50"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        Label lblAppName = new Label("SISTEMA DE GESTIÓN VETERINARIA (MVC + POO)");
        lblAppName.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblAppName.setTextFill(Color.WHITE);
        
        banner.getChildren().add(lblAppName);
        root.setTop(banner);

        // TabPane principal para navegación
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Pestaña 1: Inicio (Dashboard)
        Tab tabInicio = new Tab("Inicio");
        tabInicio.setContent(crearPanelDashboard());

        // Pestaña 2: Propietarios
        Tab tabPropietarios = new Tab("Propietarios");
        tabPropietarios.setContent(vistaPropietarios);

        // Pestaña 3: Mascotas
        Tab tabMascotas = new Tab("Mascotas");
        tabMascotas.setContent(vistaMascotas);

        // Pestaña 4: Citas Médicas
        Tab tabCitas = new Tab("Citas Médicas");
        tabCitas.setContent(vistaCitas);

        // Pestaña 5: Diagnóstico Estructuras
        Tab tabEstructuras = new Tab("Diagnóstico Estructuras");
        tabEstructuras.setContent(vistaEstructuras);

        tabPane.getTabs().addAll(tabInicio, tabPropietarios, tabMascotas, tabCitas, tabEstructuras);
        root.setCenter(tabPane);

        // --- SINCRONIZACIÓN DE PESTAÑAS (Mantenimiento de Datos) ---
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                String titulo = newTab.getText();
                switch (titulo) {
                    case "Inicio":
                        actualizarDashboardStats();
                        break;
                    case "Mascotas":
                        vistaMascotas.actualizarComboPropietarios();
                        vistaMascotas.refrescarTabla();
                        break;
                    case "Citas Médicas":
                        vistaCitas.actualizarComboMascotas();
                        vistaCitas.refrescarTabla();
                        break;
                    case "Diagnóstico Estructuras":
                        vistaEstructuras.refrescarEstructuraArbol();
                        vistaEstructuras.refrescarEstructuraHash();
                        break;
                    case "Propietarios":
                        vistaPropietarios.refrescarTabla();
                        break;
                }
            }
        });

        // 4. Crear escena y lanzar Stage
        Scene scene = new Scene(root, 1000, 650);
        
        // Cargar un estilo básico de JavaFX (opcional, pero mejora la visualización)
        // scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap");

        primaryStage.setTitle("Gestor Veterinario - Arquitectura MVC & Estructuras de Datos");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // Cargar estadísticas iniciales
        actualizarDashboardStats();
    }

    private VBox crearPanelDashboard() {
        VBox layout = new VBox(25);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        // Mensaje de Bienvenida
        Label lblBienvenida = new Label("¡Bienvenido al Gestor de la Clínica Veterinaria!");
        lblBienvenida.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblBienvenida.setTextFill(Color.web("#2c3e50"));

        Label lblDescripcion = new Label("Este panel resume el estado actual del sistema y la base de datos CSV.");
        lblDescripcion.setFont(Font.font("System", 14));
        lblDescripcion.setTextFill(Color.web("#7f8c8d"));

        // Contenedor de Tarjetas de Estadísticas (KPIs)
        HBox kpiContainer = new HBox(20);
        kpiContainer.setAlignment(Pos.CENTER);
        kpiContainer.setPadding(new Insets(20, 0, 20, 0));

        // Tarjeta 1: Propietarios
        VBox cardProp = crearTarjetaKpi("Propietarios Registrados", "0", "#3498db");
        lblCantPropietarios = (Label) cardProp.getChildren().get(1);

        // Tarjeta 2: Mascotas
        VBox cardMasc = crearTarjetaKpi("Mascotas Registradas", "0", "#2ecc71");
        lblCantMascotas = (Label) cardMasc.getChildren().get(1);

        // Tarjeta 3: Citas Agendadas
        VBox cardCitas = crearTarjetaKpi("Citas Programadas", "0", "#f1c40f");
        lblCantCitas = (Label) cardCitas.getChildren().get(1);

        kpiContainer.getChildren().addAll(cardProp, cardMasc, cardCitas);

        // Panel de información de estructuras implementadas
        VBox panelInfo = new VBox(10);
        panelInfo.setPadding(new Insets(20));
        panelInfo.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-background-radius: 8;");
        panelInfo.setMaxWidth(700);

        Label lblInfoTitulo = new Label("Estructuras de Datos Personalizadas Utilizadas:");
        lblInfoTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblInfoTitulo.setTextFill(Color.web("#2c3e50"));

        Label lblInfoEstructuras = new Label(
            "• Lista Enlazada (Lineal): Administra la cola dinámica de registros.\n" +
            "• Mapa Asociativo / Tabla Hash (Asociativa): Permite consultas O(1) de propietarios, mascotas y citas por ID.\n" +
            "• Árbol Binario de Búsqueda (No Lineal): Mantiene organizados alfabéticamente los registros de las mascotas.\n" +
            "• Persistencia en CSV: Todos los cambios se guardan directamente en el disco automáticamente."
        );
        lblInfoEstructuras.setFont(Font.font("System", 13));
        lblInfoEstructuras.setLineSpacing(5);

        panelInfo.getChildren().addAll(lblInfoTitulo, lblInfoEstructuras);

        layout.getChildren().addAll(lblBienvenida, lblDescripcion, kpiContainer, panelInfo);
        return layout;
    }

    private VBox crearTarjetaKpi(String titulo, String valorInicial, String hexColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(220);
        card.setMinHeight(120);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + hexColor + ";" +
            "-fx-border-width: 0 0 5 0;" + // Borde grueso inferior
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);"
        );

        Label lblTitulo = new Label(titulo.toUpperCase());
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 11));
        lblTitulo.setTextFill(Color.web("#95a5a6"));

        Label lblValor = new Label(valorInicial);
        lblValor.setFont(Font.font("System", FontWeight.BOLD, 36));
        lblValor.setTextFill(Color.web("#2c3e50"));

        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }

    private void actualizarDashboardStats() {
        if (service == null) return;
        int propCount = service.listarPropietarios().tamaño();
        int mascCount = service.listarMascotas().tamaño();
        int citasCount = service.listarCitas().tamaño();

        lblCantPropietarios.setText(String.valueOf(propCount));
        lblCantMascotas.setText(String.valueOf(mascCount));
        lblCantCitas.setText(String.valueOf(citasCount));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
