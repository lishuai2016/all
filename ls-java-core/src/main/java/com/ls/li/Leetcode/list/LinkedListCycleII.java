/**
 * 
 */
package com.ls.li.Leetcode.list;

/**
 * @author lishuai
 * @data 2016-11-29 上午9:11:49
 */

public class LinkedListCycleII {

	/**
	 * @author lishuai
	 * @data 2016-11-29 上午9:11:49
	 * @param args
题142
Given a linked list, return the node where the cycle begins. If there is no cycle, return null.

Note: Do not modify the linked list.

Follow up:
Can you solve it without using extra space?

1	2	3	4
			10	5
					6
			9	7
			8	
	 */
//11 23 35 47 59 64 76 88
//	x=3 y=7 k=4
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

/**
 * 
思路1、假设x为从头结点到环开始的节点的距离，k为从环开始节点到相遇节点的距离，y为环的长度。
两个指针slow fast，slow每次走一步，fast每次走两步，则有
t = X + nY + K
2t = X + mY + K
化简可以得到：
X+K  =  (m-2n)Y   即 (k=y-x)
或者X = (Y - K) + (m - 2n - 1)Y
则可以得到 ：从相遇节点往下走到环的开始节点的距离和从head节点到环的开始节点的距离相等。
 */
	//1
	public ListNode detectCycle(ListNode head) {
		ListNode fast=head;
		ListNode slow=head;
		while(fast!=null&&fast.next!=null){
			slow=slow.next;
			fast=fast.next.next;
			if(fast==slow){
				break;
			}
		}
		if(fast==null||fast.next==null){
			return null;
		}
		//走到这里说明有环
		slow=head;
		while(slow!=fast){
			slow=slow.next;
			fast=fast.next;
		}
    	return slow;
    }
/**
	A bit explaination.
Provide the X is the length from head to the start point of circle and Y is the length of the circle. 
We know slow moves t, while fast moves 2t. 
They meet at K where is the length from the start point of the circle.
Then we have :

t = X + nY + K
2t = X + mY + K
, then we get

X+K  =  (m-2n)Y    (k=y-x)
which means when they meet at K, the length from K to start point of the circle is just the X. 
At this moment, we use a head pointer to move by the same step (=1), 
and they must meet at the start point of the circle which we want.
One further step to make it more clear:

X = (Y - K) + (m - 2n - 1)Y
which means by finishing the rest length of the circle and some number of circle lengths,
the traveled distance is equal to X.
*/
	// x=3 y=6 k=3

    public ListNode detectCycle1(ListNode head) {
    	ListNode slow = head;
		ListNode fast = head;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast){
				//System.out.println(slow.val);
				break;
			}
				
		}
		if (fast == null || fast.next == null) {
			return null;
		}
		slow = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}
		return slow;
    }
    //2 思路和1差不多，不过这里的时间复杂度为n*n
    public ListNode detectCycle2(ListNode head) {
    	if (head == null || head.next == null) {
            return null;   // no circle
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {  // circle detected
                while (head != fast) {
                    fast = fast.next;
                    head = head.next;
                }
                return head;
            }
        }
        return null; // no circle
    }
}
