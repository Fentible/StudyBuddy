package com.company.view;

import com.company.Dashboard;
import com.company.model.*;
import com.company.model.Module;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class ViewModule {

    private String title;
    private String code;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Milestone> milestones = new ArrayList<>();
    private ArrayList<Deadline> deadlines = new ArrayList<>();
    private static Scene scene;

    public static Scene getScene(Module module, Stage window, SemesterProfile semesterProfile)  {

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
        Optional.ofNullable(module.getMilestones()).ifPresent(milestonesList::addAll);
        TableView<Milestone> tableOfMilestones = new TableView<Milestone>(milestonesList);
        TableColumn<Milestone, String> milestoneNameColumn = new TableColumn<>("Name");
        TableColumn<Milestone, String> milestoneCompletionColumn = new TableColumn<>("Completed");
        TableColumn<Milestone, String> milestoneDateColumn = new TableColumn<>("Due Date");
        milestoneNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        milestoneCompletionColumn.setCellValueFactory(cellData  -> new SimpleStringProperty(Integer.toString(cellData .getValue().getCompletion())));
        milestoneDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toString()));
        tableOfMilestones.setMaxWidth(window.getWidth() / 2);
        milestoneDateColumn.prefWidthProperty().bind(tableOfMilestones.widthProperty().divide(3));
        milestoneCompletionColumn.prefWidthProperty().bind(tableOfMilestones.widthProperty().divide(3));
        milestoneNameColumn.prefWidthProperty().bind(tableOfMilestones.widthProperty().divide(3));
        tableOfMilestones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfMilestones.getColumns().addAll(milestoneNameColumn, milestoneCompletionColumn, milestoneDateColumn);
        tableOfMilestones.setRowFactory(e -> {
            TableRow<Milestone> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null) {
                        ViewMilestone.Display(semesterProfile, row.getItem());
                    }
                }
            });
            return row;
        });


        //...
        ObservableList<Task> taskList = FXCollections.observableArrayList();
        Optional.ofNullable(module.getTasks()).ifPresent(taskList::addAll);
        TableView<Task> tableOfTasks = new TableView<>(taskList);
        TableColumn<Task, String> taskNameColumn = new TableColumn<>("Name");
        TableColumn<Task, String> taskProgressColumn = new TableColumn<>("Progress");
        TableColumn<Task, String> taskDateColumn = new TableColumn<>("Date");
        taskNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        taskProgressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProgress())));
        taskDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toString()));
        taskDateColumn.prefWidthProperty().bind(tableOfTasks.widthProperty().divide(3));
        taskNameColumn.prefWidthProperty().bind(tableOfTasks.widthProperty().divide(3));
        taskProgressColumn.prefWidthProperty().bind(tableOfTasks.widthProperty().divide(3));
        tableOfTasks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfTasks.getColumns().addAll(taskNameColumn, taskProgressColumn, taskDateColumn);
        tableOfTasks.setMaxWidth(window.getWidth() / 2);
        tableOfTasks.setRowFactory(e -> {
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

        //...
        ObservableList<Deadline> deadlineList = FXCollections.observableArrayList();
        Optional.ofNullable(module.getDeadlines()).ifPresent(deadlineList::addAll);
        TableView<Deadline> tableOfDeadlines = new TableView<>(deadlineList);
        TableColumn<Deadline, String> deadlineNameColumn = new TableColumn<>("Name");
        TableColumn<Deadline, String> deadlineDateColumn = new TableColumn<>("Due Date");
        deadlineNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        deadlineDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate().toString()));
        deadlineDateColumn.prefWidthProperty().bind(tableOfDeadlines.widthProperty().divide(2));
        deadlineNameColumn.prefWidthProperty().bind(tableOfDeadlines.widthProperty().divide(2));
        tableOfDeadlines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableOfDeadlines.getColumns().addAll(deadlineNameColumn, deadlineDateColumn);
        tableOfDeadlines.setMaxWidth(window.getWidth() / 2);
        tableOfDeadlines.setRowFactory(e -> {
            TableRow<Deadline> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null) {
                        ViewDeadline.Display(semesterProfile, row.getItem());
                    }
                }
            });
            return row;
        });

        // return to dashboard
        back.setOnAction(e -> {
            window.setScene(Dashboard.getScene());
        });

        container.getChildren().addAll(new Label("List of Milestones: "), tableOfMilestones,
                new Label("List of Deadlines: "), tableOfDeadlines,
                new Label("List of Tasks: "), tableOfTasks, back);
        scene = new Scene(container);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);

        window.show();

        return scene;
    }
}
