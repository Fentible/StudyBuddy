package com.company.view;

import com.company.edit.EditTask;
import com.company.model.Activity;
import com.company.model.Milestone;
import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;


public class ViewTask {

    private static Scene scene;

    public static Scene getScene() { return scene; }
    public static void updateTask(Task task) {

    }

    public static void Display(SemesterProfile semesterProfile, Task task) {


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + task.getTitle());
        window.setMinWidth(750);
        window.setMinHeight(400);

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

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getDependencies()).ifPresent(taskList::addAll);
        javafx.scene.control.ListView<Task> listOfTasks = new javafx.scene.control.ListView<>(taskList);
        listOfTasks.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTitle());
                }
            }
        });

        ObservableList<Activity> activityList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getActivities()).ifPresent(activityList::addAll);
        javafx.scene.control.ListView<Activity> listOfActivities = new javafx.scene.control.ListView<>(activityList);
        listOfActivities.setCellFactory(param -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    setText(activity.getTitle());
                }
            }
        });

        ObservableList<Milestone> milestoneList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getMilestones()).ifPresent(milestoneList::addAll);
        javafx.scene.control.ListView<Milestone> listOfMilestones = new javafx.scene.control.ListView<>(milestoneList);
        listOfMilestones.setCellFactory(param -> new ListCell<Milestone>() {
            @Override
            protected void updateItem(Milestone milestone, boolean empty) {
                super.updateItem(milestone, empty);
                if (empty || milestone == null) {
                    setText(null);
                } else {
                    setText(milestone.getTitle());
                }
            }
        });

        HBox timeBox = new HBox(8);
        timeBox.getChildren().addAll(startDate, endDate);
        GridPane gridpane = new GridPane();
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
        gridpane.add(notes, 0, 5, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 15);

        HBox hbox = new HBox(8);
        hbox.getChildren().addAll(listOfTasks, listOfActivities, listOfMilestones);
        gridpane.add(hbox, 0, 11, 2, 2);
        ScrollPane container = new ScrollPane();
        container.setContent(gridpane);
        scene = new Scene(container);
        window.setScene(scene);
        window.showAndWait();

    }

    public static void Display(SemesterProfile semesterProfile, Task task, Stage window) {


        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + task.getTitle());
        window.setMinWidth(750);
        window.setMinHeight(400);

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

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getDependencies()).ifPresent(taskList::addAll);
        javafx.scene.control.ListView<Task> listOfTasks = new javafx.scene.control.ListView<>(taskList);
        listOfTasks.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTitle());
                }
            }
        });

        ObservableList<Activity> activityList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getActivities()).ifPresent(activityList::addAll);
        javafx.scene.control.ListView<Activity> listOfActivities = new javafx.scene.control.ListView<>(activityList);
        listOfActivities.setCellFactory(param -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    setText(activity.getTitle());
                }
            }
        });

        ObservableList<Milestone> milestoneList = FXCollections.observableArrayList();
        Optional.ofNullable(task.getMilestones()).ifPresent(milestoneList::addAll);
        javafx.scene.control.ListView<Milestone> listOfMilestones = new javafx.scene.control.ListView<>(milestoneList);
        listOfMilestones.setCellFactory(param -> new ListCell<Milestone>() {
            @Override
            protected void updateItem(Milestone milestone, boolean empty) {
                super.updateItem(milestone, empty);
                if (empty || milestone == null) {
                    setText(null);
                } else {
                    setText(milestone.getTitle());
                }
            }
        });

        HBox timeBox = new HBox(8);
        timeBox.getChildren().addAll(startDate, endDate);
        GridPane gridpane = new GridPane();
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
        gridpane.add(notes, 0, 5, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 15);

        HBox hbox = new HBox(8);
        hbox.getChildren().addAll(listOfTasks, listOfActivities, listOfMilestones);
        gridpane.add(hbox, 0, 11, 2, 2);
        ScrollPane container = new ScrollPane();
        container.setContent(gridpane);
        scene = new Scene(container);
        window.setScene(scene);
        //window.showAndWait();

    }

}
