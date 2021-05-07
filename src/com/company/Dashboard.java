package com.company;

import com.company.add.AddActivity;
import com.company.add.AddMilestone;
import com.company.add.AddTask;
import com.company.add.ModuleSingleView;
import com.company.model.*;
import com.company.model.Module;
import com.company.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard extends Application {


    private CalenderDisplayType displayOption;
    private final SemesterProfile semesterProfile;
    private int year, month;
    private final TilePane tile = new TilePane();
    private static Stage window;
    private static Module viewModule;
    private static Scene scene;

    public Dashboard(SemesterProfile semesterProfile) { this.semesterProfile = semesterProfile; }

    // Gets the dates for the month to display on the calender
    public List<LocalDate> getDates(LocalDate start, LocalDate end) {
        return start.datesUntil(end.plusDays(1))
                .collect(Collectors.toList());
    }
    private void makeDeleteButton(Button delete, CalenderModelClass items) {

        if(items instanceof Task) {
            delete.setOnAction(e -> {
                semesterProfile.removeTask((Task) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.TASKS;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Deadline) {
            delete.setOnAction(e -> {
                semesterProfile.removeDeadline((Deadline) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.DEADLINES;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Milestone) {
            delete.setOnAction(e -> {
                semesterProfile.removeMilestone((Milestone) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.MILESTONES;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Activity) {
            delete.setOnAction(e -> {
                semesterProfile.removeActivity((Activity) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.ACTIVITIES;
                populateCalender(tile, this.month, this.year);
            });
        }
        return;
    }

    private void makeEditButton(Button edit, CalenderModelClass items) {

        if(items instanceof Task) {
            edit.setOnAction(e -> {
                ViewTask.Display(semesterProfile, (Task) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.TASKS;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Deadline) {
            edit.setOnAction(e -> {
                ViewDeadline.Display(semesterProfile, (Deadline) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.DEADLINES;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Milestone) {
            edit.setOnAction(e -> {
                ViewMilestone.Display(semesterProfile, (Milestone) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.MILESTONES;
                populateCalender(tile, this.month, this.year);
            });
        } else if(items instanceof Activity) {
            edit.setOnAction(e -> {
                ViewActivity.Display(semesterProfile, (Activity) items);
                tile.getChildren().clear();
                displayOption = CalenderDisplayType.ACTIVITIES;
                populateCalender(tile, this.month, this.year);
            });
        }
        return;
    }

    // When switching back from another window (not opening another) uses this to set the scene again
    public static Scene getScene() { return scene; }

    /*
     * Generates a calender box with date and items.
     * Each item is in a vbox which are added to a scrollPane with the date
     */
    public ScrollPane getCalenderBox(ArrayList<CalenderModelClass> displayItems, LocalDate date) {
        String layout = "-fx-border-color: gray;\n" +
                "-fx-border-insets: 1;\n" +
                "-fx-border-width: 1;\n" +
                "-fx-border-style: dashed;\n";
        ArrayList<VBox> vBoxes = new ArrayList<>();
        Label dateLabel;
        ScrollPane box = new ScrollPane();
        box.setPrefViewportHeight(100);
        VBox container = new VBox();
        box.setMinWidth(170);
        box.setMaxWidth(170);
        box.setMinHeight(170);
        for(CalenderModelClass items : displayItems) {
            VBox vbox = new VBox();
            vbox.setMinWidth(150);
            vbox.setStyle(layout);
            Label title = new Label(items.getTitle());
            Label time = new Label(items.getEnd().toLocalTime().toString());
            Button edit = new Button("Select");
            makeEditButton(edit, items);
            Button delete = new Button("Delete");
            makeDeleteButton(delete, items);
            HBox buttons = new HBox(8);
            buttons.setAlignment(Pos.CENTER);
            buttons.getChildren().addAll(edit, delete);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(0,0,5,0));
            VBox.setMargin(vbox, new Insets(5, 5, 0, 5));
            vbox.setSpacing(5);
            vbox.getChildren().addAll(title, time, buttons);
            vBoxes.add(vbox);
        }
        container.setAlignment(Pos.CENTER);

        dateLabel = new Label(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        container.getChildren().add(dateLabel);
        for(VBox vbox : vBoxes) {
            container.getChildren().add(vbox);
        }
        dateLabel.setPadding(new Insets(5, 5, 5, 5));
        box.setFitToWidth(true);

        box.setContent(container);
        return box;
    }

    /*
     * Generates all calender boxes for the month and adds them
     */
    private void populateCalender(TilePane tile, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
        List<LocalDate> dates = this.getDates(start, end);
        ScrollPane[] calenderBoxes = new ScrollPane[dates.size()];

        for(int i = 0; i < calenderBoxes.length; i++) {
            calenderBoxes[i] = getCalenderBox(semesterProfile.getItemsFromDate(dates.get(i), displayOption), dates.get(i));
            int finalI = i;
            calenderBoxes[i].setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2) {
                    switch (displayOption) {
                        case TASKS -> {
                            AddTask.Display(semesterProfile, dates.get(finalI));
                            tile.getChildren().clear();
                            populateCalender(tile, this.month, this.year);
                        }
                        case MILESTONES -> {
                            AddMilestone.Display(semesterProfile, dates.get(finalI));
                            tile.getChildren().clear();
                            populateCalender(tile, this.month, this.year);
                        }
                        case ACTIVITIES -> {
                            AddActivity.Display(semesterProfile, dates.get(finalI));
                            tile.getChildren().clear();
                            populateCalender(tile, this.month, this.year);
                        }
                    }

                }
            });
            tile.getChildren().add(calenderBoxes[i]);
        }
    }
    /*
     * Alert box to confirm and to select to save or not?
     * Save is currently only serialising
     */
    public void saveOption() throws IOException {
        SaveDialogBox saveDialogBox = new SaveDialogBox();
        saveDialogBox.Display("Save", "Do you want to save?", semesterProfile.getStyle());
        if(SaveDialogBox.location != null) {
            semesterProfile.saveFile(SaveDialogBox.location);
        }
    }



    @Override
    public void start(Stage stage) {
        window = stage;
        // Default calender view
        displayOption = CalenderDisplayType.TASKS;
        window.setOnCloseRequest(e -> {
            semesterProfile.saveFile(semesterProfile.getSaveFileLocation());
        });
        stage.getIcons().add(new Image("file:src/com/icon.png"));
        window.setTitle("StudyBuddy - Dashboard");
        window.setMinWidth(1000);
        window.setMinHeight(500);
        // Base grid layout
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 0, 0, 0));


        // TilePane for calender
        tile.setPadding(new Insets(10, 5, 5, 5));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefRows(2);
        // tile.setStyle("-fx-background-color: DAE6F3;");
        // Only for testing

        // Vbox for side options
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setAlignment(Pos.CENTER);
        // Only for testing
        String[] buttonTitles = {"Modules", "Export", "Search"};
        Button[] sideButtons = new Button[3];


        for(int i = 0; i < sideButtons.length; i++) {
            Button button = new Button(buttonTitles[i]);
            button.setMinWidth(200);
            button.setMinHeight(150);
            sideButtons[i] = button;
            vbox.getChildren().add(sideButtons[i]);
        }
        sideButtons[0].setOnAction(e -> {
            viewModule = ModuleSingleView.DisplayModules(semesterProfile);
            if(viewModule != null) {
                window.setScene(ViewModule.getScene(viewModule, window, semesterProfile));
            }
        });

        sideButtons[1].setOnAction(e -> ExportData.Display(semesterProfile));

        sideButtons[2].setOnAction(e -> {
            window.setScene(Search.Display(semesterProfile, window));
        });

        // View type toggle buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioMenuItem activitiesButton = new RadioMenuItem("Activities");
        activitiesButton.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            displayOption = CalenderDisplayType.ACTIVITIES;
            populateCalender(tile, this.month, this.year);
        });
        activitiesButton.setToggleGroup(toggleGroup);
        RadioMenuItem tasksButton = new RadioMenuItem("Tasks");
        tasksButton.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            displayOption = CalenderDisplayType.TASKS;
            populateCalender(tile, this.month, this.year);
        });
        tasksButton.setToggleGroup(toggleGroup);
        RadioMenuItem milestonesButton = new RadioMenuItem("Milestones");
        milestonesButton.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            displayOption = CalenderDisplayType.MILESTONES;
            populateCalender(tile, this.month, this.year);
        });
        milestonesButton.setToggleGroup(toggleGroup);
        RadioMenuItem deadlinesButton = new RadioMenuItem("Deadlines");
        deadlinesButton.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            displayOption = CalenderDisplayType.DEADLINES;
            populateCalender(tile, this.month, this.year);
        });
        tasksButton.setSelected(true);
        deadlinesButton.setToggleGroup(toggleGroup);

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Load File");
        MenuItem saveFile = new MenuItem("Save File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> {
            semesterProfile.saveFile(semesterProfile.getSaveFileLocation());
            Platform.exit();
        });
        saveFile.setOnAction(e -> {
            try {
                saveOption();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        ToggleGroup toggleMute = new ToggleGroup();
        RadioMenuItem muteButton = new RadioMenuItem("Mute");
        muteButton.setOnAction(e -> {
            semesterProfile.setMuted(true);
        });
        muteButton.setToggleGroup(toggleMute);
        RadioMenuItem unMuteButton = new RadioMenuItem("Unmute");
        unMuteButton.setToggleGroup(toggleMute);
        unMuteButton.setOnAction(e -> {
            semesterProfile.setMuted(false);
        });
        unMuteButton.setSelected(true);
        Menu toggleMuteMenu = new Menu("Toggle Mute");
        toggleMuteMenu.getItems().addAll(muteButton, unMuteButton);

        Menu displayMenu = new Menu("Display");
        displayMenu.getItems().addAll(tasksButton, deadlinesButton, activitiesButton, milestonesButton);
        file.getItems().addAll(openFile, saveFile, exit);
        Menu addMenu = new Menu("Add");
        MenuItem addTask = new MenuItem("Add Task");
        MenuItem addActivity = new MenuItem("Add Activity");
        MenuItem addMilestone = new MenuItem("Add Milestone");
        addTask.setOnAction(e -> {
            AddTask.Display(semesterProfile, null); // null or LocalDate.now()
            tile.getChildren().clear();
            populateCalender(tile, this.month, this.year);

        });
        addActivity.setOnAction(e -> {
            AddActivity.Display(semesterProfile, null);
            tile.getChildren().clear();
            populateCalender(tile, this.month, this.year);

        });
        addMilestone.setOnAction(e -> {
            AddMilestone.Display(semesterProfile, null);
            tile.getChildren().clear();
            populateCalender(tile, this.month, this.year);
        });
        Menu themeSelect = new Menu("Select Theme");
        MenuItem defaultTheme = new MenuItem("Default");
        MenuItem zenburnTheme = new MenuItem("Zenburn");
        MenuItem koalin_lightTheme = new MenuItem("Koalin-Light");
        themeSelect.getItems().addAll(defaultTheme, zenburnTheme, koalin_lightTheme);
        defaultTheme.setOnAction(e -> {
            semesterProfile.setStyle("default.css");
            scene.getStylesheets().removeAll("zenburn.css", "koalin-light.css", "default.css");
            scene.getStylesheets().add("default.css");
            scene.setUserAgentStylesheet(semesterProfile.getStyle());
        });
        zenburnTheme.setOnAction(e -> {
            semesterProfile.setStyle("zenburn.css");
            scene.getStylesheets().removeAll("zenburn.css", "koalin-light.css", "default.css");
            scene.getStylesheets().add("zenburn.css");
            scene.setUserAgentStylesheet(semesterProfile.getStyle());
        });
        koalin_lightTheme.setOnAction(e -> {
            semesterProfile.setStyle("koalin-light.css");
            scene.getStylesheets().removeAll("zenburn.css", "koalin-light.css", "default.css");
            scene.getStylesheets().add("koalin-light.css");
            scene.setUserAgentStylesheet(semesterProfile.getStyle());
        });

        addMenu.getItems().addAll(addTask, addActivity, addMilestone);
        menuBar.getMenus().addAll(file, displayMenu, addMenu, themeSelect, toggleMuteMenu);


        VBox menuVBox= new VBox(menuBar);


        // Change calender
        Button next = new Button("Next ->");
        Button prev = new Button("<- Prev ");
        HBox options = new HBox();
        options.setAlignment(Pos.TOP_CENTER);
        options.setPadding(new Insets(0, 0, 10, 0));
        options.getChildren().addAll(prev, next);

        year = LocalDate.now().getYear();
        month = LocalDate.now().getMonthValue();
        populateCalender(tile, month, year);
        next.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            if(this.month + 1 != 13) {
                this.month = this.month + 1;
                populateCalender(tile, this.month, this.year);
            } else {
                this.month = 1;
                this.year = this.year + 1;
                populateCalender(tile, this.month, this.year);
            }
        });

        prev.setOnAction(actionEvent -> {
            tile.getChildren().clear();
            if(this.month - 1 != 0) {
                this.month = this.month - 1;
                populateCalender(tile, this.month, this.year);
            } else {
                this.month = 12;
                this.year = this.year - 1;
                populateCalender(tile, this.month, this.year);
            }
        });
        window.setOnCloseRequest(windowEvent -> {
            semesterProfile.saveFile(semesterProfile.getSaveFileLocation());
            Platform.exit();
            System.exit(0);
        });

        ReminderHandler reminderHandler = new ReminderHandler();
        reminderHandler.start(semesterProfile);
        //System.out.println(reminder.getRelatedEvent().getTitle());
        borderPane.setBottom(options);
        borderPane.setTop(menuVBox);
        borderPane.setLeft(vbox);
        borderPane.setCenter(tile);
        scene = new Scene(borderPane);
        scene.getStylesheets().add(semesterProfile.getStyle() == null ? "default.css" : semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle() == null ? "default.css" : semesterProfile.getStyle());
        semesterProfile.setStyle("default.css");
        window.setScene(scene);
        window.show();

    }
}
