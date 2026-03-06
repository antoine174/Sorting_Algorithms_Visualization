    package com.antoine.lab1.algorithms;
    import java.util.List;

    public class HeapSort extends AbstractSort {

        @Override
        public void sort(List<Integer> list) {
            resetMetrics();
            int sz = list.size();
            for (int i = sz / 2 - 1; i >= 0; i--) {
                max_heapify(list, i, sz);
            }
            for (int i = sz - 1; i > 0; i--) {
                swap(list, 0, i);
                max_heapify(list, 0, i);
            }
        }

        private void max_heapify(List<Integer> list, int node, int sz) {
            int largest=node;
            int l =2*node+1;
            int r =2*node+2;

            incrementComparisons();
            if (l<sz && list.get(l)>list.get(largest)) {
                largest = l;
            }

            incrementComparisons();
            if (r<sz && list.get(r)>list.get(largest)) {
                largest=r;
            }

            if (largest!=node) {
                swap(list,node, largest);
                max_heapify(list, largest, sz);
            }
        }

        private void swap(List<Integer> list, int i, int j) {
            if (i != j) {
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                incrementInterchanges();
            }
        }
    }