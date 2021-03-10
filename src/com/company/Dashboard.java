package com.company;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Dashboard extends Application {


    Stage window;

    @Override
    public void start(Stage stage) throws Exception {

        window = stage;
        window.setTitle("StudyBuddy - Dashboard");
        window.setMinWidth(1000);
        window.setMinHeight(500);
        // Base grid layout
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));


        // TilePane for calender
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefRows(2);
        tile.setStyle("-fx-background-color: DAE6F3;");
        Button[] buttons = new Button[8];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button("Button " + i);
            tile.getChildren().add(buttons[i]);
        }

        // Vbox for side options
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button[] sideButtons = new Button[8];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button("Button " + i);
            vbox.getChildren().add(buttons[i]);
        }

        borderPane.setLeft(vbox);
        borderPane.setCenter(tile);
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();


    }
}
