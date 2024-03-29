/**
 * 
 */
package com.ls.li.Leetcode.hashtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lishuai
 * @data 2016-12-28 下午3:53:44
 */

public class TopKFrequentElements {

	/**
	 * @author lishuai
	 * @data 2016-12-28 下午3:53:44
Given a non-empty array of integers, return the k most frequent elements.

For example,
Given [1,1,1,2,2,3] and k = 2, return [1,2].

Note: 
You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
Your algorithm's time complexity must be better than O(n log n), where n is the array's size.

[1,2]
2

[1,2]


[1,2,3] 2   [1,3]
  输入一个存储整型数据的非空数组，返回出现频次最多的前k个元素。
	 */

	public static void main(String[] args) {
		

	}
	
	//4
	/**
// use treeMap. Use freqncy as the key so we can get all freqencies in order
public class Solution {
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int n: nums){
            map.put(n, map.getOrDefault(n,0)+1);
        }
        
        TreeMap<Integer, List<Integer>> freqMap = new TreeMap<>();
        for(int num : map.keySet()){
           int freq = map.get(num);
           if(!freqMap.containsKey(freq)){
               freqMap.put(freq, new LinkedList<>());
           }
           freqMap.get(freq).add(num);
        }
        
        List<Integer> res = new ArrayList<>();
        while(res.size()<k){
            Map.Entry<Integer, List<Integer>> entry = freqMap.pollLastEntry();
            res.addAll(entry.getValue());
        }
        return res;
    }
}
	 */
	
	//3九章  jdk1.8特性
	/**
	public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<Map.Entry<Integer, Integer>>(
            new Comparator<Map.Entry<Integer, Integer>>() {
                public int compare(Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) {
                    return e1.getValue() - e2.getValue();
                }
            });
        for (int i = 0; i < nums.length; i++) {
            if (!hashmap.containsKey(nums[i])) {
                hashmap.put(nums[i], 1);
            } else {
                hashmap.put(nums[i], hashmap.get(nums[i]) + 1);
            }
        }
        
        for (Map.Entry<Integer, Integer> entry : hashmap.entrySet()) {
            if (queue.size() < k) {
                queue.offer(entry);
            } else if (queue.peek().getValue() < entry.getValue()) {
                queue.poll();
                queue.offer(entry);
            }
        }
        
        List<Integer> ans = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> entry : queue)
            ans.add(entry.getKey());
        return ans;
    }
	 */
	//2思路和1类似，优化一点，但是有可能出错
	public List<Integer> topKFrequent2(int[] nums, int k) {

		List<Integer>[] bucket = new List[nums.length + 1];
		Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();

		for (int n : nums) {
			frequencyMap.put(n, frequencyMap.get(n) != null ? frequencyMap.get(n) + 1 : 1);
		}

		for (int key : frequencyMap.keySet()) {
			int frequency = frequencyMap.get(key);
			if (bucket[frequency] == null) {
				bucket[frequency] = new ArrayList<>();
			}
			bucket[frequency].add(key);
		}

		List<Integer> res = new ArrayList<>();

		for (int pos = bucket.length - 1; pos >= 0 && res.size() < k; pos--) {
			if (bucket[pos] != null) {
				res.addAll(bucket[pos]);
			}
		}
		return res;
	}
	//1桶排序：先用map统计各自出现的次数，然后在对统计的value值进行通排序
	public static List<Integer> topKFrequent1(int[] nums, int k) {
		List<Integer> res = new ArrayList<>();
		Map<Integer,Integer> map = new HashMap<>();
		int max = 0;
		for (int i = 0;i < nums.length;i++) {
			map.put(nums[i], map.get(nums[i]) != null ? map.get(nums[i]) + 1 : 1);
			max = Math.max(max, map.get(nums[i]));
		}
		List<Integer>[] dp = new List[max + 1];
		for (Integer i : map.keySet()) {
			int frequent = map.get(i);
			if (dp[frequent] == null) dp[frequent] = new ArrayList<>();
			dp[frequent].add(i);
		}
		int p = 0;
		for (int i = dp.length - 1;i >= 0 && res.size() < k;i--) {
			List<Integer> temp = dp[i];
			if (temp != null) {
				for (Integer j : temp) {
					if (p == k) return res;
					res.add(j);
					p++;
				}
			}
		}
		return res;
	}
}
