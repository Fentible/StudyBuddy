package com.company.add;

import com.company.model.*;
import com.company.model.Module;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddTask  {

    static Task task;
    static Exam exam;
    static ArrayList<Milestone> milestonesList;
    static Module module;
    static ArrayList<Task> dependenciesList;
    static Assignment assignment;
    static int displayProgress;



    public static void Display(SemesterProfile semesterProfile)  {

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

        progressSlider.setValue(0);

        Label topLabel = new Label("Select items to add task");
        Button examButton = new Button("Add exam");
        Button assignmentsButton = new Button("Add Assignment");
        Button modulesButton = new Button("Assign module");
        Button dependenciesButton = new Button("Add dependencies");
        Button milestonesButton = new Button("Add milestones");

        /* Layouts for testing */
        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(examButton, assignmentsButton,
                modulesButton, dependenciesButton, milestonesButton);
        HBox confirmButtons = new HBox(10);
        //confirmButtons.setAlignment(Pos.CENTER);
        confirmButtons.getChildren().addAll(cancelButton, saveButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);
        /*HBox selectableElements = new HBox(10);
        selectableElements.getChildren().addAll(topLabel, title, inputStartDate, inputEndDate, progressSlider,
                notes, examButton, assignmentsButton, modulesButton, dependenciesButton, milestonesButton);
        selectableElements.setAlignment(Pos.CENTER);*/

        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(topLabel, 0, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Start Date: "), 0, 2);
        gridpane.add(inputStartDate, 1, 2);
        gridpane.add(new Label("End Date: "), 0, 3);
        gridpane.add(inputEndDate, 1, 3);
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
            exam = ExamSingleView.DisplayExams(semesterProfile);
        });
        assignmentsButton.setOnAction(e -> {
            assignment = AssignmentSingleView.DisplayAssignments(semesterProfile);
        });
        progressSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                progressLabel.textProperty().setValue(String.valueOf("Progress: " + (int)progressSlider.getValue()));
            }
        });

        /* Basic field checking for testing purposes, will improve later */
        saveButton.setOnAction(e -> {
            if(title.getText().trim().isEmpty() || exam == null || assignment == null || module == null) {
                System.out.println("Some required elements are empty");
            } else {
                task = new Task(title.getText(), inputStartDate.getValue().toString(), inputEndDate.getValue().toString(),
                        (int) progressSlider.getValue(), notes.getText(), exam, assignment,
                        module, dependenciesList, milestonesList);
                semesterProfile.addTask(task);
            }
        });

        cancelButton.setOnAction(e -> window.close());


        window.setScene(new Scene(gridpane));
        window.showAndWait();

        return;
    }
}
