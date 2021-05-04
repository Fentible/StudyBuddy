package com.company.model;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.*;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.chart.fx.ChartViewer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class ModuleCharts {

    private static IntervalCategoryDataset getDataSet(Module module) {
        TaskSeries taskSeries = new TaskSeries("Tasks");
        TaskSeries deadlineSeries = new TaskSeries("Deadlines");

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

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(taskSeries);
        dataset.add(deadlineSeries);
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

    public static void createGanttChart(Module module) {

        Stage window = new Stage();
        IntervalCategoryDataset dataset = getDataSet(module);

        JFreeChart chart = ChartFactory.createGanttChart(
                module.getTitle(), "Tasks", "Timeline", dataset,
                true, false, false);

        ChartUtilities.applyCurrentTheme(chart);
        ChartViewer viewer = new ChartViewer(chart);
        viewer.addChartMouseListener(new ChartMouseListenerFX() {
            @Override
            public void chartMouseClicked(ChartMouseEventFX event) {
                exportChart(viewer.getChart());
            }

            @Override
            public void chartMouseMoved(ChartMouseEventFX event) {

            }
        });

        viewer.getCanvas().setOnMouseDragged(Event::consume);
        viewer.getCanvas().setOnMouseReleased(Event::consume);
        viewer.getCanvas().setOnMouseDragReleased(Event::consume);
        

        window.setScene(new Scene(viewer));
        window.setTitle(module.getTitle() + " Gantt Chart");

        window.show();

    }

}
