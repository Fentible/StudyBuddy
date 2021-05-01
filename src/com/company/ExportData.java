package com.company;

import com.company.model.SemesterProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ExportData {

    private static byte flag = 0x00;
    private static File file;

    public static void Display(SemesterProfile semesterProfile) {

        Stage window = new Stage();

        // Block input to other window until this popup is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Export Data");
        window.setMinWidth(350);
        window.setMinHeight(250);
        Label label = new Label("Please select which datasets you which to export");
        Label exportLocation = new Label();
        Button closeButton = new Button("Close");
        Button exportButton = new Button("Export");
        CheckBox exportTasks = new CheckBox("Tasks");
        CheckBox exportMilestones = new CheckBox("Milestones");
        CheckBox exportActivities = new CheckBox("Activities");
        CheckBox exportDeadlines = new CheckBox("Deadlines");
        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER);

        DirectoryChooser exportDirectory = new DirectoryChooser();
        File initialDir = new File("src/com/company");
        exportDirectory.setInitialDirectory(initialDir);
        Button directoryButton = new Button("Choose Directory");
        directoryButton.setOnAction(e -> {
            file = exportDirectory.showDialog(window);
            exportLocation.setText(file.getAbsolutePath());
        });
        buttons.getChildren().addAll(closeButton, directoryButton, exportButton);
        VBox exportOptions = new VBox(8);
        exportOptions.setAlignment(Pos.CENTER_LEFT);
        exportOptions.setPadding(new Insets(15, 0, 15, 125));
        exportOptions.getChildren().addAll(exportTasks, exportMilestones, exportDeadlines, exportActivities);

        closeButton.setOnAction(event -> window.close());
        exportButton.setOnAction(e -> {
            if(exportTasks.isSelected()) { flag |= 1 << 0; }
            if(exportMilestones.isSelected()) { flag |= 1 << 1; }
            if(exportDeadlines.isSelected()) { flag |= 1 << 2; }
            if(exportActivities.isSelected()) { flag |= 1 << 3; }
            System.out.println(Byte.toString(flag));
            if(flag != 0x00) {
                try {
                    semesterProfile.exportDataCVS(flag, file == null ? exportDirectory.getInitialDirectory().getAbsolutePath() : file.getAbsolutePath());
                    flag = 0x00;
                } catch (IOException ioException) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to access file for exporting" +
                            " please ensure it is not open in another application");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, label.getText());
                alert.showAndWait();
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(exportOptions, buttons, exportLocation, label);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

    }
}
