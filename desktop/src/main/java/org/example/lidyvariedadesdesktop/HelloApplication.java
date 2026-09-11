package org.example.lidyvariedadesdesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 980, 620);
        
        // Aplica folha de estilo moderna
        if (HelloApplication.class.getResource("style.css") != null) {
            scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        }

        stage.setTitle("Lidy Variedades - Sistema de Gestão");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }
