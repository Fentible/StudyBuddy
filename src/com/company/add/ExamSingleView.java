package com.company.add;

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

public class ExamSingleView {

    static Exam exam;

    /*
     * See 'AssignmentSingleView' for notes as it is similar
     */
    public static Exam DisplayExams(SemesterProfile semesterProfile) {
        exam = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Exam");
        window.setMinWidth(750);
        window.setMinHeight(400);
        ObservableList<Exam> examList = FXCollections.observableArrayList();

        examList.addAll(ModuleSingleView.module.getExams());
        ListView<Exam> listOfExam = new ListView<>(examList);
        listOfExam.setCellFactory(param -> new ListCell<Exam>() {
            @Override
            protected void updateItem(Exam exam, boolean empty) {
                super.updateItem(exam, empty);
                if(empty || exam == null) {
                    setText(null);
                } else {
                    setText(exam.getTitle());
                }
            }
        });
        if(exam != null) {
            listOfExam.getSelectionModel().select(exam);
        }
        listOfExam.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            exam = listOfExam.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfExam, confirm);
        window.setScene(new Scene(vbox));
        window.showAndWait();

        return exam;

    }

}
