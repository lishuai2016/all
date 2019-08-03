/**
 * 
 */
package com.ls.li.interview.ali;

import java.util.ArrayList;

/**
 * @author lishuai
 * @data 2017-4-28 上午11:18:09
 */

public class Qiefenshuzu {
	public static void main(String[] args) {


//		System.out.println(clipArray(new int[]{1,2,3,3},0,3,3));
		System.out.println(parseArray(new int[]{2 ,5 ,1 ,1 ,1 ,1 ,4 ,1 ,7 ,3 ,7 }));
		System.out.println(resolve(new int[]{1,1,1,1,1,1,1}));
// 2 5 1 1 1 1 4 1 7 3 7             1,1,1,1,1,1,1
	}
	public static boolean resolve(int[] arrays) {		
		int n = arrays.length;
		if (n < 7) {
			return false;
		}
		int leftSum = arrays[0];
		int rightSum = arrays[n - 1];
		if (leftSum == rightSum && (clipArray(arrays,2,n - 3, leftSum)) != -1) {
			return true;
		}
		int startIndex = 1;
		int endIndex = n - 2;
		int index= leftSum <= rightSum ? startIndex : endIndex;	    
		while (index >= startIndex && index <= endIndex) {
			if (leftSum <= rightSum) {
				leftSum += arrays[index];
				startIndex++;
			} else {
				rightSum += arrays[index];
				endIndex--;
			}
			if(leftSum == rightSum && clipArray(arrays,startIndex + 1,endIndex - 1,leftSum) != -1){
				return true;
			}			
		  index= leftSum <= rightSum ? startIndex : endIndex;  					
		}		
		return false;
	}
	
	public static int clipArray(int[] arrays,int start,int end,int target) {
		int leftSum = 0;
		int rightSum = 0;
		int position = -1;
		boolean flag = false;
		while(start <= end){
			if (flag) {
				rightSum += arrays[start];
			} else {
				leftSum += arrays[start];			
				if(leftSum == target) {
					flag = true;
					position = start + 1; 
					start++;
				}
			}
			 start++;
		}
		if(leftSum == rightSum) {
			return position;
		} else {
			return -1;
		}
	}	
	
	
	
	public static ArrayList<Integer> parseArray(int[] arrays) {
		ArrayList<Integer> pos=new ArrayList<Integer>();
		int len=arrays.length;
		if(len<7) return null;
		int start=arrays[0];
		int end=arrays[len-1];
		if(start==end){
		    int midclip= clipArray(arrays,2,len-3,start);
		    if(midclip>1) {
				pos.add(1);
				pos.add(len-2);
				pos.add(midclip);
				return pos;
		    }	    
		}
		int startIndex=1;
		int endIndex=len-2;
		int index=start<=end?1:len-2;	    
		while(index<=endIndex && index>=startIndex){
			if(start<=end) {
				start+=arrays[index];
				startIndex++;
			} else {
				end+=arrays[index];
				endIndex--;
			}
			if(start==end){
				  int mid= clipArray(arrays,startIndex+1,endIndex-1,start);
				    if(mid>1) {						
						pos.add(startIndex);
						pos.add(mid);
						pos.add(endIndex);
						return pos;
				     }			  
			}			
		  index=start<=end?startIndex:endIndex;  					
		}
		return pos;
	}
}
