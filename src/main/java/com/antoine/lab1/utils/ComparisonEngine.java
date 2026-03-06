package com.antoine.lab1.utils;

import com.antoine.lab1.algorithms.AbstractSort;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparisonEngine {

    public static class ResultRow {
        public String algoName;
        public int arraySize;
        public String generationMode;
        public int runs;
        public double avgTimeMs;
        public double minTimeMs;
        public double maxTimeMs;
        public long comparisons;
        public long interchanges;
    }

    public static ResultRow runBenchmark(AbstractSort algorithm, List<Integer> originalData, String modeName, int runs) {
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;
        double totalTime = 0;
        long comparisons = 0;
        long interchanges = 0;

        for (int i=0; i<runs; i++) {
            List<Integer> dataCopy=new ArrayList<>(originalData);

           algorithm.setupVisualization(0, null);
            algorithm.resetMetrics();

            long startTime = System.nanoTime();
            algorithm.sort(dataCopy);
            long endTime = System.nanoTime();

            double durationMs = (endTime-startTime) / 1000000.0;//3l4an n5leh milli

            if (durationMs<minTime){
                minTime=durationMs;
            }
            if (durationMs>maxTime) {
                maxTime=durationMs;
            }
            totalTime+=durationMs;

            comparisons =algorithm.getComparisons();
            interchanges =algorithm.getInterchanges();
        }

        ResultRow row =new ResultRow();
        row.algoName =algorithm.getClass().getSimpleName();// 2sm 2l class
        row.arraySize =originalData.size();
        row.generationMode =modeName;
        row.runs=runs;
        row.avgTimeMs=totalTime / runs;
        row.minTimeMs=minTime;
        row.maxTimeMs=maxTime;
        row.comparisons=comparisons;
        row.interchanges=interchanges;

        return row;
    }

    public static void exportToCsv(List<ResultRow> results, String filepath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            pw.println("Algorithm,Array Size,Generation Mode,Runs,Avg Time (ms),Min Time (ms),Max Time (ms),Comparisons,Interchanges");
            for (ResultRow row : results) {
                pw.printf("%s,%d,%s,%d,%.4f,%.4f,%.4f,%d,%d\n",
                        row.algoName, row.arraySize, row.generationMode,
                        row.runs, row.avgTimeMs, row.minTimeMs, row.maxTimeMs,
                        row.comparisons, row.interchanges);
            }

           pw.println(); // str fady
            pw.println("final conc (STATISTICS PER ALGORITHM)");
            pw.println("Algorithm,Total Cases Tested,Generation Mode,-,Overall Avg Time,Absolute Min Time,Absolute Max Time,Avg Comparisons,Avg Interchanges");

            Map<String, List<ResultRow>> groupedByAlgo = new HashMap<>();
            for (ResultRow r : results) {
                groupedByAlgo.computeIfAbsent(r.algoName, k -> new ArrayList<>()).add(r);
            }

            String[] algorithmOrder = {"BubbleSort", "SelectionSort", "InsertionSort", "MergeSort", "QuickSort", "HeapSort"};

            for (String alg : algorithmOrder) {
                List<ResultRow> algoResults = groupedByAlgo.get(alg);
                if (algoResults == null || algoResults.isEmpty()) continue;

                double absoluteMin = Double.MAX_VALUE;
                double absoluteMax = Double.MIN_VALUE;
                double sumAvgTime = 0;
                long sumComparisons = 0;
                long sumInterchanges = 0;

                for (ResultRow r : algoResults) {
                    if (r.minTimeMs < absoluteMin) absoluteMin = r.minTimeMs;
                    if (r.maxTimeMs > absoluteMax) absoluteMax = r.maxTimeMs;
                    sumAvgTime +=r.avgTimeMs;
                    sumComparisons +=r.comparisons;
                    sumInterchanges +=r.interchanges;
                }

                int count = algoResults.size();

                pw.printf("%s SUMMARY,%d,ALL ROWS COMBINED,-,%.4f,%.4f,%.4f,%d,%d\n",
                        alg, count,(sumAvgTime/count),absoluteMin,absoluteMax,
                        (sumComparisons/count), (sumInterchanges/count));
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}