/**
 * 
 */
package com.ls.li.Leetcode.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author lishuai
 * @data 2017-1-8 上午11:12:12
 */

public class MeetingRooms {

	/**
	 * @author lishuai
	 * @data 2017-1-8 上午11:12:12
Given an array of meeting time intervals consisting of start and end times 
[[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
For example,
Given [[0, 30],[5, 10],[15, 20]],
return false.
public class Interval {
	int start;
	int end;
	Interval() { start = 0; end = 0; }
	Interval(int s, int e) { start = s; end = e; }
}

[[0, 5],[5, 10],[15, 20]],
	 */

	public static void main(String[] args) {
		Interval i1 = new Interval(0,30);
		Interval i2 = new Interval(5,10);
		Interval i3 = new Interval(15,20);
		Interval[] intervals = {i1,i2,i3};
		System.out.println(canAttendMeetings(intervals));

	}
	/**
	 */
	//2 拆分intervals 然后比较是否有交叉
	 public static boolean canAttendMeetings(Interval[] intervals) {  
	        int[] starts = new int[intervals.length];  
	        int[] ends = new int[intervals.length];  
	        for(int i=0; i<intervals.length; i++) {  
	            starts[i] = intervals[i].start;  
	            ends[i] = intervals[i].end;  
	        }  
	        Arrays.sort(starts);  
	        Arrays.sort(ends);  
	        for(int i=1; i<intervals.length; i++) {  
	            if (ends[i-1] > starts[i]) return false;  
	        }  
	        return true;  
	    }  
	
	//1 通过自定义比较器，对整个intervals数组排序，然后比较是否有交叉
	public static boolean canAttendMeetings1(Interval[] intervals) {  
		 if (intervals == null || intervals.length == 0) {
			 return false;
		 }
		 Arrays.sort(intervals,new Comparator<Interval>() {
			@Override
			public int compare(Interval o1, Interval o2) {				
				return o1.start - o2.start;
			}
		});
		
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i - 1].start <= intervals[i].start && intervals[i].start < intervals[i - 1].end) {
				return false;
			}
		}
		 
		return true; 
	}

}
