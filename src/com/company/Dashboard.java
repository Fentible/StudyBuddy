package com.company;

import com.company.model.CalenderDisplayType;
import com.company.model.CalenderModelClass;
import com.company.model.SemesterProfile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard extends Application {


    private CalenderDisplayType displayOption;
    private SemesterProfile semesterProfile;
    private int year, month;
    private TilePane tile = new TilePane();
    private Stage window;


    public Dashboard(SemesterProfile semesterProfile) { this.semesterProfile = semesterProfile; }


    public List<LocalDate> getDates(LocalDate start, LocalDate end) {
        return start.datesUntil(end)
                .collect(Collectors.toList());
    }

    /*
     * Generates a calender box with date and items. Each item is in a vbox which are added to a scrollpane
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
        box.setMinHeight(170);
        for(CalenderModelClass items : displayItems) {
            VBox vbox = new VBox();
            vbox.setMinWidth(150);
            vbox.setStyle(layout);
            Label title = new Label(items.getTitle());
            Label time = new Label(items.getEnd().toLocalTime().toString());
            Button edit = new Button("Edit");
            // edit.setOnAction(e -> openEditWindow);
            vbox.setAlignment(Pos.CENTER);
            VBox.setMargin(vbox, new Insets(5, 5, 0, 5));
            vbox.setSpacing(5);
            vbox.getChildren().addAll(title, time, edit);
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
     * Generates all calender boxes and adds them
     */
    private void populateCalender(TilePane tile, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
        List<LocalDate> dates = this.getDates(start, end);
        ScrollPane[] calenderBoxes = new ScrollPane[dates.size()];
        for(int i = 0; i < calenderBoxes.length; i++) {
            calenderBoxes[i] = getCalenderBox(semesterProfile.getItemsFromDate(dates.get(i), displayOption), dates.get(i));
            tile.getChildren().add(calenderBoxes[i]);
        }
        return;

    }
    /*
     * Alert box to confirm and to select to save or not?
     * Save is currently only serialising
     */
    public void closeProgram() {
        try {
            FileOutputStream fileOut = new FileOutputStream("src/com/company/model/profile.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.semesterProfile);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        window.close();
    }

    @Override
    public void start(Stage stage) {
        window = stage;
        // Default calender view
        displayOption = CalenderDisplayType.TASKS;
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

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
        Button[] sideButtons = new Button[8];
        for(int i = 0; i < sideButtons.length; i++) {
            Button button = new Button("Button " + i);
            button.setMinWidth(200);
            button.setMinHeight(75);
            sideButtons[i] = button;
            vbox.getChildren().add(sideButtons[i]);
        }

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
        MenuItem openFile = new MenuItem("Open File");
        Menu displayMenu = new Menu("Display");
        displayMenu.getItems().addAll(tasksButton, deadlinesButton, activitiesButton, milestonesButton);
        file.getItems().add(openFile);
        menuBar.getMenus().addAll(file, displayMenu);
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

        borderPane.setBottom(options);
        borderPane.setTop(menuVBox);
        borderPane.setLeft(vbox);
        borderPane.setCenter(tile);
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();

    }
}
