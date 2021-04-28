package com.company;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReminderAlertBox {

    public static void Display(String title, String time, String style) {

        Stage window = new Stage();

        // Block input to other window until this popup is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Reminder for: " + title);
        window.setMinWidth(250);
        window.setMinHeight(250);
        Label label = new Label("Event " + title + " is starting at: " + time);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(style);
        scene.setUserAgentStylesheet(style);
        window.setScene(scene);
        window.showAndWait();

    }
}
