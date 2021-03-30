package com.company.add;

import com.company.model.SemesterProfile;
import com.company.model.Milestone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MilestoneListView {

    static ArrayList<Milestone> milestones;

    /*
     * See 'AssignmentSingleView' for notes as it is similar
     */
    public static ArrayList<Milestone> DisplayMilestones(SemesterProfile semesterProfile) {
        milestones = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Task");
        window.setMinWidth(750);
        window.setMinHeight(400);
        ObservableList<Milestone> milestonesList = FXCollections.observableArrayList();

        milestonesList.addAll(semesterProfile.getMilestones());
        javafx.scene.control.ListView<Milestone> listOfMilestones = new javafx.scene.control.ListView<>(milestonesList);
        listOfMilestones.setCellFactory(param -> new ListCell<Milestone>() {
            @Override
            protected void updateItem(Milestone milestone, boolean empty) {
                super.updateItem(milestone, empty);
                if(empty || milestone == null) {
                    setText(null);
                } else {
                    setText(milestone.getTitle());
                }
            }
        });
        if(milestones != null) {
            for(Milestone milestone : milestones) {
                listOfMilestones.getSelectionModel().select(milestone);
            }
        }
        listOfMilestones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            milestones = new ArrayList<Milestone>(listOfMilestones.getSelectionModel().getSelectedItems());
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfMilestones, confirm);
        window.setScene(new Scene(vbox));
        window.showAndWait();

        return milestones;

    }

}
