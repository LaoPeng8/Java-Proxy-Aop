package org.pjj.sort;

import java.util.Arrays;

/**
 * 快速排序
 *
 * 思路为: 将数组第0个数置为中间数pivot, 两个指针指向数组低位low 与 数组高位high,
 * 将数组中间数pivot左边全是小于中间数的数, 将数组中间数pivot右边全是大于中间数的数,
 * 然后递归处理左边 于 右边.
 *
 * 话不多说直接开干
 *
 * @author PengJiaJun
 * @Date 2022/08/08 11:59
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {5,7,6,9,3,10,2,11};
        quick3(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void quick(int[] arr, int left, int right) {

        /**
         * 递归结束条件
         * 我本来以为 while(low < high) 可以挡住递归结束条件, 后来一直报 java.lang.StackOverflowError
         * 后来发现递归调用在 while(low < high) 外面, 所以就加了一个递归结束条件, 然后就好了.
         *
         * 看起来比较傻的bebug过程, 哈哈哈
         * 首先是 该行打断点 quick(arr, left, low); 发现 left 与 right 都为 0 了, 然后一直递归
         * 后将递归调用的两行外面加上 if(left !=0 && right !=0)
         * 后继续debug发现 可以走到 quick(arr, low+1, right);了, 后又发现 数组已经有序了, 但是还在一直递归
         * 灵光一闪在 此处加上 if(left == right) return; 果然解决了问题 (删除了之前的if(left !=0 && right !=0))
         * 后看着 while(low < high)  将if(left == right) 改为 if(left >= right)
         *
         */
        if(left >= right) {
            return;
        }

        int low = left;
        int high = right;
        int pivot = arr[left];

        //low < high, 当low于high重合的时候, 也就是low !< high的时候, 表示一轮排序结束, pivot左边全是小于pivot的, pivot右边全是大于pivot的
        while(low < high) {

            /**
             * 这里需要注意两点
             * 1. 必须是先从high开始遍历, 也就是说必须 先 while(arr[high] >= pivot) 后 while(arr[low] <= pivot)
             *      * 因为, 我一开始先 while(arr[low] <= pivot) 发现一个问题
             *      * 当 arr[low] > pivot了则是要将 arr[low]的值赋值给 arr[high], 那么arr[high]的值就会丢失
             *
             *      * 如果先从while(arr[high] >= pivot) 开始则不会出现这种情况
             *      * 当 arr[high] < pivot时,将 arr[high]的值赋值给 arr[low], arr[low]的值不会丢失, 因为arr[low]的值作为中间值赋值给了pivot了
             *
             * 2. 必须要加上 low < high 要不然可能会越界, 因为外层while的 low < high 只是 while高位赋值到低位, 低位赋值高位后 (pivot左右各交换一个元素后),
             * 进行下一轮交换前的判断, 只能防止 各交换一个元素后不会出现 low > high.
             *      * 有这么一种情况, low值本身就全部小于 pivot, 那么就会一直low++往后寻找大于pivot的值
             *      * 然后终于找到了但是 low值已经大于high了, 但是那不是越界了嘛
             *
             * 3. 这里不能是 > 或 <, 需要是 >= 或 <= 避免了等于pivot的值还需要左右移动, 多此一举
             */
            while(low < high && arr[high] >= pivot) {
                high--;
            }
            arr[low] = arr[high];

            while(low < high && arr[low] <= pivot) {
                low++;
            }
            arr[high] = arr[low];
        }
        //退出循环后, 必然是 low 与 high 指向同一个元素, 该元素就是中间值
        //至此 一轮排序完成, pivot左全部小于pivot, pivot右全部大于pivot
        arr[low] = pivot;//arr[high] = pivot;也可

        //虽然pivot左全部小于pivot, 但是并不是有序的, 它们只是都小于pivot但是并不有序
        //虽然pivot右全部大于pivot, 但是并不是有序的, 它们只是都大于pivot但是并不有序
        //所以需要递归处理
        quick(arr, left, low);//左, 左边的范围就是 原数组左边到 中间值
        quick(arr, low+1, right);//右, 右边的范围就是 原数组中间值+1 到原数组右边
    }


    /**
     * 再写一遍
     * 希望以后能一直记得
     */
    public static void quick2(int[] arr, int left, int right) {

        if(left >= right) {//递归结束条件
            return;
        }

        int low = left;
        int high = right;
        int pivot = arr[left];//数组第一个值作为中间值

        while(low < high) {
            while(low < high && arr[high] >= pivot) {
                high--;
            }
            arr[low] = arr[high];

            while(low < high && arr[low] <= pivot) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = pivot;

        quick2(arr, left, low);
        quick2(arr, low + 1, right);

    }


    public static void quick3(int[] arr, int left, int right) {

        if(left >= right) {
            return;
        }

        int low = left;
        int high = right;
        int pivot = arr[left];

        while(low < high) {
            while(low < high && arr[high] >= pivot) {
                high--;
            }
            arr[low] = arr[high];

            while(low < high && arr[low] <= pivot) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = pivot;

        quick3(arr, left, low);
        quick3(arr, low + 1, right);
    }

}
