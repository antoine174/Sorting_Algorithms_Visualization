package com.antoine.lab1.utils;

import com.antoine.lab1.algorithms.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchTestRunner {

    private static List<AbstractSort> AllALg() {
        return Arrays.asList(new BubbleSort(),new SelectionSort(),new InsertionSort(),
                new MergeSort(),new QuickSort(),new HeapSort());
    }

    public static void testGenerted(List<Integer> data, String mode, int runs, String outputPath) {
        List<ComparisonEngine.ResultRow> results = new ArrayList<>();

        for (AbstractSort algo : AllALg()) {
            results.add(ComparisonEngine.runBenchmark(algo, data, mode, runs));
        }
        ComparisonEngine.exportToCsv(results, outputPath);
    }

    public static void TestFile(String filePath, int runs, String outputPath) {
        List<ComparisonEngine.ResultRow> results = new ArrayList<>();
        try {
            List<List<Integer>> allTestCases = ArrayGenerate.readMultipleTESTCASES(filePath);
            String fileName = new File(filePath).getName();

            int rowNum = 1;
            for (List<Integer> testCase : allTestCases) {
                String modeLabel = fileName + " (Row " + rowNum + ")";
                System.out.println("Running benchmark for " + modeLabel + " size: " + testCase.size());

                for (AbstractSort algo : AllALg()) {
                    results.add(ComparisonEngine.runBenchmark(algo, testCase, modeLabel, runs));
                }
                rowNum++;
            }
            ComparisonEngine.exportToCsv(results, outputPath);
            System.out.println("Exported to " + outputPath);
        } catch (Exception e) {
            System.err.println("Error reading or processing file: " + e.getMessage());
        }
    }
}