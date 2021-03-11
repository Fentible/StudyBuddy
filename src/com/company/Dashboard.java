package com.company;

import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard extends Application {


    SemesterProfile semesterProfile;
    int year, month;
    TilePane tile = new TilePane();

    public Dashboard(SemesterProfile semesterProfile) { this.semesterProfile = semesterProfile; }

    public List<LocalDate> getDates(LocalDate start, LocalDate end) {
        return start.datesUntil(end)
                .collect(Collectors.toList());
    }

    public ScrollPane getCalenderBox(ArrayList<Task> tasks, LocalDate date) {
        String layout = "-fx-border-color: gray;\n" +
                "-fx-border-insets: 1;\n" +
                "-fx-border-width: 1;\n" +
                "-fx-border-style: dashed;\n";
        ArrayList<VBox> vBoxes = new ArrayList<>();
        Label dateLabel;
        ScrollPane box = new ScrollPane();
        box.setPrefViewportHeight(100);
        VBox container = new VBox();
        for(Task task : tasks) {
            VBox vbox = new VBox();
            vbox.setStyle(layout);
            Label title = new Label(task.getTitle());
            Label time = new Label(task.getEnd().toLocalTime().toString());
            Button edit = new Button("Edit");
            vbox.getChildren().addAll(title, time, edit);
            vBoxes.add(vbox);
        }
        dateLabel = new Label(date.toString());
        for(VBox vbox : vBoxes) {
            container.getChildren().add(vbox);
        }
        container.getChildren().add(dateLabel);
        box.setContent(container);
        return box;
    }

    private TilePane populateCalender(TilePane tile, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
        List<LocalDate> dates = this.getDates(start, end);
        ScrollPane[] calenderBoxes = new ScrollPane[dates.size()];
        for(int i = 0; i < calenderBoxes.length; i++) {
            calenderBoxes[i] = getCalenderBox(semesterProfile.getTasksFromDate(dates.get(i)), dates.get(i));
            tile.getChildren().add(calenderBoxes[i]);
        }
        return tile;

    }

    @Override
    public void start(Stage stage) {


        stage.setTitle("StudyBuddy - Dashboard");
        stage.setMinWidth(1000);
        stage.setMinHeight(500);
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
        // Only for testing
        Button[] sideButtons = new Button[8];
        for(int i = 0; i < sideButtons.length; i++) {
            sideButtons[i] = new Button("Button " + i);
            vbox.getChildren().add(sideButtons[i]);
        }

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        file.getItems().add(openFile);
        menuBar.getMenus().add(file);
        VBox menuVBox= new VBox(menuBar);

        // Change calender
        Button next = new Button("Next ->");
        Button prev = new Button("<- Prev ");
        HBox options = new HBox();
        options.getChildren().addAll(prev, next);

        year = LocalDate.now().getYear();
        month = LocalDate.now().getMonthValue();
        tile = populateCalender(tile, month, year);
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
        stage.setScene(scene);
        stage.show();

    }
}
