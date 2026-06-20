package com.veterinaria.vista;

import com.veterinaria.controlador.MascotaController;
import com.veterinaria.controlador.PropietarioController;
import com.veterinaria.estructuras.ListaEnlazada;
import com.veterinaria.model.Mascota;
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
import javafx.util.StringConverter;

import java.util.ArrayList;

public class VistaMascotas extends BorderPane {
    private final MascotaController controller;
    private final PropietarioController propietarioController;

    private final TableView<Mascota> tabla;
    private final TextField txtId;
    private final TextField txtNombre;
    private final TextField txtEspecie;
    private final TextField txtRaza;
    private final TextField txtEdad;
    private final TextField txtPeso;
    private final ComboBox<Propietario> cbPropietario;
    private final TextField txtBuscar;

    public VistaMascotas(MascotaController controller, PropietarioController propietarioController) {
        this.controller = controller;
        this.propietarioController = propietarioController;
        this.setPadding(new Insets(20));

        // Título de sección
        Label lblTitulo = new Label("GESTIÓN DE MASCOTAS");
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

        Label lblId = new Label("Código Único *:");
        txtId = new TextField();
        txtId.setPromptText("Ej: M001");

        Label lblNombre = new Label("Nombre Mascota *:");
        txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Firulais");

        Label lblEspecie = new Label("Especie:");
        txtEspecie = new TextField();
        txtEspecie.setPromptText("Ej: Perro, Gato");

        Label lblRaza = new Label("Raza:");
        txtRaza = new TextField();
        txtRaza.setPromptText("Ej: Criollo, Siamés");

        Label lblEdad = new Label("Edad (años) *:");
        txtEdad = new TextField();
        txtEdad.setPromptText("Ej: 3");

        Label lblPeso = new Label("Peso (kg) *:");
        txtPeso = new TextField();
        txtPeso.setPromptText("Ej: 12.5");

        Label lblPropietario = new Label("Dueño / Propietario *:");
        cbPropietario = new ComboBox<>();
        cbPropietario.setPromptText("Seleccione dueño...");
        cbPropietario.setPrefWidth(200);

        // Configurar despliegue del ComboBox de propietarios
        cbPropietario.setConverter(new StringConverter<Propietario>() {
            @Override
            public String toString(Propietario p) {
                if (p == null) return "";
                return p.getNombre() + " (" + p.getId() + ")";
            }

            @Override
            public Propietario fromString(String string) {
                return null;
            }
        });

        formulario.add(lblId, 0, 0);
        formulario.add(txtId, 1, 0);
        formulario.add(lblNombre, 0, 1);
        formulario.add(txtNombre, 1, 1);
        formulario.add(lblEspecie, 0, 2);
        formulario.add(txtEspecie, 1, 2);
        formulario.add(lblRaza, 0, 3);
        formulario.add(txtRaza, 1, 3);
        formulario.add(lblEdad, 0, 4);
        formulario.add(txtEdad, 1, 4);
        formulario.add(lblPeso, 0, 5);
        formulario.add(txtPeso, 1, 5);
        formulario.add(lblPropietario, 0, 6);
        formulario.add(cbPropietario, 1, 6);

        // Botones del formulario
        Button btnGuardar = new Button("Registrar");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox btnsForm = new HBox(10, btnGuardar, btnActualizar, btnLimpiar);
        btnsForm.setPadding(new Insets(10, 0, 0, 0));
        formulario.add(btnsForm, 0, 7, 2, 1);

        this.setLeft(formulario);

        // --- TABLA Y BÚSQUEDA (Centro) ---
        VBox centro = new VBox(15);
        centro.setPadding(new Insets(0, 0, 0, 20));

        HBox barraAcciones = new HBox(10);
        barraAcciones.setAlignment(Pos.CENTER_LEFT);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por código...");
        txtBuscar.setPrefWidth(180);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        Button btnRefrescar = new Button("Listar Todos");
        btnRefrescar.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("Eliminar Mascota");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        barraAcciones.getChildren().addAll(txtBuscar, btnBuscar, btnRefrescar, spacer, btnEliminar);

        // Estructura de la Tabla de Mascotas
        tabla = new TableView<>();
        
        TableColumn<Mascota, String> colId = new TableColumn<>("Código");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(80);

        TableColumn<Mascota, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(120);

        TableColumn<Mascota, String> colEspecie = new TableColumn<>("Especie");
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colEspecie.setPrefWidth(100);

        TableColumn<Mascota, String> colRaza = new TableColumn<>("Raza");
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colRaza.setPrefWidth(100);

        TableColumn<Mascota, Integer> colEdad = new TableColumn<>("Edad (Años)");
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colEdad.setPrefWidth(90);

        TableColumn<Mascota, Double> colPeso = new TableColumn<>("Peso (kg)");
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colPeso.setPrefWidth(85);

        TableColumn<Mascota, String> colIdDueno = new TableColumn<>("ID Propietario");
        colIdDueno.setCellValueFactory(new PropertyValueFactory<>("idPropietario"));
        colIdDueno.setPrefWidth(110);

        tabla.getColumns().addAll(colId, colNombre, colEspecie, colRaza, colEdad, colPeso, colIdDueno);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        centro.getChildren().addAll(barraAcciones, tabla);
        this.setCenter(centro);

        // --- MANEJO DE EVENTOS ---
        
        refrescarTabla();
        actualizarComboPropietarios();

        // Al seleccionar de la tabla, cargar en formulario
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getId());
                txtId.setDisable(true);
                txtNombre.setText(newSelection.getNombre());
                txtEspecie.setText(newSelection.getEspecie());
                txtRaza.setText(newSelection.getRaza());
                txtEdad.setText(String.valueOf(newSelection.getEdad()));
                txtPeso.setText(String.valueOf(newSelection.getPeso()));

