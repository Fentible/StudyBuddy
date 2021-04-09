package com.company.view;

import com.company.edit.EditTask;
import com.company.model.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;



public class ViewTask {

    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static void Display(SemesterProfile semesterProfile, Task task) {


        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + task.getTitle());
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
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
                EditTask.Display(semesterProfile, task, window);
        });

        Label title = new Label(task.getTitle());

        TextArea notes = new TextArea();
        notes.setText(task.getNotes());
        notes.setEditable(false);

        TextField startDate = new TextField();
        startDate.setText(task.getStart().toString());
        startDate.setEditable(false);
        TextField endDate = new TextField();
        endDate.setText(task.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(task.getProgress());
        progress.setDisable(true);

        TextField deadline = new TextField();
        String str = "";
        if(task.getExam() != null) {
            str = str + task.getExam().getTitle();
        }
        if(task.getAssignment() != null) {
            str = str + ", " + task.getAssignment().getTitle();
        }
        deadline.setText(str);
        deadline.setEditable(false);

        TextField module = new TextField();
        module.setText(task.getModule() == null ? "Not found " : task.getModule().getTitle());
        module.setEditable(false);


        ObservableList<Activity> activityList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getActivities()).ifPresent(activityList::addAll);
        TableView<Activity> tableOfActivities = new TableView<>(activityList);
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        TableColumn<Activity, String> activityDateColumn = new TableColumn<>("Due Date");
        TableColumn<Activity, String> activityProgressColumn = new TableColumn<>("Completion");
        activityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        activityDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        activityProgressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getTimeSpent())));
        tableOfActivities.setPrefWidth(300);
        tableOfActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfActivities.getColumns().addAll(activityNameColumn, activityProgressColumn, activityDateColumn);


        ObservableList<Milestone> milestonesList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getMilestones()).ifPresent(milestonesList::addAll);
        TableView<Milestone> tableOfMilestones = new TableView<Milestone>(milestonesList);
        TableColumn<Milestone, String> milestoneNameColumn = new TableColumn<>("Name");
        TableColumn<Milestone, String> milestoneCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Milestone, String> milestoneDateColumn = new TableColumn<>("Due Date");
        milestoneNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        milestoneCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getCompletion())));
        milestoneDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfMilestones.setPrefWidth(300);
        tableOfMilestones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfMilestones.getColumns().addAll(milestoneNameColumn, milestoneCompletionColumn, milestoneDateColumn);


        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getDependencies()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setPrefWidth(300);
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

        HBox timeBox = new HBox(8); // dates HBox
        timeBox.getChildren().addAll(startDate, endDate);

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
        tables.getChildren().addAll(tableOfDependencies, tableOfActivities, tableOfMilestones);
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
        window.setScene(scene);
        window.show();


    }

    public static void Display(SemesterProfile semesterProfile, Task task, Stage window) {

        //window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("View: " + task.getTitle());
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
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, editButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        cancelButton.setOnAction(e -> {
            window.close();
        });

        editButton.setOnAction(e -> {
                EditTask.Display(semesterProfile, task, window);
        });

        Label title = new Label(task.getTitle());

        TextArea notes = new TextArea();
        notes.setText(task.getNotes());
        notes.setEditable(false);

        TextField startDate = new TextField();
        startDate.setText(task.getStart().toString());
        startDate.setEditable(false);
        TextField endDate = new TextField();
        endDate.setText(task.getEnd().toString());
        endDate.setEditable(false);

        Slider progress = new Slider();
        progress.setValue(task.getProgress());
        progress.setDisable(true);

        TextField deadline = new TextField();
        String str = "";
        if(task.getExam() != null) {
            str = str + task.getExam().getTitle();
        }
        if(task.getAssignment() != null) {
            str = str + ", " + task.getAssignment().getTitle();
        }
        deadline.setText(str);
        deadline.setEditable(false);

        TextField module = new TextField();
        module.setText(task.getModule().getTitle());
        module.setEditable(false);


        ObservableList<Activity> activityList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getActivities()).ifPresent(activityList::addAll);
        TableView<Activity> tableOfActivities = new TableView<>(activityList);
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        TableColumn<Activity, String> activityDateColumn = new TableColumn<>("Due Date");
        TableColumn<Activity, String> activityProgressColumn = new TableColumn<>("Completion");
        activityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        activityDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        activityProgressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getTimeSpent())));
        tableOfActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfActivities.setPrefWidth(300);
        tableOfActivities.getColumns().addAll(activityNameColumn, activityProgressColumn, activityDateColumn);


        ObservableList<Milestone> milestonesList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getMilestones()).ifPresent(milestonesList::addAll);
        TableView<Milestone> tableOfMilestones = new TableView<Milestone>(milestonesList);
        TableColumn<Milestone, String> milestoneNameColumn = new TableColumn<>("Name");
        TableColumn<Milestone, String> milestoneCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Milestone, String> milestoneDateColumn = new TableColumn<>("Due Date");
        milestoneNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        milestoneCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getCompletion())));
        milestoneDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfMilestones.setPrefWidth(300);
        tableOfMilestones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfMilestones.getColumns().addAll(milestoneNameColumn, milestoneCompletionColumn, milestoneDateColumn);


        ObservableList<Task> dependenciesList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getDependencies()).ifPresent(dependenciesList::addAll);
        TableView<Task> tableOfDependencies = new TableView<Task>(dependenciesList);
        TableColumn<Task, String> dependencyNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> dependencyCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Task, String> dependencyDateColumn = new TableColumn<>("Due Date");
        dependencyNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dependencyCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getProgress())));
        dependencyDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfDependencies.setMaxWidth(300);
        tableOfDependencies.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfDependencies.setRowFactory(e -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null)
                        ViewTask.Display(semesterProfile, row.getItem());
                }
            });
            return row;
        });
        tableOfDependencies.getColumns().addAll(dependencyNameColumn, dependencyCompletionColumn, dependencyDateColumn);



        HBox timeBox = new HBox(8); // dates HBox
        timeBox.getChildren().addAll(startDate, endDate);

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
        tables.getChildren().addAll(tableOfDependencies, tableOfActivities, tableOfMilestones);
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
        window.setScene(scene);
        //window.showAndWait();

    }

}
