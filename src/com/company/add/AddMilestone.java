package com.company.add;

import com.company.model.Deadline;
import com.company.model.Milestone;
import com.company.model.SemesterProfile;
import com.company.model.Task;
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

public class AddMilestone {

    private static ArrayList<Task> requiredTasks = new ArrayList<>();
    private static Deadline event; // what the milestone is working towards
    private static String title;
    private static LocalDateTime end;
    private static Milestone milestone;
    static boolean save;

    /*
     * See 'AddActivity' for notes as this class is very similar
     */
    public static boolean Display(SemesterProfile semesterProfile, LocalDate date) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Milestone");
        window.setMinWidth(750);
        window.setMinHeight(400);

        Label errorMessage = new Label();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField title = new TextField();
        title.setPromptText("Title: ");

        DatePicker inputEndDate = new DatePicker(date == null ? LocalDate.now() : date);
        TextField endTime = new TextField();
        endTime.setPromptText("HH:MM");

        Label topLabel = new Label("Select items to add to the milestone");
        Button deadlineButton = new Button("Assign deadline");
        Button tasksButton = new Button("Assign task(s)");


        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(deadlineButton,
                tasksButton);
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(errorMessage, cancelButton, saveButton);
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
        gridpane.add(title, 1, 1);
        gridpane.add(new Label("End Date: "), 0, 2);
        gridpane.add(endTimeBox, 1, 2);
        gridpane.add(bottomButtons, 0, 3, 2, 2);
        GridPane.setHalignment(confirmButtons, HPos.RIGHT);
        gridpane.add(confirmButtons, 1, 5);

        tasksButton.setOnAction(e -> {
            requiredTasks = TaskListView.DisplayTasks(semesterProfile);
        });
        deadlineButton.setOnAction(e -> {
            event = DeadlineSingleView.DisplayDeadlines(semesterProfile);
        });


        saveButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            if(title.getText().trim().isEmpty() || event == null || requiredTasks == null) {
                errorMessage.setText("Some required elements are empty");
            }
            else if (endTime.getText().matches("^..:..$") == false){
                errorMessage.setText("The start time must be a proper 24hour time"); //tests format for time
            }
            else {
                milestone = new Milestone(requiredTasks, event, title.getText(),
                        formatter.format(inputEndDate.getValue()) + " " + endTime.getText());
                semesterProfile.addMilestone(milestone);
                milestone.getEvent().getModule().addMilestone(milestone);
                save = true;
                window.close();
            }

        });

        cancelButton.setOnAction(e -> {
            save = false;
            window.close();
        });
        Scene scene = new Scene(gridpane);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return save;

    }
}
