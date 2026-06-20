package com.veterinaria.vista;

import com.veterinaria.controlador.PropietarioController;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Propietario;

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

import java.util.ArrayList;

public class VistaPropietarios extends BorderPane {
    private final PropietarioController controller;

    private final TableView<Propietario> tabla;
    private final TextField txtId;
    private final TextField txtNombre;
    private final TextField txtTelefono;
    private final TextField txtEmail;
    private final TextField txtBuscar;

    public VistaPropietarios(PropietarioController controller) {
        this.controller = controller;
        this.setPadding(new Insets(20));

        // Título de la sección
        Label lblTitulo = new Label("GESTIÓN DE PROPIETARIOS");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitulo.setTextFill(Color.web("#2c3e50"));
        
        HBox topBar = new HBox(lblTitulo);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        this.setTop(topBar);

        // --- FORMULARIO (Izquierda) ---
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(15);
        formulario.setPadding(new Insets(10));
        formulario.setMinWidth(300);

        Label lblId = new Label("ID / Cédula *:");
        txtId = new TextField();
        txtId.setPromptText("Ej: 12345678");

        Label lblNombre = new Label("Nombre Completo *:");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Juan Pérez");

        Label lblTelefono = new Label("Teléfono:");
        txtTelefono = new TextField();
        txtTelefono.setPromptText("Ej: 3001234567");

        Label lblEmail = new Label("Email:");
        txtEmail = new TextField();
        txtEmail.setPromptText("Ej: juan@email.com");

        formulario.add(lblId, 0, 0);
        formulario.add(txtId, 1, 0);
        formulario.add(lblNombre, 0, 1);
        formulario.add(txtNombre, 1, 1);
        formulario.add(lblTelefono, 0, 2);
        formulario.add(txtTelefono, 1, 2);
        formulario.add(lblEmail, 0, 3);
        formulario.add(txtEmail, 1, 3);

        // Botones del formulario
        Button btnGuardar = new Button("Registrar");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox btnsForm = new HBox(10, btnGuardar, btnActualizar, btnLimpiar);
        btnsForm.setPadding(new Insets(10, 0, 0, 0));
        formulario.add(btnsForm, 0, 4, 2, 1);

        this.setLeft(formulario);

        // --- TABLA Y BÚSQUEDA (Centro) ---
        VBox centro = new VBox(15);
        centro.setPadding(new Insets(0, 0, 0, 20));

        // Barra de búsqueda y eliminación
        HBox barraAcciones = new HBox(10);
        barraAcciones.setAlignment(Pos.CENTER_LEFT);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por ID...");
        txtBuscar.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        Button btnRefrescar = new Button("Listar Todos");
        btnRefrescar.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("Eliminar Seleccionado");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        barraAcciones.getChildren().addAll(txtBuscar, btnBuscar, btnRefrescar, spacer, btnEliminar);

        // Estructura de la Tabla
        tabla = new TableView<>();
        
        TableColumn<Propietario, String> colId = new TableColumn<>("ID / Cédula");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(100);

        TableColumn<Propietario, String> colNombre = new TableColumn<>("Nombre Completo");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);

        TableColumn<Propietario, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTelefono.setPrefWidth(120);

        TableColumn<Propietario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(180);

        tabla.getColumns().addAll(colId, colNombre, colTelefono, colEmail);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        centro.getChildren().addAll(barraAcciones, tabla);
        this.setCenter(centro);

        // --- MANEJO DE EVENTOS ---
        
        // Cargar datos inicialmente
        refrescarTabla();

        // Doble click o selección en la tabla carga datos al formulario
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getId());
                txtId.setDisable(true); // Evitar cambiar el ID primario al actualizar
                txtNombre.setText(newSelection.getNombre());
                txtTelefono.setText(newSelection.getTelefono());
                txtEmail.setText(newSelection.getEmail());
            }
        });

        btnLimpiar.setOnAction(e -> limpiarFormulario());

        btnGuardar.setOnAction(e -> {
            try {
                controller.registrarPropietario(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText()
                );
                mostrarAlerta("Éxito", "Propietario registrado con éxito.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error de Registro", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnActualizar.setOnAction(e -> {
            try {
                controller.actualizarPropietario(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText()
                );
                mostrarAlerta("Éxito", "Datos de propietario actualizados.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error al Actualizar", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnEliminar.setOnAction(e -> {
            Propietario seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Advertencia", "Seleccione un propietario de la tabla para eliminar.", Alert.AlertType.WARNING);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminación");
            confirm.setHeaderText("¿Eliminar a " + seleccionado.getNombre() + "?");
            confirm.setContentText("¡ADVERTENCIA! Se eliminarán en cascada todas sus mascotas y citas programadas asociadas.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.eliminarPropietario(seleccionado.getId());
                    mostrarAlerta("Eliminado", "Propietario y dependencias eliminados.", Alert.AlertType.INFORMATION);
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
            Propietario p = controller.buscarPropietario(busqueda);
            if (p != null) {
                tabla.setItems(FXCollections.observableArrayList(p));
            } else {
                tabla.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Búsqueda", "No se encontró ningún propietario con el ID: " + busqueda, Alert.AlertType.INFORMATION);
            }
        });

        btnRefrescar.setOnAction(e -> {
            txtBuscar.clear();
            refrescarTabla();
        });
    }

    public void refrescarTabla() {
        ListaEnlazada<Propietario> lista = controller.listarPropietarios();
        ArrayList<Propietario> arrayList = new ArrayList<>();
        for (Propietario p : lista) {
            arrayList.add(p);
        }
        ObservableList<Propietario> obsList = FXCollections.observableArrayList(arrayList);
        tabla.setItems(obsList);
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtId.setDisable(false);
        txtNombre.clear();
        txtTelefono.clear();
        txtEmail.clear();
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
