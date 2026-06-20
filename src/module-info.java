module com.veterinaria {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.veterinaria.vista;
    opens com.veterinaria.controlador;
    opens com.veterinaria.model;

    exports com.veterinaria.vista;
    exports com.veterinaria.model;
    exports com.veterinaria.controlador;
    exports com.veterinaria.dao;
    exports com.veterinaria.estructuras;
    exports com.veterinaria.service;
}
