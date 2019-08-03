/**
 * 
 */
package com.ls.li.Leetcode.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * @author lishuai
 * @data 2017-1-8 上午11:41:25
 */

public class MeetingRoomsII {

	/**
	 * @author lishuai
	 * @data 2017-1-8 上午11:41:25
Given an array of meeting time intervals consisting of start and end times 
[[s1,e1],[s2,e2],...] (si < ei), find the minimum number of conference rooms required.

For example,
Given [[0, 30],[5, 10],[15, 20]],
return 2.
	 */

	public static void main(String[] args) {
		Interval i1 = new Interval(0,4);
		Interval i2 = new Interval(5,10);
		Interval i3 = new Interval(15,20);
		Interval[] intervals = {i1,i2,i3};
		System.out.println(minMeetingRooms(intervals));

	}
	//2
	public static int minMeetingRooms(Interval[] intervals) {
        Arrays.sort(intervals, new IntervalComparator());
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();  
        int rooms = 0;  
        for(int i = 0; i < intervals.length; i++) {  
            if(minHeap.size() == 0) {  
                minHeap.add(intervals[i].end);  
                rooms++;  
                continue;  
            }  
            if(minHeap.peek() <= intervals[i].start) {  
                minHeap.poll();  
                minHeap.add(intervals[i].end);  
            } else {  
                minHeap.add(intervals[i].end);  
                rooms++;  
            }  
        }  
        return rooms;  
    }  
}

class IntervalComparator implements Comparator<Interval> {
    public int compare(Interval a, Interval b) {
        return a.start - b.start;  
    }  
	
	/**
	首先interval数组按照start 排序 然后建立一个以end排序的priority queue 这样每次按照start的先后顺序入队，
	入队之前要把所有在这个start之前都结束的会议poll出来,每次更新room数量 
	
	
	 */
    //1
	public static int minMeetingRooms(Interval[] intervals) {
        Arrays.sort( intervals, new Comparator<Interval>(){
            public int compare (Interval int1, Interval int2 ){
                return int1.start - int2.start;  
            }  
        });  
        Queue<Interval> que = new PriorityQueue<Interval>(intervals.length, new Comparator<Interval>() {
        	@Override
            public int compare(Interval int1, Interval int2 ){
                return int1.end - int2.end;  
            }  
        });  
        int rooms = 0;  
        for (int i = 0; i < intervals.length; i ++ ) {  
            while (!que.isEmpty() && que.peek().end <= intervals[ i ].start ){  
                que.poll();  
            }  
            que.offer(intervals[ i ]);  
            rooms = Math.max (rooms,que.size() );  
        }  
        return rooms;  
    }  
}
