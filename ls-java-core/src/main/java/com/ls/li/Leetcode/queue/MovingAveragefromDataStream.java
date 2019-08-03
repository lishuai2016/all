/**
 * 
 */
package com.ls.li.Leetcode.queue;

/**
 * @author lishuai
 * @data 2017-1-10 上午11:45:43
 */

public class MovingAveragefromDataStream {

	/**
	 * @author lishuai
	 * @data 2017-1-10 上午11:45:43
Given a stream of integers and a window size,
 calculate the moving average of all integers in the sliding window.

For example,
MovingAverage m = new MovingAverage(3);
m.next(1) = 1
m.next(10) = (1 + 10) / 2
m.next(3) = (1 + 10 + 3) / 3
m.next(5) = (10 + 3 + 5) / 3
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//1 双端链表 优解
//	private LinkedList<Integer> dequeue = new LinkedList<>();  
//    private int size;  
//    private long sum;  
//  
//    /** Initialize your data structure here. */  
//    public MovingAveragefromDataStream(int size) {  
//        this.size = size;  
//    }  
//      
//    public double next(int val) {  
//        if (dequeue.size() == size) sum -= dequeue.removeFirst();  
//        dequeue.addLast(val);  
//        sum += val;   
//        return (double) sum / dequeue.size();  
//    }  
    //2
    private int[] vals;  
    private int from, size;  
    private long sum;  
  
    /** Initialize your data structure here. */  
    public MovingAveragefromDataStream(int size) {  
        this.vals = new int[size];  
    }  
      
    public double next(int val) {  
        if (size < vals.length) {  
            sum += val;  
            vals[size++] = val;  
        } else {  
            sum -= vals[from];  
            vals[from] = val;  
            from = (from+1) % vals.length;  
        }  
        return (double)sum / size;  
    }  
}
