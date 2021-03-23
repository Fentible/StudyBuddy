package com.company.add;

import com.company.model.Assignment;
import com.company.model.Exam;
import com.company.model.Module;
import com.company.model.SemesterProfile;
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

public class AssignmentSingleView {

    static Assignment assignment;

    public static Assignment DisplayAssignments(SemesterProfile semesterProfile) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Exam");
        window.setMinWidth(750);
        window.setMinHeight(400);
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
        listOfAssignment.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            assignment = listOfAssignment.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfAssignment, confirm);
        window.setScene(new Scene(vbox));
        window.showAndWait();

        return assignment;

    }

}
