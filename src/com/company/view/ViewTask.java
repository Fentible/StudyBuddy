package com.company.view;

import com.company.model.Activity;
import com.company.model.Milestone;
import com.company.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ViewTask {

    public static void DisplayTask(Task task) {


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("View: " + task.getTitle());
        window.setMinWidth(750);
        window.setMinHeight(400);

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
        deadline.setText(task.getExam().getTitle() + ", " + task.getAssignment().getTitle());
        deadline.setEditable(false);

        TextField module = new TextField();
        module.setText(task.getModule().getTitle());
        module.setEditable(false);

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        taskList.addAll(task.getDependencies());
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
        activityList.addAll(task.getActivities());
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
        milestoneList.addAll(task.getMilestones());
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
        VBox hbox = new VBox(8);
        hbox.getChildren().addAll(title, notes, startDate, endDate, deadline, module, listOfTasks, listOfActivities, listOfMilestones);
        ScrollPane container = new ScrollPane();
        container.setContent(hbox);
        Scene scene = new Scene(container);
        window.setScene(scene);
        window.showAndWait();

    }

}
