/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author lishuai
 * @data 2017-1-8 下午2:11:32
 */

public class KthLargestElementinanArray {

	/**
	 * @author lishuai
	 * @data 2017-1-8 下午2:11:32
Find the kth largest element in an unsorted array. 
Note that it is the kth largest element in the sorted order, not the kth distinct element.

For example,
Given [3,2,1,5,6,4] and k = 2, return 5.

Note: 
You may assume k is always valid, 1 ≤ k ≤ array's length.
	 */

	public static void main(String[] args) {
		int[] a = {3,2,1,5,6,4};
		System.out.println(findKthLargest3(a,2));
	}
	/**
You can take a couple of approaches to actually solve it:

O(N lg N) running time + O(1) memory
The simplest approach is to sort the entire input array and 
then access the element by it's index (which is O(1)) operation:
	 */
	//4  3ms
	public static int findKthLargest(int[] nums, int k) {
        int N = nums.length;
        Arrays.sort(nums);
        return nums[N - k];
}
	
	/**
	O(N lg K) running time + O(K) memory
Other possibility is to use a min oriented priority queue that will store the K-th largest values. 
The algorithm iterates over the whole input and maintains the size of priority queue.
	 */
	//3  最大堆PriorityQueue     14ms
	public static int findKthLargest3(int[] nums, int k) {

	    PriorityQueue<Integer> pq = new PriorityQueue<>();
	    for(int val : nums) {
	        pq.offer(val);

	        if(pq.size() > k) {
	            pq.poll();
	        }
	    }
	    return pq.peek();
	}
	/**
Initialize left to be 0 and right to be nums.size() - 1;
Partition the array, if the pivot is at the k-1-th position, return it (we are done);
If the pivot is right to the k-1-th position, update right to be the left neighbor of the pivot;
Else update left to be the right neighbor of the pivot.
Repeat 2.
	 */
	//1   和2类似   59ms  1 2 3 4 
    public static int findKthLargest1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
        	return 0;
        }
        if (k <= 0) {
        	return 0;
        }
        int left = 0;
        int right = nums.length - 1;
        while (true) {
        	int position = partition1(nums, left, right);
        	if (position == nums.length - k) {
        		return nums[position];
        	} else if (position < nums.length - k) {
        		left = position + 1;
        	} else {
        		right = position - 1;
        	}
        }    	   
    }    
    public static int partition1(int[] nums, int l, int r) {
    	int left = l;
    	int right = r;
    	int pivot = nums[left];
    	
    	while (left < right) {
    		while (left < right && nums[right] >= pivot) {
    			right--;
    		}
    		if (left < right) {
    			nums[left] = nums[right];
    			left++;
    		}
    		while (left < right && nums[left] < pivot) {
    			left++;
    		}
    		if (left < right) {
    			nums[right] = nums[left];
    			right--;
    		}   		   		
    	}
    	
    	nums[left] = pivot;
    	return left;
    }
    
    
    //2九章   44ms
    public static int kthLargestElement2(int k, int[] nums) {
        // write your code here
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (k <= 0) {
            return 0;
        }
        return helper(nums, 0, nums.length - 1, nums.length - k + 1);
        
    }
    public static int helper(int[] nums, int l, int r, int k) {
        if (l == r) {
            return nums[l];
        }
        int position = partition(nums, l, r);
        if (position + 1 == k) {
            return nums[position];
        } else if (position + 1 < k) {
            return helper(nums, position + 1, r, k);
        }  else {
            return helper(nums, l, position - 1, k);
        }
    }
    public static int partition(int[] nums, int l, int r) {
        // 初始化左右指针和pivot
        int left = l, right = r;
        int pivot = nums[left];
        
        // 进行partition
        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }
        
        // 返还pivot点到数组里面
        nums[left] = pivot;
        return left;         
    }
}
