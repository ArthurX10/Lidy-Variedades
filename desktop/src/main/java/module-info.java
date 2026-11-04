module org.example.lidyvariedadesdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires java.desktop;

    opens org.example.lidyvariedadesdesktop to javafx.fxml, com.google.gson, javafx.base;
    opens org.example.lidyvariedadesdesktop.service to com.google.gson;

    exports org.example.lidyvariedadesdesktop;
    exports org.example.lidyvariedadesdesktop.service;
}
