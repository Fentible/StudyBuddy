package com.company;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
        borderPane.setPadding(new Insets(0, 0, 0, 0));


        // TilePane for calender
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(10, 5, 5, 5));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefRows(2);
        // tile.setStyle("-fx-background-color: DAE6F3;");
        // Only for testing
        Button[] buttons = new Button[8];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button("Button " + i);
            tile.getChildren().add(buttons[i]);
        }

        // Vbox for side options
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        // Only for testing
        Button[] sideButtons = new Button[8];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button("Button " + i);
            vbox.getChildren().add(buttons[i]);
        }

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        file.getItems().add(openFile);
        menuBar.getMenus().add(file);
        VBox menuVBox= new VBox(menuBar);

        borderPane.setTop(menuVBox);
        borderPane.setLeft(vbox);
        borderPane.setCenter(tile);
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();


    }
}
