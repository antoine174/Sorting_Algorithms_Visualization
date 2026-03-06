package com.antoine.lab1.ui;

import com.antoine.lab1.algorithms.*;
import com.antoine.lab1.utils.ArrayGenerate;
import com.antoine.lab1.utils.BatchTestRunner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VisualizerApp extends Application {

    private List<Rectangle> bars = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();
    private int arrsize = 50;
    private double Factor = 1.0;

    private HBox barsContainer;
    private Label statsLabel;

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab visualizationTab = new Tab("Sorting Visualization", createVisualizationView(primaryStage));
        Tab comparisonTab = new Tab("Sorting Comparison", createComparisonView(primaryStage));

        tabPane.getTabs().addAll(visualizationTab, comparisonTab);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setTitle("Sorting Algorithms Lab 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

  private BorderPane createVisualizationView(Stage stage) {
        barsContainer = new HBox(2);
        barsContainer.setAlignment(Pos.BOTTOM_CENTER);
        barsContainer.setPrefHeight(400);

         ComboBox<String> algorithmDropdown = new ComboBox<>();
        algorithmDropdown.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Heap Sort");
        algorithmDropdown.getSelectionModel().selectFirst();

        Slider delaySlider = new Slider(1, 200, 30);
        delaySlider.setShowTickLabels(true);
        delaySlider.setShowTickMarks(true);
        delaySlider.setPrefWidth(150);

         Spinner<Integer> sizeSpinner = new Spinner<>(5, 100, 50);
        sizeSpinner.setPrefWidth(70);

        Button startButton = new Button("Start Sort");
        Button generateRandomBtn = new Button("Random Array");
        Button loadCsvBtn = new Button("Load from CSV");

        statsLabel = new Label("Comparisons: 0 | Interchanges: 0");
        statsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

         HBox topControls = new HBox(15,
                new Label("Algo:"), algorithmDropdown,
                new Label("Size:"), sizeSpinner,
                new Label("Delay (ms):"), delaySlider
        );
        topControls.setAlignment(Pos.CENTER);

        HBox buttonControls = new HBox(15, generateRandomBtn, loadCsvBtn, startButton);
        buttonControls.setAlignment(Pos.CENTER);

        VBox controlPanel = new VBox(15, topControls, buttonControls, statsLabel);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(15));

        BorderPane root = new BorderPane();
        root.setTop(controlPanel);
        root.setBottom(barsContainer);

         generateRandomArray(sizeSpinner.getValue());

         generateRandomBtn.setOnAction(event -> {
            generateRandomArray(sizeSpinner.getValue());
            statsLabel.setText("Comparisons: 0 | Interchanges: 0");
        });

        loadCsvBtn.setOnAction(event -> loadCSV(stage));

        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            generateRandomBtn.setDisable(true);
            loadCsvBtn.setDisable(true);
            startSorting(algorithmDropdown.getValue(), (long) delaySlider.getValue(), startButton, generateRandomBtn, loadCsvBtn);
        });

        return root;
    }

    private void generateRandomArray(int size) {
        this.arrsize = size;
        barsContainer.getChildren().clear();
        bars.clear();
        values.clear();

        Random rand = new Random();
        Factor = 1.0;
        double barWidth = Math.max(2,800.0/size -2);

        for (int i=0; i<size; i++) {
            int val = rand.nextInt(350) + 20;
            values.add(val);
            Rectangle r = new Rectangle(barWidth, val * Factor);
            r.setFill(Color.DODGERBLUE);
            bars.add(r);
            barsContainer.getChildren().add(r);
        }
    }

    private void loadCSV(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                List<List<Integer>> testCases = ArrayGenerate.readMultipleTESTCASES(file.getAbsolutePath());
                if(!testCases.isEmpty()) {
                    List<Integer> firstCase =testCases.get(0);
                    this.arrsize =Math.min(firstCase.size(), 100);
                    barsContainer.getChildren().clear();
                    bars.clear();
                    values.clear();
                    double barWidth=Math.max(2,800.0/ arrsize -2);
                         int maxVal=firstCase.stream().max(Integer::compareTo).orElse(1);
                    Factor=350.0/Math.max(maxVal,1);
                    for (int i = 0; i< arrsize; i++) {
                        int originalVal=firstCase.get(i);
                        values.add(originalVal);
                        Rectangle r = new Rectangle(barWidth, Math.max(5, originalVal * Factor));
                        r.setFill(Color.DODGERBLUE);
                        bars.add(r);
                        barsContainer.getChildren().add(r);
                    }
                    statsLabel.setText("Loaded from CSV! (Size: " + arrsize + ")");
                }
            } catch (Exception e) {
                statsLabel.setText("Error loading CSV file.");
                e.printStackTrace();
            }
        }
    }

    private void startSorting(String algorithmName, long delayMs, Button startBtn, Button randBtn, Button csvBtn) {
        Thread sortThread = new Thread(() -> {
            List<Integer> listToSort = new ArrayList<>(values);

            AbstractSort algorithm;
            switch (algorithmName) {
                case "Selection Sort": algorithm = new SelectionSort(); break;
                case "Insertion Sort": algorithm = new InsertionSort(); break;
                case "Merge Sort": algorithm = new MergeSort(); break;
                case "Quick Sort": algorithm = new QuickSort(); break;
                case "Heap Sort": algorithm = new HeapSort(); break;
                default: algorithm = new BubbleSort(); break;
            }

            algorithm.setupVisualization(delayMs, () -> {
                for (int i = 0; i < arrsize; i++) {
                    bars.get(i).setHeight(Math.max(5, listToSort.get(i) * Factor));
                }
                statsLabel.setText("Comparisons: " + algorithm.getComparisons() + " | Interchanges: " + algorithm.getInterchanges());
            });

            algorithm.sort(listToSort);

            values = new ArrayList<>(listToSort);

            Platform.runLater(() -> {
                startBtn.setDisable(false);
                randBtn.setDisable(false);
                csvBtn.setDisable(false);
            });
        });

        sortThread.setDaemon(true);
        sortThread.start();
    }

    private VBox createComparisonView(Stage stage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Label title = new Label("Batch Run Sorting Comparison");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Random", "Sorted", "Inversely Sorted");
        typeBox.getSelectionModel().selectFirst();

        TextField sizeField = new TextField("1000");
        sizeField.setPromptText("Array Size");

        Button runGeneratedBtn = new Button("Run Benchmark on Generated Array");

        HBox generateControls = new HBox(10, new Label("Type:"), typeBox, new Label("Size:"), sizeField);
        generateControls.setAlignment(Pos.CENTER);

        Button runFileBtn = new Button("Select CSV File and Run Benchmark");
        Label statusLabel = new Label("Ready.");

        runGeneratedBtn.setOnAction(e -> {
            try {
                int size = Integer.parseInt(sizeField.getText());
                String type = typeBox.getValue();
                List<Integer> data;

                if (type.equals("Sorted")) data = ArrayGenerate.SortedArr(size);
                else if (type.equals("Inversely Sorted")) data = ArrayGenerate.reverselySorted(size);
                else data = ArrayGenerate.GENrandom(size);

                statusLabel.setText("Running generated benchmark... Check console.");
                new Thread(() -> {
                    BatchTestRunner.testGenerted(data, type, 5, "comparison_results_generated.csv");
                    Platform.runLater(() -> statusLabel.setText("Done! Saved to comparison_results_generated.csv"));
                }).start();
            } catch (NumberFormatException ex) {
                statusLabel.setText("Error: Invalid array size.");
            }
        });

        runFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Test Cases CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                statusLabel.setText("Processing file... Check console.");
                new Thread(() -> {
                    BatchTestRunner.TestFile(file.getAbsolutePath(), 5, "comparison_results_file.csv");
                    Platform.runLater(() -> statusLabel.setText("Done! Saved to comparison_results_file.csv"));
                }).start();
            }
        });

        layout.getChildren().addAll(
                title, new Separator(),
                new Label("Option A:Generate Data In-Memory"), generateControls, runGeneratedBtn,
                new Separator(),
                new Label("Option B: read multiple test cases from file"), runFileBtn,
                new Separator(), statusLabel
        );

        return layout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}