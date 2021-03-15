package com.company;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SaveDialogBox {

    static String location;

    public void Display(String title, String message) throws IOException {

        Stage window = new Stage();

        // Block input to other window until this popup is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(200);
        Label label = new Label(message);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField inputLocation = new TextField();
        Button directorySelect = new Button("Find Directory");
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/com/company/model/config.properties"));
        inputLocation.setText(properties.getProperty("location"));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        cancelButton.setOnAction(event -> {
            location = null;
            window.close();
        });
        directorySelect.setOnAction(event -> {
            File selected = directoryChooser.showDialog(window);
            inputLocation.setText(selected.getAbsolutePath());
        });
        saveButton.setOnAction(event -> {
            location = inputLocation.getText();
            window.close();
        });

        VBox layout = new VBox(20);
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5,10,5,10));
        buttons.getChildren().addAll(cancelButton, saveButton, directorySelect);
        layout.getChildren().addAll(label, inputLocation, buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return;
    }
}
