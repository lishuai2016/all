/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-12 下午5:04:21
 */

public class BubbleSort {

	/**
	 * @author lishuai
	 * @data 2016-9-12 下午5:04:21
	 * @param args
	 */
//从小到大排
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,2,4,8,9,7,1,3,0,5};
		bubble_sort3(a,a.length);
		for(int i:a){
			System.out.println(i);
		}
	}
	
	public static void bubble_sort1(int[] a,int n){
		//一般的方法
		for(int i=0;i<n;i++){
			for(int j=1;j<n-i;j++){
				if(a[j-1]>a[j]){
					int temp=a[j-1];
					a[j-1]=a[j];
					a[j]=temp;
				}
			}
		}
	}
	public static void bubble_sort2(int[] a,int n){
		//优化一：当一次遍历没有发生交换，说明排序已经排好，不再进行比较
		boolean flag=true;
		int k=n;
		while(flag){
			flag=false;
			for(int j=1;j<k;j++){
				if(a[j-1]>a[j]){
					int temp=a[j-1];
					a[j-1]=a[j];
					a[j]=temp;
					flag=true;
				}				
			}
			k--;
		}
	}
	public static void bubble_sort3(int[] a,int n){
		/*优化二：再做进一步的优化。如果有100个数的数组，仅前面10个无序，后面90个都已排好序且都大于前面10个数字，
		那么在第一趟遍历后，最后发生交换的位置必定小于10，且这个位置之后的数据必定已经有序了，
		记录下这位置，第二次只要从数组头部遍历到这个位置就可以了。*/
		int mark=n;
		while(mark>0){
			int k=mark;
			//mark不为0说明发生了交换
			mark=0;
			for(int j=1;j<k;j++){
				if(a[j-1]>a[j]){
					int temp=a[j-1];
					a[j-1]=a[j];
					a[j]=temp;
					//记录交换的位置
					mark=j;
				}	
			}
		}
		
	}

}
