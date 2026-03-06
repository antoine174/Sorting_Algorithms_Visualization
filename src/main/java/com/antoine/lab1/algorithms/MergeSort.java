package com.antoine.lab1.algorithms;

import java.util.ArrayList;
import java.util.List;

public class MergeSort extends AbstractSort {

    @Override
    public void sort(List<Integer> list) {
        resetMetrics();
        if (list != null && !list.isEmpty()) {
            merge(list, 0, list.size() - 1);
        }
    }

    private List<Integer> merge(List<Integer> mainList, int L, int R) {
        if(L==R) {
            List<Integer> base = new ArrayList<>();
            base.add(mainList.get(L));
            return base;
        }
        int split = L + (R - L) / 2;
        List<Integer> left = merge(mainList, L, split);
        List<Integer> right = merge(mainList, split + 1, R);

        List<Integer> res = new ArrayList<>();
        int i=0, j=0;

        while (i<left.size() && j<right.size()) {
            incrementComparisons();
            if (left.get(i) <= right.get(j)) {
                res.add(left.get(i++));
            } else {
                res.add(right.get(j++));
            }
        }
        while (i<left.size()) {
            res.add(left.get(i++));
        }
        while (j<right.size()) {
            res.add(right.get(j++));
        }

        for (int k=0; k<res.size();k++) {
            mainList.set(L+k,res.get(k));
            incrementInterchanges();
        }
        return res;
    }
}