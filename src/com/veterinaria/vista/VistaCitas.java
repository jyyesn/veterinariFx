package com.veterinaria.vista;

import com.veterinaria.controlador.CitaController;
import com.veterinaria.controlador.MascotaController;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Cita;
import com.veterinaria.model.Mascota;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class VistaCitas extends BorderPane {
    private final CitaController controller;
    private final MascotaController mascotaController;

    private final TableView<Cita> tabla;
    private final TextField txtId;
    private final TextField txtFecha;
    private final TextField txtHora;
    private final ComboBox<Mascota> cbMascota;
    private final TextArea txtMotivo;
    private final TextField txtBuscar;

    public VistaCitas(CitaController controller, MascotaController mascotaController) {
        this.controller = controller;
        this.mascotaController = mascotaController;
        this.setPadding(new Insets(20));

        // Título de la sección
        Label lblTitulo = new Label("GESTIÓN DE CITAS VETERINARIAS");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.web("#2c3e50"));
        
        HBox topBar = new HBox(lblTitulo);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        this.setTop(topBar);

        // --- FORMULARIO (Izquierda) ---
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(10));
        formulario.setMinWidth(320);

        Label lblId = new Label("ID de Cita *:");
        txtId = new TextField();
        txtId.setPromptText("Ej: C001");

        Label lblFecha = new Label("Fecha (AAAA-MM-DD) *:");
        txtFecha = new TextField();
        txtFecha.setPromptText("Ej: 2026-06-25");

        Label lblHora = new Label("Hora (HH:MM) *:");
        txtHora = new TextField();
        txtHora.setPromptText("Ej: 14:30");

        Label lblMascota = new Label("Mascota Paciente *:");
        cbMascota = new ComboBox<>();
        cbMascota.setPromptText("Seleccione mascota...");
        cbMascota.setPrefWidth(200);

        // Configurar selector de mascotas
        cbMascota.setConverter(new StringConverter<Mascota>() {
            @Override
            public String toString(Mascota m) {
                if (m == null) return "";
                return m.getNombre() + " (" + m.getId() + ")";
            }

            @Override
            public Mascota fromString(String string) {
                return null;
            }
        });

        Label lblMotivo = new Label("Motivo de consulta *:");
        txtMotivo = new TextArea();
        txtMotivo.setPromptText("Escriba aquí los síntomas o tratamiento a realizar...");
        txtMotivo.setPrefRowCount(4);
        txtMotivo.setWrapText(true);

        formulario.add(lblId, 0, 0);
        formulario.add(txtId, 1, 0);
        formulario.add(lblFecha, 0, 1);
        formulario.add(txtFecha, 1, 1);
        formulario.add(lblHora, 0, 2);
        formulario.add(txtHora, 1, 2);
        formulario.add(lblMascota, 0, 3);
        formulario.add(cbMascota, 1, 3);
        formulario.add(lblMotivo, 0, 4);
        formulario.add(txtMotivo, 1, 4);

        // Botones del formulario
        Button btnGuardar = new Button("Programar Cita");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox btnsForm = new HBox(10, btnGuardar, btnActualizar, btnLimpiar);
        btnsForm.setPadding(new Insets(10, 0, 0, 0));
        formulario.add(btnsForm, 0, 5, 2, 1);

        this.setLeft(formulario);

        // --- TABLA Y BÚSQUEDA (Centro) ---
        VBox centro = new VBox(15);
        centro.setPadding(new Insets(0, 0, 0, 20));

        HBox barraAcciones = new HBox(10);
        barraAcciones.setAlignment(Pos.CENTER_LEFT);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por ID de cita...");
        txtBuscar.setPrefWidth(180);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        Button btnRefrescar = new Button("Listar Todas");
        btnRefrescar.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("Cancelar Cita Seleccionada");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        barraAcciones.getChildren().addAll(txtBuscar, btnBuscar, btnRefrescar, spacer, btnEliminar);

        // Estructura de la Tabla de Citas
        tabla = new TableView<>();
        
        TableColumn<Cita, String> colId = new TableColumn<>("ID Cita");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(80);

        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setPrefWidth(100);

        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colHora.setPrefWidth(80);

        TableColumn<Cita, String> colIdMascota = new TableColumn<>("Código Mascota");
        colIdMascota.setCellValueFactory(new PropertyValueFactory<>("idMascota"));
        colIdMascota.setPrefWidth(120);

        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colMotivo.setPrefWidth(220);

        tabla.getColumns().addAll(colId, colFecha, colHora, colIdMascota, colMotivo);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        centro.getChildren().addAll(barraAcciones, tabla);
        this.setCenter(centro);

        // --- MANEJO DE EVENTOS ---
        
        refrescarTabla();
        actualizarComboMascotas();

        // Al seleccionar fila, cargar datos al formulario
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getId());
                txtId.setDisable(true);
                txtFecha.setText(newSelection.getFecha());
                txtHora.setText(newSelection.getHora());
                txtMotivo.setText(newSelection.getMotivo());

                Mascota m = mascotaController.buscarMascota(newSelection.getIdMascota());
                if (m != null) {
                    cbMascota.setValue(m);
                }
            }
        });

        btnLimpiar.setOnAction(e -> limpiarFormulario());

        btnGuardar.setOnAction(e -> {
            Mascota mascotaSeleccionada = cbMascota.getValue();
            if (mascotaSeleccionada == null) {
                mostrarAlerta("Error", "Debe seleccionar una mascota.", Alert.AlertType.ERROR);
                return;
            }
            try {
                controller.programarCita(
                    txtId.getText(),
                    txtFecha.getText(),
                    txtHora.getText(),
                    mascotaSeleccionada.getId(),
                    txtMotivo.getText()
                );
                mostrarAlerta("Éxito", "Cita programada exitosamente.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error al Agendar", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnActualizar.setOnAction(e -> {
            Mascota mascotaSeleccionada = cbMascota.getValue();
            if (mascotaSeleccionada == null) {
                mostrarAlerta("Error", "Debe seleccionar una mascota.", Alert.AlertType.ERROR);
                return;
            }
            try {
                controller.actualizarCita(
                    txtId.getText(),
                    txtFecha.getText(),
                    txtHora.getText(),
                    mascotaSeleccionada.getId(),
                    txtMotivo.getText()
                );
                mostrarAlerta("Éxito", "Cita actualizada correctamente.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error al Actualizar", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnEliminar.setOnAction(e -> {
            Cita seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarAlerta("Advertencia", "Seleccione una cita de la tabla para cancelar.", Alert.AlertType.WARNING);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Cancelar Cita");
            confirm.setHeaderText("¿Seguro que desea cancelar la cita ID: " + seleccionada.getId() + "?");
            confirm.setContentText("Esta operación eliminará permanentemente la cita programada del sistema.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.cancelarCita(seleccionada.getId());
                    mostrarAlerta("Cancelada", "Cita cancelada correctamente.", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                    refrescarTabla();
                }
            });
        });

        btnBuscar.setOnAction(e -> {
            String busqueda = txtBuscar.getText().trim();
            if (busqueda.isEmpty()) {
                refrescarTabla();
                return;
            }
            Cita c = controller.buscarCita(busqueda);
            if (c != null) {
                tabla.setItems(FXCollections.observableArrayList(c));
            } else {
                tabla.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Búsqueda", "No se encontró ninguna cita con el ID: " + busqueda, Alert.AlertType.INFORMATION);
            }
        });

        btnRefrescar.setOnAction(e -> {
            txtBuscar.clear();
            refrescarTabla();
        });
    }

    public void refrescarTabla() {
        ListaEnlazada<Cita> lista = controller.listarCitas();
        ArrayList<Cita> arrayList = new ArrayList<>();
        for (Cita c : lista) {
            arrayList.add(c);
        }
        ObservableList<Cita> obsList = FXCollections.observableArrayList(arrayList);
        tabla.setItems(obsList);
    }

    public void actualizarComboMascotas() {
        ListaEnlazada<Mascota> lista = mascotaController.listarMascotas();
        ArrayList<Mascota> arrayList = new ArrayList<>();
        for (Mascota m : lista) {
            arrayList.add(m);
        }
        cbMascota.setItems(FXCollections.observableArrayList(arrayList));
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtId.setDisable(false);
        txtFecha.clear();
        txtHora.clear();
        cbMascota.setValue(null);
        txtMotivo.clear();
        tabla.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
