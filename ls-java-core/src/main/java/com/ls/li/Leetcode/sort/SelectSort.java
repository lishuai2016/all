/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-13 上午10:06:26
 */

public class SelectSort {

	/**
	 * @author lishuai
	 * @data 2016-9-13 上午10:06:26
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,2,4,8,9,7,1,3,0,5};
		select_sort1(a,a.length);
		for(int i:a){
			System.out.println(i);
		}

	}
//	直接选择排序是从无序区选一个最小的元素直接放到有序区的最后。
	public static void select_sort1(int[] s,int n){
		int i,j,minIndex;
		for(i=0;i<n;i++){
			minIndex=i;
			for(j=i+1;j<n;j++){
				if(s[j]<s[minIndex]){
					minIndex=j;
				}
			}
			int temp=s[i];
			s[i]=s[minIndex];
			s[minIndex]=temp;
		}
	}
	

}
