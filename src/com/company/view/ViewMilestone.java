package com.company.view;

import com.company.Reminder;
import com.company.edit.EditMilestone;
import com.company.model.Milestone;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("ALL")
public class ViewMilestone {

    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static void Display(SemesterProfile semesterProfile, Milestone milestone) {


        Stage window = new Stage();
        milestone.updateCompletion();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + milestone.getTitle());
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
            if(milestone.getEnd().isAfter(LocalDateTime.now())) {
                semesterProfile.addReminder(new Reminder(milestone));
            } else {
                Alert reminderAlert = new Alert(Alert.AlertType.ERROR, "A reminder cannot be set in the past");
                reminderAlert.showAndWait();
            }
        });

        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton, addReminder);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
            EditMilestone.Display(semesterProfile, milestone, window);
        });

        Label title = new Label(milestone.getTitle());

        TextField endDate = new TextField();
        endDate.setText(milestone.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(milestone.getCompletion());
        progress.setDisable(true);

        TextField deadline = new TextField();
        deadline.setText(milestone.getEvent().getTitle());
        deadline.setEditable(false);

        Button examButton = new Button();
        examButton.setText(milestone.getEvent().getTitle());
        examButton.setOnAction(e -> {
            ViewDeadline.Display(semesterProfile, milestone.getEvent());
        });


        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(milestone.getRequiredTasks()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setPrefWidth(900);
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
        timeBox.getChildren().addAll(endDate);

        GridPane gridpane = new GridPane(); // GridPane for most of the elements
        gridpane.prefWidthProperty().bind(window.widthProperty());
        boolean missed = false; //bool for if missed
        LocalDateTime now = LocalDateTime.now(); // current date/time
        if(milestone.getEnd().isBefore(now) && milestone.getCompletion() < 100){
            missed = true;
        }

        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(new Label("View Milestone: "), 0, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Date range: "), 0, 2);
        gridpane.add(timeBox, 1, 2);
        Label progressLabel = new Label("Progress: " + progress.getValue());
        gridpane.add(progressLabel, 0 ,3);
        gridpane.add(progress, 1, 3);
        gridpane.add(new Label("Missed: " + missed), 0, 4);
        gridpane.add(examButton, 1, 5);

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

    public static void Display(SemesterProfile semesterProfile, Milestone milestone, Stage window) {

        //window.initModality(Modality.APPLICATION_MODAL);
        milestone.updateCompletion();
        window.setTitle("View: " + milestone.getTitle());
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
            if(milestone.getEnd().isAfter(LocalDateTime.now())) {
                semesterProfile.addReminder(new Reminder(milestone));
            } else {
                Alert reminderAlert = new Alert(Alert.AlertType.ERROR, "A reminder cannot be set in the past");
                reminderAlert.showAndWait();
            }
        });

        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton, addReminder);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);



        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
            EditMilestone.Display(semesterProfile, milestone, window);
        });

        Label title = new Label(milestone.getTitle());


        TextField endDate = new TextField();
        endDate.setText(milestone.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(milestone.getCompletion());
        progress.setDisable(true);

        TextField deadline = new TextField();
        deadline.setText(milestone.getEvent().getTitle());
        deadline.setEditable(false);


        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(milestone.getRequiredTasks()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setPrefWidth(900);
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
        timeBox.getChildren().addAll(endDate);

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
