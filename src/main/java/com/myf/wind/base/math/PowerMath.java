package com.myf.wind.base.math;
/**
 * @author : wind-myf
 * @date : 2021/9/5 22:03
 * @desc : 位运算
 * @version : 1.0
 */
public class PowerMath {
    public static void main(String[] args) {
        int powerN = powerN(2, 5);
        System.out.println("powerN = " + powerN);

        boolean power = isPower(128);
        System.out.println("isPower=" + power);

        boolean power2 = isPower2(754);
        System.out.println("power2 = " + power2);

        System.out.println(countOne(7));
        System.out.println(countOne(8));

        System.out.println(countOne2(7));
        System.out.println(countOne2(8));
    }

    /**
     * 如何用位移运算实现乘法运算
     *
     * 把一个数向左移动n位相当于把该数乘以2的n次方
     *
     * m乘以2的n次方
     */
    public static int powerN(int m,int n){
        return m << n;
    }

    /**
     * 如何判断一个数是否为2的n次方
     * 方法1：用1做位移操作，判断位移后的值是否与给定的数相等
     */
    public static boolean isPower(int n){
        if (n < 1){
            return false;
        }

        int i = 1;
        while (i <= n){
            if (i == n){
                return true;
            }
            i <<= 1;
        }
        return false;
    }

    /**
     * 如何判断一个数是否为2的n次方
     * 方法2：如果一个数是2的n次方，则二进制表示中只有一位为1
     */
    public static boolean isPower2(int n){
        if (n < 1){
            return false;
        }
        int m = n&(n-1);

        return m == 0;
    }

    /**
     * 求二进制数中1的个数
     * 思路1：
     * 判断最后一位是否为1，如果为1，则计数器加1，右移丢弃最后一位
     * 循环执行
     */
    public static int countOne(int n){
        System.out.println(Integer.toBinaryString(n));
        int count = 0;
        while (n > 0){
            if ((n&1) == 1){
                count ++;
            }
            n >>= 1;
        }

        return count;
    }

    /**
     * 求二进制数中1的个数求二进制数中1的个数
     * 思路2：
     * 该方法复杂度O(m)，m为二兼职数中1的个数
     */
    public static int countOne2(int n){
        System.out.println(Integer.toBinaryString(n));
        int count = 0;
        while (n != 0){
            n = n&(n-1);
            count ++;
        }
        return count;
    }
}
