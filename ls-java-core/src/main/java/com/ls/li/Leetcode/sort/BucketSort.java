/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-12-16 下午3:40:35
 */

public class BucketSort {

	/**
	 * @author lishuai
	 * @data 2016-12-16 下午3:40:35
	 * @param args
	 */

	public static void main(String[] args) {
		int [] a={6,2,4,3,7,1,3,0,5,15};
		bucket_sort(a);
		for(int i:a){
			System.out.println(i);
		}

	}
	//通排序
	public static void bucket_sort(int[] a) {
		int max = 0;
		for (int i = 0;i < a.length;i++) if (a[i] > max) max = a[i];
		int[] hash = new int[max + 1];
		for (int i = 0;i < a.length;i++) hash[a[i]]++;
		int k = 0;
		for (int i = 0;i < hash.length;i++) {
			while (hash[i]-- != 0) a[k++] = i;
		}
		
		System.out.println();
	}
	
	
}
