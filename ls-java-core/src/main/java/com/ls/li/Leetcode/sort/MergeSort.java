/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-13 下午1:40:28
 */

public class MergeSort {

	/**
	 * @author lishuai
	 * @data 2016-9-13 下午1:40:28
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,2,4,8,9,7,1,3,0,5};
		mergeSort(a, a.length);
		for(int i:a){
			System.out.println(i);
		}
	}
	
	public static void mergeSort(int[] a,int n){
		int [] temp=new int[n];
		//注意下标，按数组的下标而不是长度
		merge_sort(a, 0, n-1,temp);
	}
	
	public static void merge_sort(int[] a,int first,int end,int[] temp){
		if(first<end){
			int mid=(first+end)/2;
			merge_sort(a, first, mid, temp);//左边有序  
			merge_sort(a, mid+1, end, temp);//右边有序  
			merge_arrays(a, first, mid, end, temp);//再将二个有序数列合并  
		}
	}
	
	//将有二个有序数列a[first...mid]和a[mid...last]合并。
	public static void merge_arrays(int[] a,int first,int mid,int end,int[] temp){
		int i=first,j=mid+1;
		int m=mid,n=end;
		int k=0;
		//注意下标，按数组的下标而不是长度
		while(i<=m&&j<=n){
			if(a[i]<=a[j]){
				temp[k++]=a[i++];
			}else{
				temp[k++]=a[j++];
			}
		}
		//注意下标，按数组的下标而不是长度
		while(i<=m){
			temp[k++]=a[i++];
		}
		//注意下标，按数组的下标而不是长度
		while(j<=n){
			temp[k++]=a[j++];
		}
		for(i=0;i<k;i++){
			a[first+i]=temp[i];
		}
	}

}
