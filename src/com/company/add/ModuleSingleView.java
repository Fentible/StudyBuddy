package com.company.add;

import com.company.model.SemesterProfile;
import com.company.model.Module;
import com.company.model.Task;
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

public class ModuleSingleView {

    public static Module module;

    /*
     * See 'AssignmentSingleView' for notes as it is similar
     */
    public static Module DisplayModules(SemesterProfile semesterProfile) {
        module = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Module");
        window.setMinWidth(750);
        window.setMinHeight(400);
        ObservableList<Module> modulesList = FXCollections.observableArrayList();

        modulesList.addAll(semesterProfile.getModules());
        ListView<Module> listOfModule = new ListView<>(modulesList);
        listOfModule.setCellFactory(param -> new ListCell<Module>() {
            @Override
            protected void updateItem(Module module, boolean empty) {
                super.updateItem(module, empty);
                if(empty || module == null) {
                    setText(null);
                } else {
                    setText(module.getTitle());
                }
            }
        });

        if(module!= null) {
                listOfModule.getSelectionModel().select(module);
        }
        listOfModule.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            module = listOfModule.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfModule, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return module;

    }

    public static Module DisplayModules(SemesterProfile semesterProfile, Task passedTask) {
        module = passedTask.getModule();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Module");
        window.setMinWidth(750);
        window.setMinHeight(400);
        ObservableList<Module> modulesList = FXCollections.observableArrayList();

        modulesList.addAll(semesterProfile.getModules());
        ListView<Module> listOfModule = new ListView<>(modulesList);
        listOfModule.setCellFactory(param -> new ListCell<Module>() {
            @Override
            protected void updateItem(Module module, boolean empty) {
                super.updateItem(module, empty);
                if(empty || module == null) {
                    setText(null);
                } else {
                    setText(module.getTitle());
                }

            }
        });
        listOfModule.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(module!= null) {
                Platform.runLater(() -> {
                    listOfModule.requestFocus();
                    listOfModule.getSelectionModel().select(module);
                });
        }

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            module = listOfModule.getSelectionModel().getSelectedItem();
            window.close();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(listOfModule, confirm);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

        return module;

    }

}
