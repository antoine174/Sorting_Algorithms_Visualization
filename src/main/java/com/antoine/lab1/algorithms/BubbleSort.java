package com.antoine.lab1.algorithms;

import java.util.List;

public class BubbleSort extends AbstractSort {

    @Override
    public void sort(List<Integer> list) {
        resetMetrics();
        int sz = list.size();
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < sz - 1; i++) {
                incrementComparisons();
                if (list.get(i) > list.get(i + 1)) {
                    int temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    swapped = true;
                    incrementInterchanges();
                }
            }
            sz--;
        }
    }
}