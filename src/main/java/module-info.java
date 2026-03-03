module com.antoine.lab1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.antoine.lab1 to javafx.fxml;
    exports com.antoine.lab1;
    exports com.antoine.lab1.ui;
}