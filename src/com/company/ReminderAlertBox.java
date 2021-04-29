package com.company;

import com.company.model.*;
import com.company.view.ViewActivity;
import com.company.view.ViewDeadline;
import com.company.view.ViewMilestone;
import com.company.view.ViewTask;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReminderAlertBox {

    public static void Display(Reminder reminder, SemesterProfile semesterProfile) {

        Stage window = new Stage();
        CalenderModelClass model = reminder.getRelatedEvent();
        // Block input to other window until this popup is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Reminder for: " + reminder.getRelatedEvent().getTitle());
        window.setMinWidth(250);
        window.setMinHeight(250);
        Label label = new Label("Event " + reminder.getRelatedEvent().getTitle() +
                " is starting at: " + reminder.getRelatedEvent().getEnd().toLocalTime().toString());
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> window.close());
        Button viewButton = new Button("View");
        HBox buttons = new HBox(8);
        viewButton.setOnAction(e -> {
            if(model instanceof Task) { ViewTask.Display(semesterProfile, (Task) model); }
            else if(model instanceof Milestone) { ViewMilestone.Display(semesterProfile, (Milestone) model); }
            else if(model instanceof Deadline) { ViewDeadline.Display(semesterProfile, (Deadline) model); }
            else if(model instanceof Activity) { ViewActivity.Display(semesterProfile, (Activity) model); }
        });
        buttons.getChildren().addAll(closeButton, viewButton);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

    }
}
