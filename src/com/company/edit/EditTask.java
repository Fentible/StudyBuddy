package com.company.edit;

import com.company.add.*;
import com.company.model.*;
import com.company.model.Module;
import com.company.view.ViewTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditTask {

    private static Task task;
    private static Exam exam;
    private static ArrayList<Milestone> milestonesList;
    private static Module module;
    private static ArrayList<Task> dependenciesList;
    private static Assignment assignment;
    private static int displayProgress;
    static boolean save;
    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static boolean Display(SemesterProfile semesterProfile, Task passedTask, Stage window)  {

        task = passedTask;
        exam = passedTask.getExam();
        milestonesList = passedTask.getMilestones();
        module = passedTask.getModule();
        dependenciesList = passedTask.getDependencies();
        displayProgress = passedTask.getProgress();


        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Task");
        //window.setMinWidth(750);
        //window.setMinHeight(500);
        //window.setMaxWidth(750);
        //window.setMaxHeight(500);
        //window.setWidth(750);
        //window.setHeight(500);
        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(
                new KeyFrame(new Duration(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        window.setWidth(window.getWidth() - 5);
                        window.setHeight(window.getHeight() - 5);
                        if(window.getWidth() <= 700 && window.getHeight() <= 650) {
                            tl.stop();
                        }
                    }
                })
        );
        tl.play();

        // switching from edit and view windows messed with the sizing a bit
        // so that's why theres so many width and height setting
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField title = new TextField();
        title.setText(task.getTitle());
        DatePicker inputStartDate = new DatePicker(task.getStart().toLocalDate());
        DatePicker inputEndDate = new DatePicker(task.getEnd().toLocalDate());
        Slider progressSlider = new Slider(0, 100, 0);
        TextArea notes = new TextArea();
        notes.setText(task.getNotes());
        TextField startTime = new TextField();
        startTime.setText(task.getStart().toLocalTime().toString());
        TextField endTime = new TextField();
        endTime.setText(task.getStart().toLocalTime().toString());

        progressSlider.setValue(task.getProgress());

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
            dependenciesList = TaskListView.DisplayTasks(semesterProfile, task);
        });
        milestonesButton.setOnAction(e -> {
            milestonesList = MilestoneListView.DisplayMilestones(semesterProfile, task);
        });
        modulesButton.setOnAction(e -> {
            module = ModuleSingleView.DisplayModules(semesterProfile, task);
        });
        examButton.setOnAction(e -> {
                exam = ExamSingleView.DisplayExams(semesterProfile, task);
        });
        assignmentsButton.setOnAction(e -> {
                assignment = AssignmentSingleView.DisplayAssignments(semesterProfile, task);
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
            Task temp = new Task(title.getText(), formatter.format(inputStartDate.getValue()) + " " + startTime.getText(),
                        formatter.format(inputEndDate.getValue()) + " " + endTime.getText(),
                        (int) progressSlider.getValue(), notes.getText(), exam, assignment,
                        module, dependenciesList, milestonesList);
            task.updateTask(temp);
            save = true;
            ViewTask.Display(semesterProfile, task, window);
            window.setScene(ViewTask.getScene());
        });

        cancelButton.setOnAction(e -> {
            save = false;
            ViewTask.Display(semesterProfile, task, window);
            window.setScene(ViewTask.getScene());
        });
        scene = new Scene(gridpane);
        window.setScene(scene);

        return save;
    }
}