                // Seleccionar al propietario en el Combo
                Propietario p = propietarioController.buscarPropietario(newSelection.getIdPropietario());
                if (p != null) {
                    cbPropietario.setValue(p);
                }
            }
        });

        btnLimpiar.setOnAction(e -> limpiarFormulario());

        btnGuardar.setOnAction(e -> {
            Propietario propSeleccionado = cbPropietario.getValue();
            if (propSeleccionado == null) {
                mostrarAlerta("Error", "Debe seleccionar un propietario.", Alert.AlertType.ERROR);
                return;
            }
            try {
                controller.registrarMascota(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtEspecie.getText(),
                    txtRaza.getText(),
                    txtEdad.getText(),
                    txtPeso.getText(),
                    propSeleccionado.getId()
                );
                mostrarAlerta("Éxito", "Mascota registrada con éxito e insertada en el Árbol Binario de Búsqueda.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error de Registro", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnActualizar.setOnAction(e -> {
            Propietario propSeleccionado = cbPropietario.getValue();
            if (propSeleccionado == null) {
                mostrarAlerta("Error", "Debe seleccionar un propietario.", Alert.AlertType.ERROR);
                return;
            }
            try {
                controller.actualizarMascota(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtEspecie.getText(),
                    txtRaza.getText(),
                    txtEdad.getText(),
                    txtPeso.getText(),
                    propSeleccionado.getId()
                );
                mostrarAlerta("Éxito", "Mascota actualizada correctamente.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                refrescarTabla();
            } catch (Exception ex) {
                mostrarAlerta("Error al Actualizar", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnEliminar.setOnAction(e -> {
            Mascota seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarAlerta("Advertencia", "Seleccione una mascota para eliminar.", Alert.AlertType.WARNING);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminación");
            confirm.setHeaderText("¿Eliminar mascota " + seleccionada.getNombre() + " (" + seleccionada.getId() + ")?");
            confirm.setContentText("Se cancelarán todas sus citas programadas asociadas.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.eliminarMascota(seleccionada.getId());
                    mostrarAlerta("Eliminado", "Mascota eliminada.", Alert.AlertType.INFORMATION);
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
            Mascota m = controller.buscarMascota(busqueda);
            if (m != null) {
                tabla.setItems(FXCollections.observableArrayList(m));
            } else {
                tabla.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Búsqueda", "No se encontró ninguna mascota con el código: " + busqueda, Alert.AlertType.INFORMATION);
            }
        });

        btnRefrescar.setOnAction(e -> {
            txtBuscar.clear();
            refrescarTabla();
        });
    }

    public void refrescarTabla() {
        ListaEnlazada<Mascota> lista = controller.listarMascotas();
        ArrayList<Mascota> arrayList = new ArrayList<>();
        for (Mascota m : lista) {
            arrayList.add(m);
        }
        ObservableList<Mascota> obsList = FXCollections.observableArrayList(arrayList);
        tabla.setItems(obsList);
    }

    public void actualizarComboPropietarios() {
        ListaEnlazada<Propietario> lista = propietarioController.listarPropietarios();
        ArrayList<Propietario> arrayList = new ArrayList<>();
        for (Propietario p : lista) {
            arrayList.add(p);
        }
        cbPropietario.setItems(FXCollections.observableArrayList(arrayList));
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtId.setDisable(false);
        txtNombre.clear();
        txtEspecie.clear();
        txtRaza.clear();
        txtEdad.clear();
        txtPeso.clear();
        cbPropietario.setValue(null);
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
