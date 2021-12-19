package com.myf.wind.base.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wind-myf
 * @date : 2021/12/10 6:31
 * @desc : 重复数字
 * @version : 1.0
 */
public class MostTimeInArray {
    public static void main(String[] args) {
        int[] arr = {11,2,3,3,3,3,5,7,6,3,6,6,6,8,9,4,1};
        int intArray = findMostFrequentIntArray(arr);
        System.out.println("重复最多的数字intArray = " + intArray);

        System.out.println();

        System.out.println("两两相加等于20的组合数：");
        int[] array = {1,4,6,3,78,23,8,12,2,18,15,5,87,25,10};
        findSum(array,20);
        System.out.println("只出现一次");
        int dup = findDup(array);
        System.out.println("dup = " + dup);
    }

    /**
     * 找出数组中唯一重复的数字
     * @param arr 数组
     * @return int
     */
    public static int findDup(int[] arr){
        int n = arr.length;
        int tmp1 = 0;
        int tmp2 = 0;
        for (int i = 0; i < n - 1; ++i) {
            tmp1 += (i+1);
            tmp2 += arr[i];
        }
        tmp2 += arr[n-1];
        return tmp2 - tmp1;
    }

    /**
     * 求数组中两两相加等于20的组合数
     */
    public static void findSum(int[] arr,int sum){
        Arrays.sort(arr);
        int begin = 0;
        int end = arr.length - 1;
        while (begin < end){
            if (arr[begin] + arr[end]  < sum){
                begin++;
            }else if (arr[begin] + arr[end] > sum){
                end--;
            }else {
                System.out.println(arr[begin] + "," + arr[end]);
                begin++;
                end--;
            }
        }
    }

    /**
     * 找出数组中重复最多的元素
     * 问题描述：
     * 对于数组{1,1,1,2,2,4,4,4,4,5,5,6,6}
     * 找出数组中重复次数最多的数字
     * @param arr 数组
     * @return int
     */
    public static int findMostFrequentIntArray(int[] arr){
        int result = 0;
        int size = arr.length;
        if (size == 0){
            return Integer.MIN_VALUE;
        }
        // 记录每个元素出现的个数
        Map<Integer,Integer> countMap = new HashMap<>();
        for (int item : arr) {
            if (countMap.containsKey(item)) {
                countMap.put(item, countMap.get(item) + 1);
            } else {
                countMap.put(item, 1);
            }
        }

        // 找出出现次数最多的元素
        int most = 0;
        for (Map.Entry<Integer, Integer> next : countMap.entrySet()) {
            Integer key = next.getKey();
            Integer value = next.getValue();
            if (value > most) {
                most = value;
                result = key;
            }
        }
        return result;
    }
}
