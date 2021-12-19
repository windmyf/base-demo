package com.myf.wind.base.array;
/**
 * @author : wind-myf
 * @date : 2021/9/5 22:31
 * @desc : 寻找数组中的最大值与最小值
 * @version : 1.0
 */
public class MaxMin {

    static int max;
    static int min;

    /**
     * 获取数组最大值和最小值
     * @param arr 数组
     */
    public static void getMaxAndMin(int[] arr){
        max = arr[0];
        min = arr[0];
        int len = arr.length;
        for (int i = 1; i < len-1; i = i+2) {
            if (i+1 > len){
                if (arr[i] > max){
                    max = arr[i];
                }
                if (arr[i] < min){
                    min = arr[i];
                }
            }
            if (arr[i] > arr[i+1]){
                if (arr[i] > max){
                    max = arr[i];
                }
                if (arr[i] < min){
                    min = arr[i=1];
                }
            }
            if (arr[i] < arr[i+1]){
                if (arr[i+1] > max){
                    max = arr[i+1];
                }
            }if (arr[i] < min){
                min = arr[i];
            }
        }

    }

    /**
     * 求最大子数组之和
     * 问题描述：
     * 有n元素的数组，这n个元素可以试正数也可以是负数，数组中连续的一个或多个可以组成一个连续的子数组
     * 一个数组可能有多个这种连续的子数组，求子数组和的最大值
     * @param arr 数组
     * @return int
     */
    public static int maxSubArray1(int[] arr){
        int n = arr.length;
        int thisSum,maxSum = 0;
        int i,j,k;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                thisSum = 0;
                for (k = 0; k < j; k++) {
                    thisSum += arr[k];
                }
                if (thisSum > maxSum){
                    maxSum = thisSum;
                }

            }
        }
        return maxSum;
    }

    /**
     * 重复利用已经计算的子数组和
     * @param arr 数组
     */
    public static int maxSubArray2(int[] arr){
        int size = arr.length;
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            int sum = 0;
            for (int value : arr) {
                sum += value;
                if (sum > maxSum) {
                    maxSum = sum;
                }
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[] arr = {34,65,-34,12,-67,89,-2,6,-45,34};
        int i = maxSubArray1(arr);
        System.out.println("method1 最大连续数组和：" + i);
        int maxSubArray2 = maxSubArray2(arr);
        System.out.println("method2 最大连续数组和：" + maxSubArray2);
        getMaxAndMin(arr);
        System.out.println("max = " + max);
        System.out.println("min = " + min);
    }

}
