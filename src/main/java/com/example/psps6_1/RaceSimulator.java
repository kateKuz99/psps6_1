package com.example.psps6_1;

import com.example.psps6_1.Athlete;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RaceSimulator extends Application {
    private static final int FINISH_LINE = 360;
    private List<Athlete> athletes;

    private Button startButton;
    private Button stopButton;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.setPrefSize(500, 500);
        pane.setStyle("-fx-background-color: #808080");
        pane.setPadding(new Insets(0,100,0,100));

        Circle outerCircle = new Circle(250, 250, 200, Color.PALEGREEN);
        Circle middleCircle = new Circle(250, 250, 150, Color.GREEN);
        Circle innerCircle = new Circle(250, 250, 100, Color.DARKGREEN);

        Rectangle rectangle1 = new Rectangle(10, 10, Color.RED);
        Rectangle rectangle2 = new Rectangle(10, 10, Color.RED);
        Rectangle rectangle3 = new Rectangle(10, 10, Color.RED);

        athletes = new ArrayList<>();
        athletes.add(new Athlete("Athlete 1", 0, 200, 250, 250, FINISH_LINE));
        athletes.add(new Athlete("Athlete 2", 0, 150, 250, 250, FINISH_LINE));
        athletes.add(new Athlete("Athlete 3", 0, 100, 250, 250, FINISH_LINE));

        athletes.get(0).setRectangle(rectangle1);
        athletes.get(1).setRectangle(rectangle2);
        athletes.get(2).setRectangle(rectangle3);

        Athlete.setTotalAthletes(athletes.size());

        startButton = new Button("Start");

        startButton.setStyle("-fx-background-color: #CD5C5C" + "; -fx-text-fill: white; -fx-font-size: 14px;");
        stopButton = new Button("Stop");
        stopButton.setStyle("-fx-background-color: #CD5C5C" + "; -fx-text-fill: white; -fx-font-size: 14px;");

        Text resultText = new Text();
        stopButton.setDisable(true);

        startButton.setOnAction(event -> {
            // Disable the start button after it is pressed
            startButton.setDisable(true);
            stopButton.setDisable(false);
            resultText.setText("");

            for (Athlete athlete : athletes) {
                Thread thread = new Thread(() -> runRace(athlete));
                thread.start();
            }
        });

        stopButton.setOnAction(event -> {
            // Stop allathletes and enable the start button again
            for (Athlete athlete : athletes) {
                athlete.stop();
            }

            // Enable the start button and disable the stop button
            startButton.setDisable(false);
            stopButton.setDisable(true);
        });

        pane.getChildren().addAll(outerCircle, middleCircle, innerCircle,
                rectangle1, rectangle2, rectangle3, startButton, stopButton, resultText);

        rectangle1.setX(250);
        rectangle1.setY(50);
        rectangle2.setX(250);
        rectangle2.setY(50);
        rectangle3.setX(250);
        rectangle3.setY(50);

        startButton.setLayoutX(20);
        startButton.setLayoutY(20);
        stopButton.setLayoutX(100);
        stopButton.setLayoutY(20);
        resultText.setLayoutX(200);
        resultText.setLayoutY(20);

        Scene scene = new Scene(pane);
        primaryStage.setTitle("Race Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void runRace(Athlete athlete) {
        athlete.reset(); // Reset athlete's position and state

        Thread thread = new Thread(athlete);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}