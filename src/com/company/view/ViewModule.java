package com.company.view;

import com.company.Dashboard;
import com.company.model.Deadline;
import com.company.model.Milestone;
import com.company.model.Task;
import com.company.model.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ViewModule {

    private String title;
    private String code;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Milestone> milestones = new ArrayList<>();
    private ArrayList<Deadline> deadlines = new ArrayList<>();
    private static Scene scene;

    public static Scene getScene(Module module, Stage window)  {

        window.setMinWidth(750);
        window.setMinHeight(400);
        // Basic setup
        window.setTitle(module.getTitle() + " : " + module.getCode());
        VBox container = new VBox();
        container.setSpacing(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20, 20, 20, 20));
        Button back = new Button("Return");

        // List views for all of the content...
        ObservableList<Milestone> milestonesList = FXCollections.observableArrayList();
        milestonesList.addAll(module.getMilestones());
        javafx.scene.control.ListView<Milestone> listOfMilestones = new javafx.scene.control.ListView<>(milestonesList);
        listOfMilestones.setMaxWidth(window.getWidth() / 2);
        listOfMilestones.setCellFactory(param -> new ListCell<Milestone>() {
            @Override
            protected void updateItem(Milestone milestone, boolean empty) {
                super.updateItem(milestone, empty);
                if(empty || milestone == null) {
                    setText(null);
                } else {
                    setText(milestone.getTitle());
                }
            }
        });
        //...
        ObservableList<Task> taskList = FXCollections.observableArrayList();
        taskList.addAll(module.getTasks());
        javafx.scene.control.ListView<Task> listOfTasks = new javafx.scene.control.ListView<>(taskList);
        listOfTasks.setMaxWidth(window.getWidth() / 2);
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
        //...
        ObservableList<Deadline> deadlineList = FXCollections.observableArrayList();
        deadlineList.addAll(module.getDeadlines());
        ListView<Deadline> listOfDeadline = new ListView<>(deadlineList);
        listOfDeadline.setMaxWidth(window.getWidth() / 2);
        listOfDeadline.setCellFactory(param -> new ListCell<Deadline>() {
            @Override
            protected void updateItem(Deadline deadline, boolean empty) {
                super.updateItem(deadline, empty);
                if(empty || deadline == null) {
                    setText(null);
                } else {
                    setText(deadline.getTitle());
                }
            }
        });
        // return to dashboard
        back.setOnAction(e -> {
            window.setScene(Dashboard.getScene());
        });
        container.getChildren().addAll(listOfMilestones, listOfDeadline, listOfTasks, back);
        scene = new Scene(container);
        window.setScene(scene);
        window.show();


        return scene;
    }
}
