package com.company.edit;

import com.company.add.*;
import com.company.model.Module;
import com.company.model.*;
import com.company.view.ViewActivity;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditActivity {

    private static Activity activity;
    private static ActivityType type;
    private static int contribution;
    private static int timeSpent;
    private static String notes;
    private static String title;
    private static LocalDateTime end;
    private static ArrayList<Task> relatedTasks;
    static boolean save;
    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static boolean Display(SemesterProfile semesterProfile, Activity passedActivity, Stage window)  {

        //window.initModality(Modality.APPLICATION_MODAL);
        activity = passedActivity;
        window.setTitle("Edit Activity");
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

        // Buttons, labels, fileds, etc.
        Label errorMessage = new Label();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField title = new TextField();
        title.setText(passedActivity.getTitle());
        DatePicker inputEndDate = new DatePicker(passedActivity.getEnd().toLocalDate());
        TextArea notes = new TextArea();
        notes.setText(passedActivity.getNotes());
        TextField endTime = new TextField();
        endTime.setText(passedActivity.getEnd().toLocalTime().toString());
        ComboBox<ActivityType> activityTypeComboBox = new ComboBox<>();
        activityTypeComboBox.getItems().setAll(ActivityType.values());
        activityTypeComboBox.getSelectionModel().select(passedActivity.getType());

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
            relatedTasks = TaskListView.DisplayTasks(semesterProfile);
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
            if(title.getText().trim().isEmpty() || activityTypeComboBox.getValue() == null || relatedTasks == null || relatedTasks.isEmpty()) {
                errorMessage.setText("Some required elements are empty");
            } else {
                Activity temp = new Activity(activityTypeComboBox.getValue(), (int)contributionSlider.getValue(), (int)timeSpentSlider.getValue(), notes.getText(), title.getText(), formatter.format(inputEndDate.getValue()) + " " + endTime.getText(),
                        relatedTasks);
                activity.updateActivity(temp);
                for(Task task : relatedTasks) {
                    task.addActivity(activity);
                }
                save = true;
                ViewActivity.Display(semesterProfile, activity, window);
                window.setScene(ViewActivity.getScene());
            }

        });

        cancelButton.setOnAction(e -> {
            save = false;
            ViewActivity.Display(semesterProfile, activity, window);
            window.setScene(ViewActivity.getScene());
        });
        scene = new Scene(gridpane);

        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());

        window.setScene(scene);

        return save;
    }
}
