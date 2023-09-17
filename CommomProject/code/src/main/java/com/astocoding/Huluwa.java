package com.astocoding;

import java.util.ArrayList;
import java.util.List;

public class Huluwa {


    public static void main(String[] args) {
        List<Integer> hulu = new ArrayList<>();
        hulu.add(3);
        hulu.add(6);
        hulu.add(1);
        hulu.add(7);
        hulu.add(4);
        hulu.add(5);
        hulu.add(2);
        System.out.println(getMaxValue(hulu));
    }


    public static Integer getMaxValue(List<Integer> hulu) {
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < hulu.size(); i++) {
            Integer currentIndexMaxValue = Integer.MIN_VALUE;
            for (int j = 0; j < hulu.size(); j++) {
                if (i == j) {
                    continue;
                }
                Integer current = Math.min(hulu.get(i), hulu.get(j)) * Math.abs(i - j);
                if (current > currentIndexMaxValue) {
                    currentIndexMaxValue = current;
                }
            }
            if (currentIndexMaxValue > maxValue) {
                maxValue = currentIndexMaxValue;
            }
        }
        return maxValue;
    }
}
