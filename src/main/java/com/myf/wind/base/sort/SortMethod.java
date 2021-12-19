package com.myf.wind.base.sort;
/**
 * @author : wind-myf
 * @date : 2021/9/5 19:50
 * @desc : 排序
 * @version : 1.0
 */
public class SortMethod {
    public static void main(String[] args) {
        int[] a = {10,1,24,33,22,2,84,56,3,6,6};
//        // 选择排序
//        selectSort(a);
//        System.out.println("选择排序");
//        for (int i = 0; i < a.length; i++) {
//            System.out.println("第" + i + "个元素为：" + a[i]);
//        }

//        System.out.println();
//        // 插入排序
//        insertSort(a);
//        System.out.println("插入排序");
//        for (int i = 0; i < a.length; i++) {
//            System.out.println("第" + i + "个元素为：" + a[i]);
//        }
//
//        System.out.println();
//        // 冒泡排序
//        bubbleSort(a);
//        System.out.println("冒泡排序");
//        for (int i = 0; i < a.length; i++) {
//            System.out.println("第" + i + "个元素为：" + a[i]);
//        }

//        System.out.println();
//        // 归并排序
//        mergeSort(a,0,a.length-1);
//        System.out.println("归并排序");
//        for (int i = 0; i < a.length; i++) {
//            System.out.println("第" + i + "个元素为：" + a[i]);
//        }

        System.out.println();
        // 堆排序
        myMinHeapSort(a);
        System.out.println("堆排序");
        for (int i = 0; i < a.length; i++) {
            System.out.println("第" + i + "个元素为：" + a[i]);
        }

    }

