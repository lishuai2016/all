/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-12 下午3:34:35
 */

public class QuickSort {

	/**
	 * @author lishuai
	 * @data 2016-9-12 下午3:34:35
	 * @param args
	 * 
	 * //快速排序  
void quick_sort(int s[], int l, int r)  
{  
    if (l < r)  
    {  
        //Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1  
        int i = l, j = r, x = s[l];  
        while (i < j)  
        {  
            while(i < j && s[j] >= x) // 从右向左找第一个小于x的数  
                j--;    
            if(i < j)   
                s[i++] = s[j];  
              
            while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数  
                i++;    
            if(i < j)   
                s[j--] = s[i];  
        }  
        s[i] = x;  
        quick_sort(s, l, i - 1); // 递归调用   
        quick_sort(s, i + 1, r);  
    }  
}  
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,3,4,4,5,679,1,2,7,8,9};
		quick_sort1(a,0,a.length-1);
		for(int i:a){
			System.out.println(i);
		}
		
	}
	public static void quick_sort1(int[] nums, int left, int right) {
		if (left < right) {
			int base = nums[left];
			int i = left;
			int j = right;
			while (i < j) {
				while (i < j && nums[j] >= base) {
					j--;
				}
				if (i < j) {
					nums[i] = nums[j];
					i++;
				}
				while (i < j && nums[i] < base) {
					i++;
				}
				if (i < j) {
					nums[j] = nums[i];
					j--;
				}
			}			
			nums[i] = base;
			quick_sort1(nums, left, i - 1);
			quick_sort1(nums, i + 1, right);
		}
	}
	
	//快排     分治+挖坑
	static void quick_sort(int[] s,int l,int r){
/*		基本思想
		1．先从数列中取出一个数作为基准数。

		2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。

		3．再对左右区间重复第二步，直到各区间只有一个数。*/
		
		//递归的结束条件
		if(l<r){
			//Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换（当基准数去区间的中间值时）
			//基准数为第一个坑
			int x=s[l];
			int i=l,j=r;
			//完成一次交换数据
			while(i<j){				
				//从右边找第一个比基准数小的数，然后填入前一个坑
				while(i<j&&s[j]>=x) j--;
				if(i<j) s[i++]=s[j];
				//从左边边找第一个比基准数大或者等于的数，然后填入前一个坑
				while(i<j&&s[i]<x) i++;
				if(i<j) s[j--]=s[i];
			}
			s[i]=x;
			quick_sort(s,l,i-1);
			quick_sort(s,i+1,r);
		}
		
	}

}
