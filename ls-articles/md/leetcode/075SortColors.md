# 75. Sort Colors


```java
优解：二指针，时间复杂度N，空间复杂度O（1）
/**
 *
 */
package array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishuai
 * @data 2016-12-6 下午3:02:53
 */

public class SortColors {

    /**
     * @author lishuai
     * @data 2016-12-6 下午3:02:53
Given an array with n objects colored red, white or blue,
sort them so that objects of the same color are adjacent, with the colors in the order red, white and blue.

Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.

Note:
You are not suppose to use the library's sort function for this problem.
     */

    public static void main(String[] args) {
         int[] a = {0,1,0};
         sortColors(a);
         System.out.println();
    }
    //4 九章        二指针
    public static void sortColors(int[] a) {
        if (a == null || a.length <= 1) {
            return;
        }       
        int pl = 0;
        int pr = a.length - 1;
        int i = 0;
        while (i <= pr) {
            if (a[i] == 0) {
                swap(a, pl, i);
                pl++;
                i++;
            } else if(a[i] == 1) {
                i++;
            } else {
                swap(a, pr, i);
                pr--;
            }
        }
    }

    //3 二指针
     public static void sortColors3(int[] A) {
           if(A==null || A.length<2) return;
           int low = 0;
           int high = A.length-1;
           for(int i = low;i <= high;) {
               if(A[i] == 0) {
                  // swap A[i] and A[low] and i,low both ++
                  int temp = A[i];
                  A[i] = A[low];
                  A[low] = temp;
                  i++;
                  low++;
               }else if(A[i]==2) {
                   //swap A[i] and A[high] and high--;
                  int temp = A[i];
                  A[i] = A[high];
                  A[high] = temp;
                  high--;
               }else {
                   i++;
               }
           }
       }



    //2 思路：二指针一个维护0，一个维护2
    public static void sortColors2(int[] nums) {
        int n = nums.length;
        int second=n-1, zero=0;
        for (int i=0; i<=second; i++) {
            while (nums[i]==2 && i<second) swap(nums,i, second--);
            //循环的意义，保证交换的最后是与前面第一个非0的数字交换的
            while (nums[i]==0 && i>zero) swap(nums,i, zero++);
        }   
        System.out.println(nums);
    }
    public static void swap(int[] nums,int a,int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }


    //效率：时间复杂度N，空间复杂度O（3）
    //1 思路：使用map统计每个数字出现的次数，考虑有可能有些数字没有的情况
    public static void sortColors1(int[] nums) {
        if (nums == null || nums.length ==0) return;
        Map<Integer,Integer>  map = new HashMap<Integer,Integer>();
        for (int i = 0;i < nums.length;i++) {
            if (nums[i] == 0) map.put(0, map.get(0) != null ? map.get(0) + 1 : 1);
            if (nums[i] == 1) map.put(1, map.get(1) != null ? map.get(1) + 1 : 1);
            if (nums[i] == 2) map.put(2, map.get(2) != null ? map.get(2) + 1 : 1);
        }
        int k = 0;
        while (map.get(0) != null && k < map.get(0)) nums[k++] = 0;
        if (map.get(0) != null) {
             while (map.get(1) != null && k < map.get(1) + map.get(0)) nums[k++] = 1;
        } else {
             while (map.get(1) != null && k < map.get(1)) nums[k++] = 1;
        }       
        while (k < nums.length) nums[k++] = 2;
        System.out.println();
    }

}




```