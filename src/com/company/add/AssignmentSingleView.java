package com.company.add;

import com.company.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * 'Single' || 'List' is denoting the return amount
 */
public class AssignmentSingleView {

    static Assignment assignment;

    public static Assignment DisplayAssignments(SemesterProfile semesterProfile) {
        assignment = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Exam");
        window.setMinWidth(750);
        window.setMinHeight(400);

        // List view of the elements
        ObservableList<Assignment> assignmentList = FXCollections.observableArrayList();
        assignmentList.addAll(ModuleSingleView.module.getAssignments());
        ListView<Assignment> listOfAssignment = new ListView<>(assignmentList);
        listOfAssignment.setCellFactory(param -> new ListCell<Assignment>() {
            @Override
            protected void updateItem(Assignment assignment, boolean empty) {
                super.updateItem(assignment, empty);
                if(empty || assignment == null) {
                    setText(null);
                } else {
                    setText(assignment.getTitle());
                }
            }
        });

        if(assignment != null) {
            listOfAssignment.getSelectionModel().select(assignment);
        }
        // Allow single selection
        listOfAssignment.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            assignment = listOfAssignment.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfAssignment, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return assignment;

    }
    public static Assignment DisplayAssignments(SemesterProfile semesterProfile, Task passedTask) {
        assignment = passedTask.getAssignment();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Exam");
        window.setMinWidth(750);
        window.setMinHeight(400);

        // List view of the elements
        ObservableList<Assignment> assignmentList = FXCollections.observableArrayList();
        assignmentList.addAll(semesterProfile.getAssignments());
        ListView<Assignment> listOfAssignment = new ListView<>(assignmentList);
        listOfAssignment.setCellFactory(param -> new ListCell<Assignment>() {
            @Override
            protected void updateItem(Assignment assignment, boolean empty) {
                super.updateItem(assignment, empty);
                if(empty || assignment == null) {
                    setText(null);
                } else {
                    setText(assignment.getTitle());
                }
            }
        });
        listOfAssignment.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listOfAssignment.getSelectionModel().select(passedTask.getAssignment());
        if(assignment != null) {
            Platform.runLater(() -> {
                listOfAssignment.requestFocus();
                listOfAssignment.getSelectionModel().select(assignment);
            });
        }

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            assignment = listOfAssignment.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfAssignment, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return assignment;

    }


}
