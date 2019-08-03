/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-13 上午9:05:45
 */

public class ShellSort {

	/**
	 * @author lishuai
	 * @data 2016-9-13 上午9:05:45
	 * @param args
	 * 
	 * int i, j, gap;  
  
    for (gap = n / 2; gap > 0; gap /= 2) //步长  
        for (i = 0; i < gap; i++)        //直接插入排序  
        {  
            for (j = i + gap; j < n; j += gap)   
                if (a[j] < a[j - gap])  
                {  
                    int temp = a[j];  
                    int k = j - gap;  
                    while (k >= 0 && a[k] > temp)  
                    {  
                        a[k + gap] = a[k];  
                        k -= gap;  
                    }  
                    a[k + gap] = temp;  
                }  
        }  
	 */

	
	
	
	/*void shellsort2(int a[], int n)  
	{  
	    int j, gap;  
	      
	    for (gap = n / 2; gap > 0; gap /= 2)  
	        for (j = gap; j < n; j++)//从数组第gap个元素开始  
	            if (a[j] < a[j - gap])//每个元素与自己组内的数据进行直接插入排序  
	            {  
	                int temp = a[j];  
	                int k = j - gap;  
	                while (k >= 0 && a[k] > temp)  
	                {  
	                    a[k + gap] = a[k];  
	                    k -= gap;  
	                }  
	                a[k + gap] = temp;  
	            }  
	}  */
//	该方法的基本思想是：先将整个待排元素序列分割成若干个子序列（由相隔某个“增量”的元素组成的）分别进行直接插入排序
//	，然后依次缩减增量再进行排序，待整个序列中的元素基本有序（增量足够小）时，再对全体元素进行一次直接插入排序。
//	因为直接插入排序在元素基本有序的情况下（接近最好情况），效率是很高的，因此希尔排序在时间效率上比前两种方法有较大提高。
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,2,4,8,9,7,1,3,0,5};
		shell_sort1(a,a.length);
		for(int i:a){
			System.out.println(i);
		}
	}
	
	public static void shell_sort(int[] s,int n){
		int i,j,gap;
		//步长的更新
		for(gap=n/2;gap>0;gap/=2){
			//每一次分组有多少子序列(多少组)需要进行插入排序
			for(i=0;i<gap;i++){
				//对一组数直接插入排序
				for(j=i+gap;j<n;j+=gap){
					if(s[j]<s[j-gap]){
						int temp=s[j];
						//k 找到插入的位置
						int k=j-gap;
						while(k>=0&&temp<s[k]){
							s[k+gap]=s[k];
							k-=gap;
						}
						s[k+gap]=temp;
					}
				}
			}
		}
	}
	
	public static void shell_sort1(int[] s,int n){
		int i,gap;
		//步长的更新
		for(gap=n/2;gap>0;gap/=2){
			for(i=gap;i<n;i++){//从数组第gap个元素开始  
				if(s[i]<s[i-gap]){//每个元素与自己组内的数据进行直接插入排序  
					int temp=s[i];
					int k=i-gap;
					while(k>=0&&s[k]>temp){
						s[k+gap]=s[k];
						k-=gap;
					}
					s[k+gap]=temp;
				}
			}
		}
		
	}
	
	
	

}
