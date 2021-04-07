package com.company.add;

import com.company.model.Activity;
import com.company.model.ActivityType;
import com.company.model.SemesterProfile;
import com.company.model.Task;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddActivity {


    static ArrayList<Task> tasks;
    static Activity activity;
    static boolean save;

    /*
     * Function to display window to user when adding an activity
     */
    public static boolean Display(SemesterProfile semesterProfile, LocalDate date)  {
        // General window setup
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Task");
        window.setMinWidth(750);
        window.setMinHeight(400);

        // Buttons, labels, fileds, etc.
        Label errorMessage = new Label();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField title = new TextField();
        title.setPromptText("Title: ");
        DatePicker inputEndDate = new DatePicker(date == null ? LocalDate.now() : date);
        TextArea notes = new TextArea();
        notes.setPromptText("Notes: ");
        TextField endTime = new TextField();
        endTime.setPromptText("HH:MM");
        ComboBox<ActivityType> activityTypeComboBox = new ComboBox<>();
        activityTypeComboBox.getItems().setAll(ActivityType.values());

        Slider timeSpentSlider = new Slider(0, 100, 0);
        timeSpentSlider.setValue(0);
        Slider contributionSlider = new Slider(0, 100, 0);
        contributionSlider.setValue(0);

        Label topLabel = new Label("Select items to add to the task");
        Button tasksButton = new Button("Assign to task(s)");

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(tasksButton);
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(errorMessage, cancelButton, saveButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        HBox endTimeBox = new HBox(8);
        endTimeBox.getChildren().addAll(inputEndDate, endTime);

        // All previous boxes and elements will be setup onto the GridPane
        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(topLabel, 1, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("Date: "), 0, 2);
        gridpane.add(endTimeBox, 1, 2);

        /*
         * Multiple tasks can be linked but currently do have an individual contribution slider
         */
        Label progressLabel = new Label("Progress: " + timeSpentSlider.getValue());
        gridpane.add(progressLabel, 0 ,4);
        gridpane.add(timeSpentSlider, 1, 4);

        Label contributionLabel = new Label("Total contribution: " + contributionSlider.getValue());
        gridpane.add(contributionLabel, 0 ,5);
        gridpane.add(contributionSlider, 1, 5);

        gridpane.add(notes, 0, 6, 2, 2);
        gridpane.add(activityTypeComboBox, 0, 9);
        gridpane.add(bottomButtons, 0, 11, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 13);

        tasksButton.setOnAction(e -> {
            tasks = TaskListView.DisplayTasks(semesterProfile);
        });
        // Updates sliders...
        contributionSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                contributionLabel.textProperty().setValue(String.valueOf("Total Contribution: " + (int)contributionSlider.getValue()));
            }
        });
        // ...
        timeSpentSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                progressLabel.textProperty().setValue(String.valueOf("Progress: " + (int)timeSpentSlider.getValue()));
            }
        });

        saveButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // Basic error checking, **need to add a label for an error box**
            if(title.getText().trim().isEmpty() || activityTypeComboBox.getValue() == null || tasks == null || tasks.isEmpty()) {
                errorMessage.setText("Some required elements are empty");
            } else {
                 activity = new Activity(activityTypeComboBox.getValue(), (int)contributionSlider.getValue(), (int)timeSpentSlider.getValue(), notes.getText(), title.getText(), formatter.format(inputEndDate.getValue()) + " " + endTime.getText(),
                        tasks);
                for(Task task : tasks) {
                    task.addActivity(activity);
                }
                semesterProfile.addActivity(activity);
                save = true;
                window.close();
            }

        });

        cancelButton.setOnAction(e -> {
            save = false;
            window.close();
        });

        window.setScene(new Scene(gridpane));
        window.showAndWait();

        return false;
    }
}
