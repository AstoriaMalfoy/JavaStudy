package com.astocoding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

    /**
     * 给定一个含不同整数的集合，返回其所有的子集（任意顺序）。
     * <p>
     * input : [1,2,3]
     * output : [], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]
     */
    public static void main(String[] args) {
        List<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(2);
        input.add(3);
        input.add(4);
        System.out.println(getSubList(input));
        getSubList(input);
    }

    public static List<List<Integer>> getSubList(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        if (list.size() == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        List<List<Integer>> subListResult = getSubList(list.subList(0, list.size() - 1));

        for (List<Integer> subList : subListResult) {
            result.add(subList);
            List<Integer> containCurrentValueSubList = new ArrayList<>(subList);
            containCurrentValueSubList.add(list.get(list.size() - 1));
            result.add(containCurrentValueSubList);
        }
        return result;
    }
}