package com.company;

import com.company.edit.EditTask;
import com.company.model.*;
import com.company.view.ViewDeadline;
import com.company.view.ViewTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.swing.text.View;
import java.time.LocalDate;
import java.util.*;

public class Search {

    private static CalenderDisplayType type;
    private static ObservableList<CalenderModelClass> modelList = FXCollections.observableArrayList();

    private static void viewModel(SemesterProfile semesterProfile, CalenderModelClass modelClass) {
        if(modelClass instanceof Task) {
            ViewTask.Display(semesterProfile, (Task) modelClass);
        } else if(modelClass instanceof Deadline) {
            ViewDeadline.Display(semesterProfile, (Deadline) modelClass);
        }
        return;
    }

    public static ObservableList<CalenderModelClass> populateList(SemesterProfile semesterProfile, String title,
                                                                  LocalDate start, LocalDate end, CalenderDisplayType type) {
        ObservableList<CalenderModelClass> modelList = FXCollections.observableArrayList();
        //System.out.println(title + " : " + date.toString());
        if(type != null) {
            switch (type) {
                case TASKS -> {
                    Optional.ofNullable(semesterProfile.getTasks()).ifPresent(modelList::addAll);
                    if (start != null) {
                        modelList.retainAll(semesterProfile.getTasksFromDate(start,  end));
                    }
                    if (title != null) {
                        modelList.retainAll(semesterProfile.getTasks(title));
                    }
                }
                case DEADLINES -> {
                    Optional.ofNullable(semesterProfile.getDeadlines()).ifPresent(modelList::addAll);
                    if (start != null) {
                        modelList.retainAll(semesterProfile.getDeadlinesFromDate(start, end));
                    }
                    if (title != null) {
                        modelList.retainAll(semesterProfile.getDeadlines(title));
                    }
                }
                case MILESTONES -> {
                    Optional.ofNullable(semesterProfile.getMilestones()).ifPresent(modelList::addAll);
                    if (start != null) {
                        modelList.retainAll(semesterProfile.getMilestonesFromDate(start, end));
                    }
                    if (title != null) {
                        modelList.retainAll(semesterProfile.getMilestones(title));
                    }
                }
                case ACTIVITIES -> {
                    Optional.ofNullable(semesterProfile.getActivities()).ifPresent(modelList::addAll);
                    if (start != null) {
                        modelList.retainAll(semesterProfile.getActivitiesFromDate(start, end));
                    }
                    if (title != null) {
                        modelList.retainAll(semesterProfile.getActivities(title));
                    }
                }
            }
        } else {
            modelList.addAll(semesterProfile.getAll());

            if (start != null) {
                ArrayList<CalenderModelClass> temp = new ArrayList<>();
                temp.addAll(semesterProfile.getActivitiesFromDate(start, end));
                temp.addAll(semesterProfile.getTasksFromDate(start, end));
                temp.addAll(semesterProfile.getMilestonesFromDate(start, end));
                temp.addAll(semesterProfile.getDeadlinesFromDate(start, end));
                modelList.retainAll(temp);
            }
            if (title != null && !title.equals("")) {
                ArrayList<CalenderModelClass> temp = new ArrayList<>();
                temp.addAll(semesterProfile.getActivities(title));
                temp.addAll(semesterProfile.getTasks(title));
                temp.addAll(semesterProfile.getDeadlines(title));
                temp.addAll(semesterProfile.getMilestones(title));
                modelList.retainAll(temp);
            }
        }


        return modelList;
    }



