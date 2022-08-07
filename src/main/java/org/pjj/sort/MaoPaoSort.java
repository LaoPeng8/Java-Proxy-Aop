package org.pjj.sort;

import java.util.Arrays;

/**
 * 复习冒泡排序
 * @author PengJiaJun
 * @Date 2022/08/06 22:39
 */
public class MaoPaoSort {
    public static void main(String[] args) {
        int[] arr = {5,7,6,9,3,10,2,11};
        maopao(arr);
        System.out.println(Arrays.toString(arr));//[2, 3, 5, 6, 7, 9, 10, 11]
    }

    public static void maopao(int[] arr) {
        int temp;//定义在循环外面, 避免重复定义
        boolean flag = false;
        for(int i=0; i < arr.length - 1; i++) {//假设10个元素,则实际排序9次, 也就是说9个元素都有序了, 则最后一个元素自然是有序的了, 所以 -1
            flag = false;//重置

            //假设10个元素,则两两交换9次,则将最大值放入最后了所以 - 1;
            //假设10个元素, 已经排好序3个了, 则实际元素为7个,所以10-3; 7个元素实则只需要6次两两比较, 所以是 10-3-1
            for(int j=0; j < arr.length - 1 - i; j++) {
                if(arr[j] >= arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    //发生两两交换则将 flag = true;
                    flag = true;
                }
            }

            //但凡发生过一次 两两交换, flag都为true, 则表示需要继续排序
            //如果 flag = false;表示一次两两交换都没发生, 表示已经有序了, 不需要继续排序了
            if(!flag) {
                break;
            }
        }
    }
}