    /**
     * 选择排序
     *  * 对于一组给定的记录，经过第一次比较后得到最小的记录
     *  * 然后将该组记录与第一个记录的位置进行交换；
     *  * 接着对不包括第一个记录以外的的其他记录进行第二轮比较，得到最小的记录并与第二个记录进行位置交换；
     *  * 重复该过程，直到进行比较的记录只有一个为止
     * @param a 需要排序的数据
     */
    public static void selectSort(int[] a){
        for (int i = 0; i < a.length ; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[i]){
                    int temp = a[j];
                    a[j] = a[i];
                    a[i] = temp;
                }
            }
        }
    }

    /**
     * 插入排序
     * 对于一组给定的记录，初始时假定第一个记录自成一组有序序列，其余记录为无序序列
     * 接着从第二个记录开始，按照记录的大小依次将当前处理的记录插入到其之前的有序序列中
     * 直至最后一个记录插入到有序序列中为止
     * @param a 需排序的元素
     */
    public static void insertSort(int[] a){
        for (int i = 1;i < a.length;i++){
            int temp = a[i];
            int j = i;
            if (a[j-1] > temp){
                while (j >= 1 && a[j-1] > temp){
                    a[j] = a[j-1];
                    j--;
                }
            }
            a[j] = temp;
        }

    }

    /**
     * 冒泡排序
     * 对于给定的n个记录
     * 从第一个记录开始依次对相邻的两个记录进行比较
     * 当前记录大于后面的记录时，交换位置
     * 进行一轮比较和换位后，n个记录中的最大记录将位于第n位
     * 然后对前n-1个记录进行第二轮比较
     * 重复该过程直到比较记录只剩下一个为止
     * @param a 排序元素
     */
    public static void bubbleSort(int[] a){
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = a.length -1; j > i ; j--) {
                if (a[j] < a[j-1]){
                    int temp = a[j];
                    a[j] = a[j-1];
                    a[j-1] = temp;
                }
            }
        }
    }

    /**
     * 归并排序
     * 利用递归和分治技术将数据序列划分成越来越小的半子表
     * 再对半子表排序
     * 最后再用递归方法将排好序的半子表合成越来越大的有序序列
     * 原理如下：
     * 对于给定的一组记录（n个记录），
     * 首先将每两个相邻的长度为1的子序列进行归并，得到n/2（向上取整）个长度为2或1的有序子序列
     * 再将其两两归并
     * 反复执行此过程，直到得到一个有序序列
     * @param a 排序数组
     * @param p 初始
     * @param q 中间
     * @param r 尾部
     */
    public static void merge(int[] a,int p,int q,int r){
        int i,j,k,n1,n2;
        // 中间 - 初始 + 1
        n1 = q - p + 1;
        // 结尾 - 中间
        n2 = r -q;
        // 左
        int[] L = new int[n1];
        // 右
        int[] R = new int[n2];
        // 左子序列元素
        for (i = 0,k = p; i < n1; i++,k++) {
            L[i] = a[k];
        }
        // 右子序列元素
        for (i = 0,k = q + 1; i < n2; i++,k++) {
            R[i] =  a[k];
        }

        for (k = p,i = 0,j = 0; i < n1 && j < n2; k++) {
            if (L[i] > R[j]){
                a[k] = L[i];
                i++;
            }else {
                a[k] =R[j];
                j++;
            }
        }

        if (i < n1){
            for (j = i; j < n1; j++,k++) {
                a[k] =  L[j];
            }
        }

        if (j < n2){
            for (i = j; i < n2; i++,k++) {
                a[k] = R[i];
            }
        }
    }

    public static void mergeSort(int[] a,int p,int r){
        if (p < r){
            int q = (p + r)/2;
            mergeSort(a,p,q);
            mergeSort(a,q+1,r);
            merge(a,p,q,r);
        }
    }

    /**
     * 对于一组给定的记录
     * 通过一趟排序后将原序列分为两部分
     * 其中前一部分的所有记录均比后一部分的所有记录小
     * 然后再依次对前后两部分的记录进行快速排序
     * 递归该过程，直到该序列中的所欲记录有序为止
     * @param a
     * @param low
     * @param high
     */
    public static void quitSort(int[] a,int low,int high){
        if (low >= high){
            return;
        }
        int i = low;
        int j = high;
        int index = a[i];
        while (i < j){
            while (i < j && a[j] >= index){
                j--;
            }
            if (i < j){
                a[i++] = a[j];
            }
            while (i < j && a[i] < index){
                i++;
            }
            if (i < j){
                a[j--] = a[i];
            }
        }
        a[i] = index;
        quitSort(a,low,i-1);
        quitSort(a,i+1,high);


    }


    /**
     * 希尔排序（缩小增量排序）
     * 原理如下：
     * 先将排序的数组元素分成多个子序列，使每个子序列的元素个数相对较少
     * 然后对各个子序列分别进行直接插入排序
     * 待整个排序序列“基本有序后”
     * 最后再对所有元素进行一次直接插入排序
     * @param a 排序数组
     */
    public static void shellSort(int[] a){
        int length = a.length;
        int i,j,h,temp;
        for (h = length/2;h > 0;h=h/2){
            for (i = h; i < length; i++) {
                temp = a[i];
                for (j = i-h;j >= 0 ; j -= h) {
                    if (temp < a[j]){
                        a[j + h] = a[j];
                    }else {
                        break;
                    }
                }
                a[j + h] = temp;
            }
        }
    }

    /**
     * 构建堆
     * @param a
     * @param pos
     * @param len
     */
    public static void adjustMinHeap(int[] a,int pos,int len){
        int temp;
        int child;
        for (temp = a[pos];2 * pos + 1 <= len;pos = child){
            child = 2 * pos + 1;
            if (child < len && a[child] > a[child+1]){
                child++;
            }
            if (a[child] < temp){
                a[pos] = a[child];
            }else {
                break;
            }
        }
        a[pos] = temp;
    }

    /**
     * 堆排序：是一种树形选择排序，在排序过程中，将R[1 …… n]看作是一棵完全二叉树的顺序存储结构，
     * 利用完全二叉树中父节点和子节点之间的内在关系来选择最小的元素。
     *
     * 堆一般分为大堆顶和小堆顶两种不同的类型。
     * 对于给定的n个记录的序列（r(1),r(2）--- r(n)），当且仅当满足条件（r(i) >= r(2i)且r(i) >= r(2i+1),i=1,2---n）时称为大堆顶，
     * 此时，堆顶元素为最大值。
     *
     * 对于给定的n个记录的序列（r(1),r(2）--- r(n)），当且仅当满足条件（r(i) <= r(2i)且r(i) <= r(2i+1),i=1,2---n）时称为小堆顶，
     * 此时，堆顶元素必为最小值。
     *
     * 堆排序的思想：
     * 对于给定的N个记录，初始时把这些记录看作一棵顺序存储的二叉树，然后将其调整为一个大堆顶，
     * 然后将堆的最后一个元素和堆顶元素（即二叉树的根节点）进行交换后，堆的最后一个元素即为最大记录。
     * 接着将前n-1个元素（即不包括最大记录）重新调整为一个大堆顶，再讲堆顶元素与当前堆的最后一个元素进行交换后得到次打=大的记录，
     * 重复该过程直到调整的堆中只剩一个元素为止，该元素即为最小记录，此时可得到一个有序序列。
     *
     */
    public static void myMinHeapSort(int[] a){
        int i;
        int len = a.length;
        for (i = len/2 -1;  i >= 0 ; i--) {
            adjustMinHeap(a,i,len-1);
        }
        for (i = len-1; i >= 0; i--) {
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;
            adjustMinHeap(a,0,i-1);
        }
    }

}
