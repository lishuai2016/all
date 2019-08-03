# 80. Remove Duplicates from Sorted Array II

```java
优解：时间复杂度N（法2，3）
/**
 *
 */
package array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lishuai
 * @data 2016-12-6 下午4:18:00
 */

public class RemoveDuplicatesfromSortedArrayII {

    /**
     * @author lishuai
     * @data 2016-12-6 下午4:18:00
Follow up for "Remove Duplicates":
What if duplicates are allowed at most twice?

For example,
Given sorted array nums = [1,1,1,2,2,3],

Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3.
It doesn't matter what you leave beyond the new length.
     */

    public static void main(String[] args) {
        int[] a = {1,1,1,1,2,2,3};
         System.out.println(removeDuplicates(a));

    }
    //3九章（思路容易想到，具体写代码时需要处理细节问题）
     public static int removeDuplicates(int[] nums) {
            // write your code here
            if(nums == null)
                return 0;
            int cur = 0;
            int i ,j;
            for(i = 0; i < nums.length;){
                int now = nums[i];
                for( j = i; j < nums.length; j++){
                    if(nums[j] != now)
                        break;
                    if(j-i < 2)
                        nums[cur++] = now;
                }
                //跳过重复的
                i = j;
            }
            return cur;
        }

    //2 根据题意的特点 ，后面的和前面至少下标差2，要是后面的大，则说明前面有重复
    public static int removeDuplicates2(int[] nums) {
        int i = 0;
        for (int n : nums)
            if (i < 2 || n > nums[i-2])
                nums[i++] = n;
        return i;
    }
    //效率：时间复杂度和空间复杂度都为N
    //1 思路:步骤有点多，用一个map统计各个数字出现的次数和一个list统计都有哪些数字
    public static int removeDuplicates1(int[] nums) {
        int sum = 0;
        Map<Integer,Integer>  map = new HashMap<Integer,Integer>();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0;i < nums.length;i++) {
            if (!list.contains(nums[i])) list.add(nums[i]);
            if (map.containsKey(nums[i])) map.put(nums[i], map.get(nums[i]) + 1);
            else map.put(nums[i], 1);
        }
        int index = 0;
        for (int i = 0;i < list.size();i++) {
            if (map.get(list.get(i)) <= 2) sum += map.get(list.get(i));
            else sum += 2;   
            int k = 0;
            int size = map.get(list.get(i)) > 2 ? 2 : map.get(list.get(i));
            while (k < size) {
                nums[index++] = list.get(i);
                k++;
            }             
        }             
        return sum;
    }

}



```