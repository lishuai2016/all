# 78. Subsets


```java
优解：2
/**
 *
 */
package array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-12-6 下午2:24:32
 */

public class Subsets {

    /**
     * @author lishuai
     * @data 2016-12-6 下午2:24:32
Given a set of distinct integers, nums, return all possible subsets.

Note: The solution set must not contain duplicate subsets.

For example,
If nums = [1,2,3], a solution is:

[
  [3],
  [1],
  [2],
  [1,2,3],
  [1,3],
  [2,3],
  [1,2],
  []
]
     */

    public static void main(String[] args) {
        int[] a = {1,2,3};
        System.out.println(subsets1(a));

    }
    //3      No messy indexing. Avoid the ConcurrentModificationException by using a temp list.  4ms
     public static List<List<Integer>> subsets(int[] S) {
            List<List<Integer>> res = new ArrayList<>();
            res.add(new ArrayList<Integer>());           
            Arrays.sort(S);
            for(int i : S) {
                List<List<Integer>> tmp = new ArrayList<>();
                for(List<Integer> sub : res) {
                    List<Integer> a = new ArrayList<>(sub);
                    a.add(i);
                    tmp.add(a);
                }
                res.addAll(tmp);
            }
            return res;
        }

     //2 九章  Non Recursion 1ms
     public static ArrayList<ArrayList<Integer>> subsets2(int[] nums) {
            ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
            int n = nums.length;
            Arrays.sort(nums);

            // 1 << n is 2^n
            // each subset equals to an binary integer between 0 .. 2^n - 1
            // 0 -> 000 -> []
            // 1 -> 001 -> [1]
            // 2 -> 010 -> [2]
            // ..
            // 7 -> 111 -> [1,2,3]
            for (int i = 0; i < (1 << n); i++) {
                ArrayList<Integer> subset = new ArrayList<Integer>();
                for (int j = 0; j < n; j++) {
                    // check whether the jth digit in i's binary representation is 1
                    if ((i & (1 << j)) != 0) {
                        subset.add(nums[j]);
                    }
                }
                result.add(subset);
            }

            return result;
        }

    //1  回溯法  2ms
    public static List<List<Integer>> subsets1(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);       
        backtrack(res, new ArrayList<Integer>(), nums, 0);       
        return res;
    }

    private static void backtrack(List<List<Integer>> list , List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<Integer>(tempList));
        for(int i = start; i < nums.length; i++) {
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

}




```