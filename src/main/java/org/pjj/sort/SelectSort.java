package org.pjj.sort;

import java.util.Arrays;

/**
 * 复习选择排序
 *
 * 选择排序的思路就是, 10个元素, 遍历9轮, 每轮找出其中的最小值, 放在数组下标0的地方(依次往后放0,1,2,...), 直到找出9个最小值放前面, 剩下的最大值自然就在数组最后面了.
 * @author PengJiaJun
 * @Date 2022/08/06 23:03
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {5,7,6,9,3,10,2,11};
        select3(arr);
        System.out.println(Arrays.toString(arr));//[2, 3, 5, 6, 7, 9, 10, 11]
    }

    public static void select(int[] arr) {
        for(int i=0; i < arr.length - 1; i++) {//假设10个元素,则实际排序9轮, 也就是说9个元素都有序了, 则最后一个元素自然是有序的了, 所以 -1
            int min = arr[i];
            int jIndex = 0;
            boolean flag = false;

            // j = i是因为 i表示第几轮排序, 第几轮排序表示排好了几个元素, i = 0表示一个元素都没排好序则从 arr[0] 开始选出最小值
            // i = 5表示已经有5个元素排好序了, 则从 arr[5]开始选出最小值.
            for(int j = i; j < arr.length; j++) {
                if(arr[j] < min) {//选出最小值
                   min = arr[j];//保存最小值
                   jIndex = j;//保存最小值的下标

                   flag = true;//需要进行交换
                }
            }

            //如果没有flag, 那么arr[j]就是最小值呢, 那么其实不需要交换, 但是这里还是执行了 min,Jindex就是默认值, 这不是坏了事了
            //交换, 将最小值放在数组前面,
            if(flag) {
                int temp = arr[i];
                arr[i] = min;
                arr[jIndex] = temp;
            }
        }
    }


    /**
     * 发现select()中的flag变量其实不需要, jIndex完全可以起到它本身的作用外, 起到flag的作用
     */
    public static void select2(int[] arr) {
        for(int i=0; i < arr.length - 1; i++) {//假设10个元素,则实际排序9轮, 也就是说9个元素都有序了, 则最后一个元素自然是有序的了, 所以 -1
            int min = arr[i];
            int jIndex = -1;

            // j = i是因为 i表示第几轮排序, 第几轮排序表示排好了几个元素, i = 0表示一个元素都没排好序则从 arr[0] 开始选出最小值
            // i = 5表示已经有5个元素排好序了, 则从 arr[5]开始选出最小值.
            for(int j = i+1; j < arr.length; j++) {
                if(arr[j] < min) {//选出最小值
                    min = arr[j];//保存最小值
                    jIndex = j;//保存最小值的下标
                }
            }

            //jIndex != -1 说明需要交换
            //交换, 将最小值放在数组前面,
            if(jIndex != -1) {
                int temp = arr[i];
                arr[i] = min;
                arr[jIndex] = temp;
            }
        }
    }


    public static void select3(int[] arr) {
        for(int i=0; i < arr.length; i++) {
            int min = arr[i];
            int jIndex = -1;
            for(int j=i+1; j < arr.length; j++) {

                if(min > arr[j]) {
                    min = arr[j];
                    jIndex = j;
                }
            }

            if(jIndex != -1) {
                int temp = arr[i];
                arr[i] = min;
                arr[jIndex] = temp;
            }

        }
    }

}
