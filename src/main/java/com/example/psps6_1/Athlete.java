package com.example.psps6_1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Athlete implements Runnable {
    private String name;
    private Rectangle rectangle;
    private double angle;
    private double radius;
    private double centerX;
    private double centerY;
    private int finishLine;
    private Random random;
    private boolean stopped;
    private boolean finished;
    private static int finishedCount;
    private static int totalAthletes;

    private static List<String> finishedAthletes;

    public Athlete(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }




    public Athlete(String name, double angle, double radius, double centerX, double centerY, int finishLine) {
        this.name = name;
        this.angle = angle;
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.finishLine = finishLine;
        this.random = new Random();
        this.stopped = false;
        this.finished = false;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void stop() {
        stopped = true;
    }


    public void reset() {
        angle = 0;
        stopped = false;
        finished = false;
    }

    public void run() {
        int maxSpeed = 5; // Maximum change in angle per iteration

        while (angle < finishLine && !stopped) {
            int speed = random.nextInt(maxSpeed) + 1; // Randomly determine speed
            angle += speed; // Update angle

            // Calculate new position based on angle
            double newX = centerX + radius * Math.cos(Math.toRadians(angle));
            double newY = centerY + radius * Math.sin(Math.toRadians(angle));

            Platform.runLater(() -> {
                rectangle.setX(newX);
                rectangle.setY(newY);
            });

            try {
                Thread.sleep(100); // Pause for 0.1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        if (!stopped) {
            setFinished(name);

            Platform.runLater(() -> {
                checkAllFinished();

            });
        }

    }
    private static synchronized void setFinished(String name) {
        finishedCount++;
        finishedAthletes.add(name);
        System.out.println();
    }

    private void checkAllFinished() {
        if (finishedCount == totalAthletes) {
            showResultsModal();
            resetRace();
        }
    }

    private static void resetRace() {
        finishedCount = 0;
        finishedAthletes.clear();
    }

    private void showResultsModal() {
        Stage resultsModal = new Stage();
        resultsModal.initModality(Modality.APPLICATION_MODAL);
        resultsModal.setTitle("Race Results");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        for (int i = 0; i < finishedAthletes.size(); i++) {
            String athleteName = finishedAthletes.get(i);
            Label resultLabel = new Label("Position " + (i + 1) + ": " + athleteName);
            vbox.getChildren().add(resultLabel);
        }

        Scene scene = new Scene(vbox);
        resultsModal.setScene(scene);
        resultsModal.showAndWait();
    }

    public static void setTotalAthletes(int count) {
        totalAthletes = count;
        finishedCount = 0;

            finishedAthletes = new ArrayList<>();

    }

}

