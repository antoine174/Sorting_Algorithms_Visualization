package com.antoine.lab1.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArrayGenerate {

    public static List<Integer> GENrandom(int size) {
        List<Integer> list = new ArrayList<>(size);
        Random rand = new Random();
        for (int i=0; i<size; i++) {
            list.add(rand.nextInt(10000));
        }
        return list;
    }

    public static List<Integer> SortedArr(int size) {
        List<Integer> list = GENrandom(size);
        Collections.sort(list);
        return list;
    }

    public static List<Integer> reverselySorted(int size) {
        List<Integer> list=SortedArr(size);
        Collections.reverse(list);
        return list;
    }

    public static List<List<Integer>> readMultipleTESTCASES(String filepath) throws IOException {
        List<List<Integer>> allcases = new ArrayList<>();// n2ra 2l test cases mn 2lfiles
        try (BufferedReader b=new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line =b.readLine())!= null) {
                if (line.trim().isEmpty()) continue;
                String[] values = line.split(",");
                List<Integer> single = new ArrayList<>();
                for (String val:values) {
                    single.add(Integer.parseInt(val.trim()));
                }
                allcases.add(single);
            }
        }
        return allcases;
    }
}