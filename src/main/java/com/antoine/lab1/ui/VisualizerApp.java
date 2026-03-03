package com.antoine.lab1.ui;

import com.antoine.lab1.algorithms.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VisualizerApp extends Application {
    private static final int ARRAY_SIZE = 50;
    private Rectangle[] bars = new Rectangle[ARRAY_SIZE];
    private int[] values = new int[ARRAY_SIZE];
    private HBox barsContainer;

    @Override
    public void start(Stage primaryStage) {
        barsContainer = new HBox(2);
        barsContainer.setAlignment(Pos.BOTTOM_CENTER);
        barsContainer.setPrefHeight(350);

        generateRandomArray();

        ComboBox<String> algorithmDropdown = new ComboBox<>();
        algorithmDropdown.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort","Merge Sort","Quick Sort","Heap Sort");
        algorithmDropdown.getSelectionModel().selectFirst();

        Button startButton = new Button("Start Sort");
        Button resetButton = new Button("Reset Array");

        HBox controlPanel = new HBox(15, algorithmDropdown, startButton, resetButton);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(controlPanel);
        root.setBottom(barsContainer);

        resetButton.setOnAction(event -> generateRandomArray());

        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            resetButton.setDisable(true);

            String selectedAlgorithm = algorithmDropdown.getValue();

            startSortingThread(selectedAlgorithm, startButton, resetButton);
        });

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setTitle("Sorting Visualization - Algorithm Testing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateRandomArray() {
        barsContainer.getChildren().clear();
        Random rand = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            values[i] = rand.nextInt(300) + 50;
            bars[i] = new Rectangle(10, values[i]);
            bars[i].setFill(Color.DODGERBLUE);
            barsContainer.getChildren().add(bars[i]);
        }
    }

    private void startSortingThread(String algorithmName, Button startBtn, Button resetBtn) {
        Thread sortThread = new Thread(() -> {

            List<Integer> listToSort = new ArrayList<>();
            for (int v : values) listToSort.add(v);
            AbstractSort algorithm;
            if (algorithmName.equals("Selection Sort")) {
                algorithm = new SelectionSort();
            }
            else if (algorithmName.equals("Insertion Sort")) {
                algorithm = new InsertionSort();
            }
            else if (algorithmName.equals("Merge Sort")) {
                algorithm = new MergeSort();
            }
            else if (algorithmName.equals("Quick Sort")) {
                algorithm = new QuickSort();
            }
            else if (algorithmName.equals("Heap Sort")) {
                algorithm = new HeapSort();
            }
            else{
                algorithm = new BubbleSort();
            }

            algorithm.setupVisualization(30, () -> {
                for (int i = 0; i < ARRAY_SIZE; i++) {
                    bars[i].setHeight(listToSort.get(i));
                }
            });

            algorithm.sort(listToSort);

            for (int i = 0; i < ARRAY_SIZE; i++) {
                values[i] = listToSort.get(i);
            }

            System.out.println(algorithmName + "Comparisons: " + algorithm.getComparisons() +
                    "Swaps: " + algorithm.getInterchanges());

            Platform.runLater(() -> {
                startBtn.setDisable(false);
                resetBtn.setDisable(false);
            });
        });

        sortThread.setDaemon(true);
        sortThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}