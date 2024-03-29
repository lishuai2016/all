/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuai
 * @data 2016-11-28 上午10:39:13
 */

public class FindAllDuplicatesinanArray {

	/**
	 * @author lishuai
	 * @data 2016-11-28 上午10:39:13
	 * @param args
	 * Given an array of integers, 1 ≤ a[i] ≤ n (n = size of array), 
	 * some elements appear twice and others appear once.

Find all the elements that appear twice in this array.

Could you do it without extra space and in O(n) runtime?
Input:
[4,3,2,7,8,2,3,1]

Output:
[2,3]
	 */

	public static void main(String[] args) {
		int[] a={4,3,2,7,8,2,3,1};
		
		findDuplicates2(a);
	}
	//1 思路：由于数组中是1~n的数字，可以将数组中的数字转化为数组的下标，遍历数组时第一次下标出现，将对应下标数组中的元素设置为相反数，否则，则是此下标再次出现，则将元素存入list中，最后将原数组恢复
    public static List<Integer> findDuplicates(int[] nums) {
    	List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<nums.length;i++){
            int location=Math.abs(nums[i])-1;
            if(nums[location]<0){
                list.add(Math.abs(nums[i]));
            }else{
                nums[location]=-nums[location];
            }
        }
        //下面的代码可要可不要，回复数组中的元素
        for(int j=0;j<nums.length;j++){
            nums[j]=Math.abs(nums[j]);
        }
        
        return list;
    }
  //2 自己的第一思路       Time Limit Exceeded
    public static List<Integer> findDuplicates1(int[] nums) {

    	List<Integer> list =new ArrayList<Integer>();
    	for(int i=0;i<nums.length;i++){
    		if(!list.contains(nums[i])){
    			list.add(nums[i]);
    		}
    	}
    	for(int j=0;j<nums.length;j++){
    		if(list.contains(nums[j])){
    			list.remove(Integer.valueOf(nums[j]));    			
    		}else{
    			list.add(nums[j]);
    		}
    	}
    	
    	return list;
    }
    //3 这样也行，可1类似，只是没有恢复原数组
    public static List<Integer> findDuplicates2(int[] nums) {
    	List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; ++i) {
            int index = Math.abs(nums[i])-1;
            if (nums[index] < 0)
                res.add(Math.abs(index+1));
            nums[index] = -nums[index];
        }
        return res;
   }

}
