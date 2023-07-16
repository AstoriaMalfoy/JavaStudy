package com.astocoding.sort;

import java.util.Arrays;

public class SortAlg {
    /**
     * 冒泡排序
     *
     * @param arrays
     */
    public static void bufferSort(int[] arrays) {
        for (int i = 0; i < arrays.length; i++) {
            for (int j = 0; j < arrays.length - 1; j++) {
                if (arrays[j] > arrays[j + 1]) {
                    arrays[j] = arrays[j] + arrays[j + 1];
                    arrays[j + 1] = arrays[j] - arrays[j + 1];
                    arrays[j] = arrays[j] - arrays[j + 1];
                }
            }
        }
    }

    /**
     * 选择排序
     *
     * @param args
     */
    public static void selectSort(int[] args) {
        for (int i = 0; i < args.length; i++) {
            int minValue = Integer.MAX_VALUE, minValueIndex = -1;
            for (int j = i; j < args.length; j++) {
                if (args[j] < minValue) {
                    minValue = args[j];
                    minValueIndex = j;
                }
            }
            args[minValueIndex] = args[i];
            args[i] = minValue;

        }
    }

    /**
     * 插入排序
     *
     * @param arrays
     */
    public static void insertSort(int[] arrays) {
        for (int i = 0; i < arrays.length; i++) {
            int temp = arrays[i];
            int flag = i - 1;
            while (flag >= 0 && arrays[flag] > temp) {
                arrays[flag + 1] = arrays[flag];
                arrays[flag] = temp;
                flag--;
            }
        }
    }


    /**
     * 希尔排序
     */
    // 定义初始间隔
    private static final int CAPE = 5;

    public static void shellSort(int[] arrays) {
        int atomCape = CAPE;
        while (atomCape > 0) {
            for (int i = 0; i < arrays.length; i += atomCape) {
                int minValue = Integer.MAX_VALUE, minValueIndex = -1;
                for (int j = i; j < arrays.length; j += atomCape) {
                    if (arrays[j] < minValue) {
                        minValue = arrays[j];
                        minValueIndex = j;
                    }
                }
                arrays[minValueIndex] = arrays[i];
                arrays[i] = minValue;
            }
// 增量递减
            atomCape--;
        }
    }

    /**
     * 归并排序
     *
     * @param args
     */
    public static void mergeSort(int[] args) {
        mergeSort(args, 0, args.length);
    }

    // 左闭右开
    public static void mergeSort(int[] rags, int start, int end) {
        // 区间临界条件
        if (start + 1 >= end) {
            return;
        }
        int middle = (start + end) / 2;
        mergeSort(rags, start, middle);
        mergeSort(rags, middle, end);
        merge(rags, start, middle, middle, end);
    }

    public static void merge(int[] args, int leftStart, int leftEnd, int rightStart, int rightEnd) {
        System.out.println("marge [" + leftStart + "," + leftEnd + "] to [" + rightStart + "," + rightEnd + "] " +
                "\nbefore : " + Arrays.toString(args));
        int curse = leftStart;
        int[] leftTemp = new int[leftEnd - leftStart];
        for (int i = leftStart; i < leftEnd; i++) {
            leftTemp[i - leftStart] = args[i];
        }

        leftEnd -= leftStart;
        leftStart = 0;

        while (leftStart < leftEnd || rightStart < rightEnd) {
            if (leftStart < leftEnd && rightStart < rightEnd) {
                if (args[rightStart] < leftTemp[leftStart]) {
                    args[curse] = args[rightStart];
                    rightStart++;
                    curse++;
                } else {
                    args[curse] = leftTemp[leftStart];
                    leftStart++;
                    curse++;
                }
            } else {
                if (leftEnd <= leftStart) {
                    args[curse] = args[rightStart];
                    rightStart++;
                    curse++;
                } else {
                    args[curse] = leftTemp[leftStart];
                    leftStart++;
                    curse++;
                }
            }
        }
        System.out.println("after : " + Arrays.toString(args));
    }


    /**
     * 快速排序
     *
     * @param args
     */
    public static void quickSort(int[] args) {
        quickSort(args, 0, args.length);
    }

    public static void quickSort(int[] args, int start, int end) {
        if (start + 1 >= end) {
            return;
        }
        int tempValue = args[start];
        int middlePoint = start + 1, currentPoint = start + 1;
        while (currentPoint < end) {
            if (args[currentPoint] < tempValue) {
                swap(args, middlePoint, currentPoint);
                middlePoint++;
            }
            currentPoint++;
        }

        swap(args, middlePoint - 1, start);
        quickSort(args, start, middlePoint);
        quickSort(args, middlePoint, end);
    }

