package com.company.add;

import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TaskListView {

    static ArrayList<Task> tasks;

    /*
     * See 'AssignmentSingleView' for notes as it is similar
     */
    public static ArrayList<Task> DisplayTasks(SemesterProfile semesterProfile) {
        tasks = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Tasks");
        window.setMinWidth(750);
        window.setMinHeight(400);

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        taskList.addAll(semesterProfile.getTasks());
        javafx.scene.control.ListView<Task> listOfTasks = new javafx.scene.control.ListView<>(taskList);
        listOfTasks.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if(empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTitle());
                }
            }
        });
        if(tasks != null) {
            for(Task task : tasks) {
                listOfTasks.getSelectionModel().select(task);
            }
        }
        listOfTasks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            tasks = new ArrayList<Task>(listOfTasks.getSelectionModel().getSelectedItems());
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfTasks, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return tasks;

    }

    public static ArrayList<Task> DisplayTasks(SemesterProfile semesterProfile, Task passedTask) {
        tasks = passedTask.getDependencies();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Tasks");
        window.setMinWidth(750);
        window.setMinHeight(400);

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        taskList.addAll(semesterProfile.getTasks());

        javafx.scene.control.ListView<Task> listOfTasks = new javafx.scene.control.ListView<>(taskList);
        listOfTasks.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if(empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTitle());
                }
            }
        });
        listOfTasks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if(passedTask.getDependencies() != null && !passedTask.getDependencies().isEmpty()) {
            Platform.runLater(() -> {
                listOfTasks.requestFocus();
                for (Task task : passedTask.getDependencies()) {
                    listOfTasks.getSelectionModel().select(task);
                }
            });
        }


        if(tasks != null) {
            for(Task task : tasks) {
                listOfTasks.getSelectionModel().select(task);
            }
        }

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            tasks = new ArrayList<Task>(listOfTasks.getSelectionModel().getSelectedItems());
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfTasks, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return tasks;

    }



}
