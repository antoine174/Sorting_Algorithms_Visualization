package com.antoine.lab1.algorithms;

import java.util.List;

public class SelectionSort extends AbstractSort {

    @Override
    public void sort(List<Integer> list) {
        resetMetrics();
        int n = list.size();
            for(int i=0; i<n-1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                incrementComparisons();
                if (list.get(j) < list.get(minIdx)) {
                    minIdx = j;
                }
            }
            int temp = list.get(minIdx);
            list.set(minIdx, list.get(i));
            list.set(i, temp);

            incrementInterchanges();
        }
    }
}