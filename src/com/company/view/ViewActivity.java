package com.company.view;

import com.company.Reminder;
import com.company.edit.EditActivity;
import com.company.model.Activity;
import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;


public class ViewActivity {

    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static void Display(SemesterProfile semesterProfile, Activity activity) {


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + activity.getTitle());
        window.setHeight(579);
        window.setWidth(700);
        // Opens window slightly offset from the current one
        List<Window> open = Stage.getWindows();
        double xPos = 0;
        double yPos = 0;
        for(Window window1: open) {
            if(window1.getX() > xPos) {
                xPos = window1.getX();
                yPos = window1.getY();
            }
        }
        window.setX(xPos + 50);
        window.setY(yPos + 50);
        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(
                new KeyFrame(new Duration(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        window.setWidth(window.getWidth() + 5);
                        window.setHeight(window.getHeight() + 5);
                        if(window.getWidth() >= 1000 && window.getHeight() >= 875) {
                            tl.stop();
                        }
                    }
                })
        );
        tl.play();


        Button cancelButton = new Button("Close");
        Button editButton = new Button("Edit");
        Button addReminder = new Button("Set Reminder");
        addReminder.setOnAction(e -> {
            semesterProfile.addReminder(new Reminder(activity));
        });

        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton, addReminder);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
            EditActivity.Display(semesterProfile, activity, window);
        });


        Label title = new Label(activity.getTitle());

        TextArea notes = new TextArea();
        notes.setText(activity.getNotes());
        notes.setEditable(false);

        TextField endDate = new TextField();
        endDate.setText(activity.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(activity.getTimeSpent());
        progress.setDisable(true);

        Slider contribution = new Slider();
        contribution.setValue(activity.getContribution());
        contribution.setDisable(true);

        Button type = new Button();
        type.setText(activity.getType().toString());



        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(activity.getRelatedTasks()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setPrefWidth(750);
        tableOfDependencies.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfDependencies.setRowFactory(e -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null) {
                        ViewTask.Display(semesterProfile, row.getItem());
                    }
                }
            });
            return row;
        });
        tableOfDependencies.getColumns().addAll(dependencyNameColumn, dependencyCompletionColumn, dependencyDateColumn);

        tableOfDependencies.setPlaceholder(new Label("Table of Dependencies"));


        HBox timeBox = new HBox(8); // dates HBox
        timeBox.getChildren().addAll(type, endDate);

        GridPane gridpane = new GridPane(); // GridPane for most of the elements
        gridpane.prefWidthProperty().bind(window.widthProperty());

        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(new Label("View Task: "), 0, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Date range: "), 0, 2);
        gridpane.add(timeBox, 1, 2);
        Label progressLabel = new Label("Progress: " + progress.getValue());
        gridpane.add(progressLabel, 0 ,3);
        gridpane.add(progress, 1, 3);
        gridpane.add(notes, 0, 4, 2, 2);

        HBox tables = new HBox(8);
        tables.getChildren().addAll(tableOfDependencies);
        tables.setPadding(new Insets(15,15,15,15));
        tables.setMaxWidth(950);

        VBox container = new VBox(8);
        confirmButtons.setAlignment(Pos.CENTER);
        tables.setAlignment(Pos.CENTER);
        container.getChildren().addAll(gridpane, tables, confirmButtons);
        container.setPadding(new Insets(15,15,15,15));

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sp.setContent(container);
        scene = new Scene(sp);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.show();


    }

    public static void Display(SemesterProfile semesterProfile, Activity activity, Stage window) {

        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + activity.getTitle());
        window.setHeight(579);
        window.setWidth(700);
        //window.setHeight(875);
        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(
                new KeyFrame(new Duration(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        window.setWidth(window.getWidth() + 5);
                        window.setHeight(window.getHeight() + 5);
                        if(window.getWidth() >= 1000 && window.getHeight() >= 879) {
                            tl.stop();
                        }
                    }
                })
        );
        tl.play();
        Button cancelButton = new Button("Close");
        Button editButton = new Button("Edit");
        Button addReminder = new Button("Set Reminder");
        addReminder.setOnAction(e -> {
            semesterProfile.addReminder(new Reminder(activity));
        });

        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton, addReminder);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
            EditActivity.Display(semesterProfile, activity, window);
        });

        Label title = new Label(activity.getTitle());

        TextArea notes = new TextArea();
        notes.setText(activity.getNotes());
        notes.setEditable(false);

        TextField endDate = new TextField();
        endDate.setText(activity.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(activity.getTimeSpent());
        progress.setDisable(true);

        Slider contribution = new Slider();
        contribution.setValue(activity.getContribution());
        contribution.setDisable(true);

        Button type = new Button();
        type.setText(activity.getType().toString());

        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(activity.getRelatedTasks()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setPrefWidth(750);
        tableOfDependencies.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfDependencies.setRowFactory(e -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null) {
                        ViewTask.Display(semesterProfile, row.getItem());
                    }
                }
            });
            return row;
        });
        tableOfDependencies.getColumns().addAll(dependencyNameColumn, dependencyCompletionColumn, dependencyDateColumn);

        tableOfDependencies.setPlaceholder(new Label("Table of Dependencies"));


        HBox timeBox = new HBox(8); // dates HBox
        timeBox.getChildren().addAll(type, endDate);

        GridPane gridpane = new GridPane(); // GridPane for most of the elements
        gridpane.prefWidthProperty().bind(window.widthProperty());

        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(new Label("View Task: "), 0, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Date range: "), 0, 2);
        gridpane.add(timeBox, 1, 2);
        Label progressLabel = new Label("Progress: " + progress.getValue());
        gridpane.add(progressLabel, 0 ,3);
        gridpane.add(progress, 1, 3);
        gridpane.add(notes, 0, 4, 2, 2);

        HBox tables = new HBox(8);
        tables.getChildren().addAll(tableOfDependencies);
        tables.setPadding(new Insets(15,15,15,15));
        tables.setMaxWidth(950);

        VBox container = new VBox(8);
        confirmButtons.setAlignment(Pos.CENTER);
        tables.setAlignment(Pos.CENTER);
        container.getChildren().addAll(gridpane, tables, confirmButtons);
        container.setPadding(new Insets(15,15,15,15));

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sp.setContent(container);
        scene = new Scene(sp);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        //window.showAndWait();

    }

}
