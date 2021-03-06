package com.company.add;

import com.company.model.Deadline;
import com.company.model.SemesterProfile;
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

public class DeadlineSingleView {

    static Deadline deadline;

    /*
     * See 'AssignmentSingleView' for notes as it is similar
     */
    public static Deadline DisplayDeadlines(SemesterProfile semesterProfile) {
        deadline = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Deadline");
        window.setMinWidth(750);
        window.setMinHeight(400);
        ObservableList<Deadline> deadlineList = FXCollections.observableArrayList();

        deadlineList.addAll(semesterProfile.getDeadlines());
        ListView<Deadline> listOfDeadline = new ListView<>(deadlineList);
        listOfDeadline.setCellFactory(param -> new ListCell<Deadline>() {
            @Override
            protected void updateItem(Deadline deadline, boolean empty) {
                super.updateItem(deadline, empty);
                if(empty || deadline == null) {
                    setText(null);
                } else {
                    setText(deadline.getTitle());
                }
            }
        });
        listOfDeadline.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(deadline != null) {
            Platform.runLater(() -> {
                listOfDeadline.requestFocus();
                listOfDeadline.getSelectionModel().select(deadline);
            });
        }

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            deadline = listOfDeadline.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfDeadline, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return deadline;

    }

}
