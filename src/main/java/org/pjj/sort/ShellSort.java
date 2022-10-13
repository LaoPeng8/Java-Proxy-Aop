package org.pjj.sort;

import java.util.Arrays;

/**
 * 希尔排序
 *
 * 插入排序当数组最小值在 数组最末端时, 效率极低, 因为需要将 前面的元素每个都往后挪, 然后将最后一个元素赋值到数组第一位,
 * 这样相当于是将整个数组都往后挪动一遍, 当数组呈从大到小排列时, 对该数组使用插入排序进行 从小到大排序, 效率是最低的情况,
 * 因为 小数都在后面, 意味着, 每插入一个元素到有序数组, 都需要挪动大量元素(往后移动一位).
 * 基于这种情况的出现, 一个叫希尔的人发明了, 希尔排序
 * 希尔排序的目的就是通过前几次的排序, 将数组中小的数整体往前挪动, 即避免了上面小数都在数组后面的情况, 即将插入排序效率到最优.
 *
 * 思路就是: 将一个数组分为 步长=数组.length / 2;组, 对其每个组进行插入排序 目的是为了让数组 整体有序,
 * 之后步长=步长/2; 即将 数组再次分组, 再次排序, 最后步长为1时, 即是对整个数组进行排序.(此时的数组已经达到了整体有序的情况)
 * (不太会说出来, 感觉还不如看网上的博客)
 *
 * 上一次写希尔排序是2020年, 这次写 在复习了插入排序后, 并看了之前写希尔排序的笔记思路后, 就写出来了 (没有看代码, 虽然感觉还是有点懵逼)
 * 我依稀记得就是, 希尔排序就是普通的插入排序外面套了一层for循环用来控制 步长, 步长第一次=数组长度/2 之后 步长=步长/2, 直到步长=1完成最后一次排序
 * 步长将整个数组分成了N个小组, 原本无序数组-1就是 有序数组第一个, 但是由于分成了N个小组, 所以是无序数组-步长才是有序数组第一个,
 * 同样, 在自己小组内移动, 之前是-1, 现在都是-步长
 * 用死的来说就是 所有的 -1 变成了 -步长
 *
 * 感觉挺抽象的, 呵呵
 *
 * 但其实也挺好理解的. 明天在本子上写一遍 写的出来就过了.
 *
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/07 21:47
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        shell3(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void shell(int[] arr) {
        for(int step=arr.length/2; step >= 1; step=step/2) {

            for(int i=step; i < arr.length; i++) {
                int insertValue = arr[i];//无序列表第一个元素, 待排序元素
                int insertIndex = i - step;//有序列表最后一个

                while(insertIndex >= 0 && insertValue < arr[insertIndex]) {
                    arr[insertIndex + step] = arr[insertIndex];
                    insertIndex = insertIndex - step;
                }
                arr[insertIndex + step] = insertValue;
            }

        }

    }

    /**
     * 再写一遍
     * 希望以后能一直记得
     */
    public static void shell2(int[] arr) {

        //步长 > 1时, 都是让这个数组整体趋于有序, 即将 数组末端的小数插入到数组前面, 数组前面的大数插入到数组后面
        //步长 = 1时, 即是最后一轮排序, 即普通的插入排序
        for(int step=arr.length/2; step >=1; step = step / 2) {

            // i = 步长, 假如数组长度为10, 步长为10/2=5, 那么下标5的元素表示无序列表第一个元素, 5-5=0 表示有序列表最后一个元素
            // 下标6 也是表示无序列表第一个元素, 6-5=1 也是表示有序列表最后一个元素, 不同在于 一个是第一组,一个是第二组
            for(int i = step; i < arr.length; i++) {//遍历每组的无序列表,并将每组的无序列表的元素插入每组的有序列表
                int insertValue = arr[i];//无序列表第一个
                int insertIndex = i - step;//有序列表最后一个

                while(insertIndex >=0 && insertValue < arr[insertIndex]) {
                    arr[insertIndex + step] = arr[insertIndex];//往后挪一格 (都是在组内挪动)
                    insertIndex = insertIndex - step;
                }
                arr[insertIndex + step] = insertValue;
            }

        }

    }

    public static void shell3(int[] arr) {
        for(int step = arr.length / 2; step >= 1; step = step / 2) {
            for(int i = step; i < arr.length; i++) {
                int insertValue = arr[i];
                int insertIndex = i - step;

                while(insertIndex >=0 && insertValue < arr[insertIndex]) {
                    arr[insertIndex + step] = arr[insertIndex];
                    insertIndex = insertIndex - step;
                }
                arr[insertIndex + step] = insertValue;

            }
        }
    }
}
