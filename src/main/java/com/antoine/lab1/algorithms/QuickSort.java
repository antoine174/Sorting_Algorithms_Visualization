package com.antoine.lab1.algorithms;

import java.util.List;

public class QuickSort extends AbstractSort {

    @Override
    public void sort(List<Integer> list) {
        resetMetrics();
        if (list != null && !list.isEmpty()) {
            recursionSorting(list, 0, list.size() - 1);
        }
    }

    private void recursionSorting(List<Integer> list, int l, int r) {
        if (l >= r) return;

        int pivotIndex = pivoting(list, l, r);

        recursionSorting(list,l, pivotIndex-1);
        recursionSorting(list, pivotIndex+1,r);
    }

    private int pivoting(List<Integer> list, int l, int r) {
        int randomPivot = l + (int)(Math.random() * (r - l + 1));
        swap(list, l,randomPivot);

        int i = l;
        int pivotValue = list.get(l);

        for (int j =l+1; j<=r; j++) {
            incrementComparisons();
            if (list.get(j) < pivotValue) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list,l, i);
        return i;
    }

    private void swap(List<Integer> list, int i, int j) {
        if (i!=j) {
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);

            incrementInterchanges();
        }
    }
}