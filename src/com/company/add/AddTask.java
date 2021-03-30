package com.company.add;

import com.company.AlertBox;
import com.company.model.Module;
import com.company.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddTask  {

    private static Task task;
    private static Exam exam;
    private static ArrayList<Milestone> milestonesList;
    private static Module module;
    private static ArrayList<Task> dependenciesList;
    private static Assignment assignment;
    private static int displayProgress;
    static boolean save;

    public static boolean Display(SemesterProfile semesterProfile)  {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Task");
        window.setMinWidth(750);
        window.setMinHeight(400);


        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField title = new TextField();
        title.setPromptText("Title: ");
        DatePicker inputStartDate = new DatePicker(LocalDate.now());
        DatePicker inputEndDate = new DatePicker(LocalDate.now());
        Slider progressSlider = new Slider(0, 100, 0);
        TextArea notes = new TextArea();
        notes.setPromptText("Notes: ");
        TextField startTime = new TextField();
        startTime.setPromptText("HH:MM");
        TextField endTime = new TextField();
        endTime.setPromptText("HH:MM");

        progressSlider.setValue(0);

        Label topLabel = new Label("Select items to add to the task");
        Button examButton = new Button("Add exam");
        Button assignmentsButton = new Button("Add Assignment");
        Button modulesButton = new Button("Assign module");
        Button dependenciesButton = new Button("Add dependencies");
        Button milestonesButton = new Button("Add milestones");

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(examButton, assignmentsButton,
                modulesButton, dependenciesButton, milestonesButton);
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, saveButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        HBox startTimeBox = new HBox(8);
        HBox endTimeBox = new HBox(8);
        startTimeBox.getChildren().addAll(inputStartDate, startTime);
        endTimeBox.getChildren().addAll(inputEndDate, endTime);
        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(topLabel, 1, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Start Date: "), 0, 2);
        gridpane.add(startTimeBox, 1, 2);
        gridpane.add(new Label("End Date: "), 0, 3);
        gridpane.add(endTimeBox, 1, 3);
        Label progressLabel = new Label("Progress: " + displayProgress);
        gridpane.add(progressLabel, 0 ,4);
        gridpane.add(progressSlider, 1, 4);
        gridpane.add(notes, 0, 5, 2, 2);
        gridpane.add(bottomButtons, 0, 8, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 11);


        dependenciesButton.setOnAction(e -> {
            dependenciesList = TaskListView.DisplayTasks(semesterProfile);
        });
        milestonesButton.setOnAction(e -> {
            milestonesList = MilestoneListView.DisplayMilestones(semesterProfile);
        });
        modulesButton.setOnAction(e -> {
            module = ModuleSingleView.DisplayModules(semesterProfile);
        });
        examButton.setOnAction(e -> {
            if(ModuleSingleView.module == null) {
                AlertBox.Display("Error", "Please select a module first");
            } else {
                exam = ExamSingleView.DisplayExams(semesterProfile);
            }
        });
        assignmentsButton.setOnAction(e -> {
            if(ModuleSingleView.module == null) {
                AlertBox.Display("Error", "Please select a module first");
            } else {
            assignment = AssignmentSingleView.DisplayAssignments(semesterProfile);
            }
        });
        progressSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                progressLabel.textProperty().setValue(String.valueOf("Progress: " + (int)progressSlider.getValue()));
            }
        });

        /* Basic field checking for testing purposes, will improve later */
        saveButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            if(title.getText().trim().isEmpty() || exam == null || assignment == null || module == null) {
                System.out.println("Some required elements are empty");
            } else {
                task = new Task(title.getText(), formatter.format(inputStartDate.getValue()) + " " + startTime.getText(),
                        formatter.format(inputEndDate.getValue()) + " " + endTime.getText(),
                        (int) progressSlider.getValue(), notes.getText(), exam, assignment,
                        module, dependenciesList, milestonesList);
                semesterProfile.addTask(task);
            }
            save = true;
            window.close();
        });

        cancelButton.setOnAction(e -> {
            save = false;
            window.close();
        });

        window.setScene(new Scene(gridpane));
        window.showAndWait();

        return save;
    }
}
