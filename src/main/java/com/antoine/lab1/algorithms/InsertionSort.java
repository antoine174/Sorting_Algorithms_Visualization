package com.antoine.lab1.algorithms;

import java.util.List;

public class InsertionSort extends AbstractSort{

    @Override
    public void sort(List<Integer> list) {
        resetMetrics();
        int sz = list.size();
        for(int i=1;i<sz;i++) {
            int key = list.get(i);
            int j = i-1;
            while(j>=0) {
                incrementComparisons();
                if (key<list.get(j)) {
                    list.set(j + 1, list.get(j));
                    incrementInterchanges();
                    j--;
                }
                else {
                    break;
                }
            }
            if (j!=i-1){
                list.set(j+1, key);
                incrementInterchanges();
            }
        }
    }
}