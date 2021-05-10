package com.company.model;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class ModuleCharts {

    private static IntervalCategoryDataset getDataSet(Module module) {
        TaskSeries taskSeries = new TaskSeries("Tasks");
        TaskSeries deadlineSeries = new TaskSeries("Deadlines");
        TaskSeries milestoneSeries = new TaskSeries("Milestones");
        for(Task task : module.getTasks()) {
            taskSeries.add(new org.jfree.data.gantt.Task(task.getTitle(),
                    Date.from(Instant.from(task.getStart().toLocalDate().atStartOfDay(ZoneId.systemDefault()))),
                    Date.from(Instant.from(task.getEnd().toLocalDate().atStartOfDay(ZoneId.systemDefault())))));
        }
        for(Deadline deadline : module.getDeadlines()) {
           deadlineSeries.add(new org.jfree.data.gantt.Task(deadline.getTitle(),
                    Date.from(Instant.from(deadline.getDueDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()))),
                    Date.from(Instant.from(deadline.getDueDate().plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault())))));
        }
        for(Milestone milestone : module.getMilestones()) {
            deadlineSeries.add(new org.jfree.data.gantt.Task(milestone.getTitle(),
                    Date.from(Instant.from(milestone.getEnd().toLocalDate().atStartOfDay(ZoneId.systemDefault()))),
                    Date.from(Instant.from(milestone.getEnd().plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault())))));
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(taskSeries);
        dataset.add(deadlineSeries);
        dataset.add(milestoneSeries);
        return dataset;
    }

    private static void exportChart(JFreeChart chart) {
        Stage window = new Stage();
        window.setMinHeight(350);
        window.setMinWidth(350);
        Label label = new Label("Export Chart");
        HBox options = new HBox(8);
        options.setAlignment(Pos.CENTER);
        Button png = new Button("PNG");
        File file = new File(chart.getTitle().getText() + "_Chart.png");
        png.setOnAction(e -> {
            BufferedImage chartImage = chart.createBufferedImage(1920, 1080, null);
            try {
                ImageIO.write(chartImage, "png", file);
                Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION, "Your PNG has been exported to: " + file.getAbsolutePath());
                alertBox.showAndWait();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        options.getChildren().add(png);
        VBox container = new VBox(8);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(label, options);

        window.setScene(new Scene(container));
        window.showAndWait();
    }

    public static void exportChartNoShow(JFreeChart chart) {

        File file = new File(chart.getTitle().getText() + "_Chart.png");
        BufferedImage chartImage = chart.createBufferedImage(1920, 1080, null);
        try {
            ImageIO.write(chartImage, "png", file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return;
    }

    public static JFreeChart createGanttChartNoShow(Module module) {
        IntervalCategoryDataset dataset = getDataSet(module);

        return ChartFactory.createGanttChart(
                module.getTitle(), "Tasks", "Timeline", dataset,
                true, false, false);

    }
    public static void createGanttChart(Module module) {

        Stage window = new Stage();
        IntervalCategoryDataset dataset = getDataSet(module);

        JFreeChart chart = ChartFactory.createGanttChart(
                module.getTitle() + " - Right click to export", "Tasks", "Timeline", dataset,
                true, false, false);

        ChartUtils.applyCurrentTheme(chart);
        ChartViewer viewer = new ChartViewer(chart);
        

        window.setScene(new Scene(viewer));
        window.setTitle(module.getTitle() + " Gantt Chart");

        window.show();

    }

}
