package org.pjj.sort;

import java.util.Arrays;

/**
 * 复习插入排序
 *
 * 插入排序的思路就是, 将N个代排序的元素看为一个有序表和一个无序表, 开始时有序表中只有一个元素, 无序表中有N-1个元素,
 * 每轮排序取出无序表中的第一个元素, 与有序表所有元素比较, 并插入到合适位置, 实之成为新的有序表, 直到无序表中一个元素都没有了. 则排序完成
 * @author PengJiaJun
 * @Date 2022/08/06 23:35
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {5,7,6,9,3,10,2,11};
        insert3(arr);
        System.out.println(Arrays.toString(arr));
    }


    /**
     * 看了一遍插入排序的思路, 也是回想起来了(明白了)思路, 但是自己写, 写了半个多小时搞不定,总是有点小问题,按照我那个写代码的思路就是有问题的感觉
     *
     * 看了一边, 以前写的插入排序, 感觉代码的好简洁, 比我自己的思路好太多.
     */
    public static void insert(int[] arr) {
        //i = 1; 是因为 从无序列表第一个开始遍历
        for(int i=1; i < arr.length; i++) {//遍历无序列表, 并将其中每一个元素, 依次插入有序列表中合适的位置(插入后有序列表仍然有序)
            int insertValue = arr[i];//待排序的值, 也就是无序列表的第一个值
            int insertIndex = i - 1;//有序列表的最后一个数的下标(无序列表的的第一个数的前一个数不就是有序列表的最后一个数吗?)

            //倒着遍历有序列表(方便数组元素向后挪动)
            //并找到无序列表中的第一个元素, 应该插入到有序列表中的哪个位置, 将该位置的元素往后挪一位, 方便插入到该位置
            while(insertIndex >= 0 && insertValue < arr[insertIndex]) {
                arr[insertIndex + 1] = arr[insertIndex];//向后挪动, 腾出insertValue在有序列表中的插入位置 (不用担心往后挪动会覆盖无序列表第一个元素的值, 无序列表第一个元素的值就是insertValue已经被保存了一份)
                insertIndex--;
            }
            //退出while后,就已经找到了insertValue的插入位置了, 两种情况
            //1. insertIndex = -1 了, 说明 insertValue的值 在有序列表中应该是最小的, 即应该在第一位 (-1 + 1 就是0了, 位置刚好, 所以 +1)
            //2. insertValue第一次大于arr[insertIndex], 说明insertValue应该插入到insertIndex的后一位 (所以也是需要 + 1)
            arr[insertIndex + 1] = insertValue;
        }
    }

    /**
     * 再写一遍
     * 希望以后能一直记得
     */
    public static void insert2(int[] arr) {
        for(int i=1; i < arr.length; i++) {//遍历无序列表, 并将无序列表的元素一个一个插入有序列表

            int insertValue = arr[i];//无序列表第一个元素, 也就是待插入有序列表的元素
            int insertIndex = i - 1;//有序列表最后一个元素, 用来遍历有序列表, 找到 insertValue的插入位置 (无序列表前一个元素不就是有序列表最后一个元素嘛?)

            //想象一样 有序列表是一个数组, 里面10个元素, 待插入元素 需要插入5这个位置, 那么5,即5往后的元素, 是不是需要往后挪一格
            //不用担心往后挪一格后会破坏无序列表的第一个元素, 因为无序列表的第一个元素就是 待插入元素insertValue中已经保存了, 所以直接覆盖是没有问题的.
            while(insertIndex >= 0 && insertValue < arr[insertIndex]) {
                arr[insertIndex + 1] = arr[insertIndex];
                insertIndex--;
            }
            //当while退出后,两种情况
            //1. insertValue < arr[insertIndex], 不满足了, 说明 insertValue第一次 > arr[insertIndex], 说明insertValue应该插入 insertIndex的后面 (insertIndex + 1就是 insertIndex的后面)
            //2. insertIndex >= 0, 不满足了, 说明 insertValue = -1了, insertValue还是 < arr[insertIndex], 说明insertValue是最小的, 应该插入第一个位置 (-1 + 1 就是第一个位置)
            arr[insertIndex + 1] = insertValue;
        }
    }

    public static void insert3(int[] arr) {
        for(int i = 1; i < arr.length; i++) {
            int insertValue = arr[i];//无序列表第一个, 也就是待插入元素
            int insertIndex = i - 1;//有序列表最后一个

            while(insertIndex >= 0 && insertValue < arr[insertIndex]) {
                arr[insertIndex + 1] = arr[insertIndex];
                insertIndex--;
            }
            arr[insertIndex + 1] = insertValue;
        }
    }
}
