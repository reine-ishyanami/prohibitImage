module com.reine.prohibitimage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.reine.prohibitimage.fxcontroller to javafx.fxml;
    exports com.reine.prohibitimage;
}