    public static Scene Display(SemesterProfile semesterProfile, Stage window) {

        // Opens window slightly offset from the current one
        List<Window> open = Stage.getWindows();

        Button cancelButton = new Button("Close");
        Button searchButton = new Button("Search");
        HBox confirmButtons = new HBox(10);
        confirmButtons.getChildren().addAll(cancelButton, searchButton);
        confirmButtons.setAlignment(Pos.CENTER_RIGHT);

        TextField searchInput = new TextField();
        searchInput.setPromptText("Item's title");

        DatePicker startDate = new DatePicker();
        startDate.setChronology(LocalDate.now().getChronology());

        DatePicker endDate = new DatePicker();
        startDate.setChronology(LocalDate.now().getChronology());

        ToggleGroup modelSelection = new ToggleGroup();
        RadioMenuItem tasksOption = new RadioMenuItem("Tasks");
        RadioMenuItem activitiesOption = new RadioMenuItem("Activities");
        RadioMenuItem milestonesOption = new RadioMenuItem("Milestones");
        RadioMenuItem deadlinesOption = new RadioMenuItem("Deadlines");
        RadioMenuItem allOption = new RadioMenuItem("All");
        tasksOption.setToggleGroup(modelSelection);
        activitiesOption.setToggleGroup(modelSelection);
        milestonesOption.setToggleGroup(modelSelection);
        deadlinesOption.setToggleGroup(modelSelection);
        allOption.setToggleGroup(modelSelection);
        Menu menu = new Menu("All");
        menu.getItems().addAll(allOption, tasksOption, activitiesOption, milestonesOption, deadlinesOption);
        MenuBar menuBar = new MenuBar(menu);
        activitiesOption.setOnAction(e -> {
            menu.setText("Activities");
        });
        deadlinesOption.setOnAction(e -> {
            menu.setText("Deadlines");
        });
        milestonesOption.setOnAction(e -> {
            menu.setText("Milestones");
        });
        tasksOption.setOnAction(e -> {
            menu.setText("Tasks");
        });
        allOption.setOnAction(e -> {
            menu.setText("All");
        });
        allOption.setSelected(true);

        HBox searchItems = new HBox(8);
        searchItems.getChildren().addAll(searchInput, startDate, endDate, searchButton, menuBar);
        searchItems.setAlignment(Pos.CENTER);

        modelList = FXCollections.observableArrayList();
        TableView<CalenderModelClass> tableOfModel = new TableView<>(modelList);
        TableColumn<CalenderModelClass, String> nameColumn = new TableColumn<>("Name");
        TableColumn<CalenderModelClass, String> dateColumn = new TableColumn<>("Due Date");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toLocalDate().toString()));
        tableOfModel.getColumns().addAll(nameColumn, dateColumn);
        tableOfModel.setRowFactory(e -> {
            TableRow<CalenderModelClass> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    if(row.getItem() != null) {
                        viewModel(semesterProfile, row.getItem());
                    }
                }
            });
            return row;
        });

        searchButton.setOnAction(e -> {
            if(modelSelection.getSelectedToggle() == activitiesOption) {
                type = CalenderDisplayType.ACTIVITIES;
            } else if(modelSelection.getSelectedToggle() == milestonesOption) {
                type = CalenderDisplayType.ACTIVITIES;
            } else if(modelSelection.getSelectedToggle() == deadlinesOption) {
                type = CalenderDisplayType.DEADLINES;
            } else if(modelSelection.getSelectedToggle() == tasksOption) {
                type = CalenderDisplayType.TASKS;
            } else {
                type = null;
            }
            if(startDate.getEditor().getText().equals("")) {
                startDate.setValue(null);
            }
            modelList.clear();
            modelList.addAll(populateList(semesterProfile, searchInput.getText(), startDate.getValue(), endDate.getValue(), type));
            //System.out.println(Arrays.deepToString(modelList.toArray()));

        });

        tableOfModel.setPrefWidth(1000);
        tableOfModel.setMinWidth(1000);
        tableOfModel.setMaxWidth(1000);
        tableOfModel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        cancelButton.setOnAction(e -> {
                window.setScene(Dashboard.getScene());
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(searchItems, 0, 0);
        gridPane.add(tableOfModel, 0, 1, 10, 10);
        gridPane.add(cancelButton, 0, 11);
        GridPane.setHgrow(gridPane, Priority.ALWAYS);
        GridPane.setVgrow(gridPane, Priority.ALWAYS);
        GridPane.setHalignment(gridPane, HPos.CENTER);
        GridPane.setFillWidth(gridPane, true);

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        return window.getScene();
    }

}
