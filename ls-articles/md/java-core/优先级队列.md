1、优先队列

/**
 *
 */
package queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author lishuai
 * @data 2016-12-19 下午2:40:36
 */

public class PriorityQueueExample {

    /**
     * @author lishuai
     * @data 2016-12-19 下午2:40:36
下面程序输出的结果
1,2,3,5,10,
-----------------------------
5,11,3,2,10,
     */

    public static void main(String[] args) {
        Queue<Integer> qi = new PriorityQueue<Integer>();

        qi.add(5);
        qi.add(2);
        qi.add(1);
        qi.add(10);
        qi.add(3);

        while (!qi.isEmpty()){
          System.out.print(qi.poll() + ",");
        }
        System.out.println();
        System.out.println("-----------------------------");

        Queue<Integer> q2 = new PriorityQueue<Integer>(5,new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 02 - 01;
            }

        });
        q2.add(5);
        q2.add(2);
        q2.add(11);
        q2.add(10);
        q2.add(3);
        while (!q2.isEmpty()){
              System.out.print(q2.poll() + ",");
            }
    }

}