    public static void swap(int[] args, int swapA, int swapB) {
        if (swapA == swapB) {
            return;
        }
        args[swapA] += args[swapB];
        args[swapB] = args[swapA] - args[swapB];
        args[swapA] = args[swapA] - args[swapB];
    }


    /**
     * 堆排序
     *
     * @param args
     */
    public static void heapSort(int[] args) {
        int length = args.length;
        while (length > 0) {
            buildHead(args, length);
            swapPoint(args, 0, --length);
        }
    }

    public static void buildHead(int[] args, int length) {
        int position = length / 2;
        while (position >= 0) {
            maintainHeap(args, position, length);
            position--;
        }
    }

    public static void maintainHeap(int[] args, int point, int length) {
        if (2 * (point + 1) - 1 >= length) {
            return;
        }
        int leftPoint, rihtPoint;
        boolean containRight = (rihtPoint = 2 * (point + 1)) < length;
        leftPoint = rihtPoint - 1;
        if (containRight && !(args[leftPoint] < args[point] && args[rihtPoint] < args[point])) {
            if (args[leftPoint] < args[rihtPoint]) {
                swapPoint(args, rihtPoint, point);
                maintainHeap(args, rihtPoint, length);
            } else {
                swapPoint(args, leftPoint, point);
                maintainHeap(args, leftPoint, length);
            }
        } else {
            if (args[point] < args[leftPoint]) {
                swapPoint(args, point, leftPoint);
            }
        }
    }

    private static void swapPoint(int[] args, int swapA, int swapB) {
        if (swapA == swapB) {
            return;
        }
        if (swapA > args.length || swapB > args.length) {
            return;
        }
        args[swapA] += args[swapB];
        args[swapB] = args[swapA] - args[swapB];
        args[swapA] = args[swapA] - args[swapB];
    }

    /**
     * 计数排序
     *
     * @param args
     */
    public static void countSort(int[] args) {
        int maxValue = getMaxValue(args);
        int[] tempArgs = new int[maxValue + 1];
        for (int arg : args) {
            tempArgs[arg]++;
        }
        int point = 0;
        for (int i = 0; i <= maxValue; i++) {
            while (tempArgs[i] > 0) {
                args[point] = i;
                point++;
                tempArgs[i]--;
            }
        }
    }

    public static int getMaxValue(int[] args) {
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < args.length; i++) {
            if (args[i] > maxValue) {
                maxValue = args[i];
            }
        }
        return maxValue;
    }


    /**
     * 桶排序
     */
    private static final Integer bucketSize = 10;

    public static void bucketSort(int[] args) {
        args = bucketSort(args, bucketSize);
    }

    public static int[] bucketSort(int[] args, int bucketSize) {
        int[][] buckets = new int[bucketSize][0];
        int minValue = Integer.MAX_VALUE, maxValue = Integer.MIN_VALUE;
        for (int arg : args) {
            if (arg < minValue) {
                minValue = arg;
            }
            if (maxValue < arg) {
                maxValue = arg;
            }
        }
        for (int arg : args) {
            int bucketInext = getBucketInext(minValue, maxValue, bucketSize, arg);
            buckets[bucketInext] = arrAppent(buckets[bucketInext], arg);
        }
        for (int[] bucket : buckets) {
            quickSort(bucket);
        }
        int[] merge = merge(buckets, args);
        return merge;
    }

    private static int[] merge(int[][] buckets, int[] args) {
        int[] result = new int[args.length];
        int flat = 0;
        for (int[] bucket : buckets) {
            for (int i : bucket) {
                args[flat] = i;
                flat++;
            }
        }
        return result;
    }

    /**
     * array自动扩容
     *
     * @param arrays
     * @param value
     * @return
     */
    public static int[] arrAppent(int[] arrays, int value) {
        arrays = Arrays.copyOf(arrays, arrays.length + 1);
        arrays[arrays.length - 1] = value;
        return arrays;
    }

    /**
     * 散列函数
     *
     * @param minValue
     * @param maxValue
     * @param bucketSize
     * @param value
     * @return
     */
    public static int getBucketInext(int minValue, int maxValue, int bucketSize, int value) {
        int result = 0;
        int step = ((maxValue - minValue) / bucketSize) + 1;
        while (minValue + step < value) {
            result++;
            minValue += step;
        }
        return result;

    }

    
}
