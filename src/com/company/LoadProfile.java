package com.company;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoadProfile {

    public static String saveLocation;

    public static String Display() throws IOException {

        Stage window = new Stage();
        window.setMinWidth(250);
        window.setMinHeight(200);
        window.setTitle("Select Your Semester Profile");
        FileChooser fileChooser = new FileChooser();
        Button fileSelector = new Button("Choose File");
        Button confirm = new Button("Confirm");
        Label label = new Label("Please select your 'semester_profile' file");
        Label errorMsg = new Label("");
        TextField inputLocation = new TextField();


        VBox container = new VBox(8);
        container.getChildren().addAll(label, fileSelector, confirm, errorMsg);
        container.setAlignment(Pos.CENTER);

        fileSelector.setOnAction(event -> {
            File selected = fileChooser.showOpenDialog(window);
            inputLocation.setText(selected.getAbsolutePath());
            if(selected.getName().equals("semester_profile")) {
                saveLocation = selected.getAbsolutePath();
                window.close();
            } else {
                errorMsg.setText(selected.getName() + " is not accepted, Please choose a 'semester_profile' file");
            }
        });

        Scene scene = new Scene(container);
        window.setScene(scene);
        window.showAndWait();

        return saveLocation;
    }
}
