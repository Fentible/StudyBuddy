package com.company.edit;

import com.company.add.*;
import com.company.model.Module;
import com.company.model.*;
import com.company.view.ViewMilestone;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditMilestone {

    private static Milestone milestone;
    private static ArrayList<Task> requiredTasks = new ArrayList<>();
    private static Deadline event; // what the milestone is working towards
    private static String title;
    private static LocalDateTime end;
    private static boolean save;
    private static Scene scene;

    public static Scene getScene() { return scene; }

    public static boolean Display(SemesterProfile semesterProfile, Milestone passedMilestone, Stage window)  {

        milestone = passedMilestone;
        event = milestone.getEvent();
        requiredTasks = milestone.getRequiredTasks();
        title = milestone.getTitle();
        end = milestone.getEnd();



        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Milestone");
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
        TextField textField = new TextField();
        textField.setText(title);

        DatePicker inputEndDate = new DatePicker(end.toLocalDate());

        TextField endTime = new TextField();
        endTime.setText(end.toLocalTime().toString());

        Label topLabel = new Label("Select items to add to the task");
        Button taskButton = new Button("Add task(s)");
        Button eventButton = new Button("Add Deadline");

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(taskButton, eventButton);
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, saveButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);


        HBox endTimeBox = new HBox(8);
        endTimeBox.getChildren().addAll(inputEndDate, endTime);
        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPadding(new Insets(15,15,15,15));
        gridpane.setHgap(25);
        gridpane.setVgap(10);
        gridpane.add(topLabel, 1, 0);
        gridpane.add(new Label("Title: "), 0, 1);
        gridpane.add(textField, 1, 1);

        gridpane.add(new Label("Date: "), 0, 2);
        gridpane.add(endTimeBox, 1, 2);

        gridpane.add(bottomButtons, 0, 3, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 5);


        taskButton.setOnAction(e -> {
            requiredTasks = TaskListView.DisplayTasks(semesterProfile);
        });

        eventButton.setOnAction(e -> {
            event = DeadlineSingleView.DisplayDeadlines(semesterProfile);
        });


        /* Basic field checking for testing purposes, will improve later */
        saveButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Milestone temp = new Milestone(requiredTasks, event, textField.getText(),
                        formatter.format(inputEndDate.getValue()) + " " + endTime.getText());
            milestone.updateMilestone(temp);

            save = true;
            ViewMilestone.Display(semesterProfile, milestone, window);
            window.setScene(ViewMilestone.getScene());
        });

        cancelButton.setOnAction(e -> {
            save = false;
            ViewMilestone.Display(semesterProfile, milestone, window);
            window.setScene(ViewMilestone.getScene());
        });
        scene = new Scene(gridpane);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);

        return save;
    }
}
