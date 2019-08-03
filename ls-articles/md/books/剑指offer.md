剑指offer目录：
面试题1 赋值运算符函数
面试题2 实现Singleton模式
面试题3 二维数组中的查找
面试题4 替换空格
面试题5 从头到尾打印链表
面试题6 重建二叉树
面试题7 用两个栈实现队列
面试题8 旋转数组的最小数字
面试题9 斐波那契数列
面试题9 (变形) 跳台阶
面试题9 (变形) 变态跳台阶
面试题9 (变形) 矩形覆盖
面试题10 二进制中1的个数
面试题11 数值的整数次方
面试题12 打印1到最大的N位数
面试题13 在O(1)时间删除链表结点
面试题14 调整数组顺序使奇数位于偶数前面
面试题15 链表中倒数第k个结点
面试题16 反转链表
面试题17 合并两个排序的链表
面试题18 树的子结构
面试题19 二叉树的镜像
面试题20 顺时针打印矩阵
面试题21 包含min函数的栈
面试题22 栈的压入、弹出序列
面试题23 从上往下打印二叉树
面试题24 二叉搜索树的后序遍历序列
面试题25 二叉树中和为某一值的路径
面试题26 复杂链表的复制
面试题27 二叉搜索树与双向链表
面试题28 字符串的排列
面试题29 数组中出现次数超过一半的数字
面试题30 最小的K个数
面试题31 连续子数组的最大和
面试题32 从1到n整数中1出现的次数
面试题33 把数组排成最小的数
面试题34 丑数
面试题35 第一个只出现一次的字符
面试题36 数组中的逆序对
面试题37 两个链表的第一个公共结点
面试题38 数字在排序数组中出现的次数
面试题39 二叉树的深度
面试题40 数组中只出现一次的数字
面试题41 和为S的两个数字
面试题41 和为S的连续正数序列
面试题42 翻转单词顺序
面试题42 左旋转字符串
面试题43 N个骰子的点数
面试题44 扑克牌的顺子
面试题45 圆圈中最后剩下的数
面试题46 求1+2+……+n
面试题47 不用加减乘除做加法
面试题48 不能被继承的类 不适合在线模式
面试题49 把字符串转换成整数

面试题50 树中两个结点的最低公共祖先


1-10基础部分
sum up
基础知识，准备这方面的知识需要从编程语言、数据结构和算法3方面做准备。通常采用概念题、代码分析和编程题3种常见题型考察对某一编程语言的掌握程度。
数据结构一直是面试考察的重点，数组和字符串是两种最基本的数据结构。链表应该是面试题中使用频率最高的一种数据结构。如果面试官想加大面试难度，他很有可能会选用树（尤其是二叉树）相关的面试题。由于栈与递归调用密切相关，队列在图（包括树）的宽度优先遍历中需要用到，因此应聘者也需要掌握这两种数据结构。
算法也是面试考察的重点，查找（特别是二分查找）和排序（特别是快速排序和归并排序）是面试中最经常考察的算法，一定要熟悉掌握。另外还需掌握分析时间复杂度的方法，理解即使是同一思路，基于循环和递归的不同实现他们的时间复杂度可能大不相同。

位运算是针对二进制数字的运算规律，熟悉掌握二进制的与、或、异或运算以及左移、右移操作，就能解决与位运算相关的问题。

# 3：二维数组中查找元素
1     2     8     9
2     4     9     12
4     7     10     13
6     8     11     15
二维数组的每行每一列都是递增排序。在这个数组中查找数字7，有返回true；查找数字5，没有返回false；

思路：每次选取右上或者左下角的数字，然后逐渐缩小要查找的数组的范围（每比较一次，减少一行或者一列）

考点：二维数组在内存中的存储，在内存中占据连续的空间，从上到下存储每行，每一行从左到右存储，因此可以根据行号和列号计算出相对于数组首地址的偏移量，找到对应的元素。

解答：

# 4：替换空格
写一个函数，把一个字符串中的空格替换成%20（空格的ascii码为32，十六进制为0x20）
比如： we are happy        we%20are%20happy

思路：1、在原来的字符串上修改；（从前往后时间复杂度n*n;从后向前时间复杂度为n）
          2、重新分配空间修改；

解答：



4_1.有两个排序的数组A1和A2，内存在A1的末尾有足够多的空余空间容纳A2。请实现一个函数，把A2中的所有数字插入到A1中并且所有的数字是排序的。

# 5：从尾到头打印链表
题目：输入一个l链表的头节点，从尾到头反过来打印出每一个节点的值

思路：
1、修改链表的结构，把链表逆过来，在顺序输出（这种还没想好如何实现？？？）
2、不改变链表的结构，使用栈，把节点压入栈中，然后出栈即可（java中可以用list实现栈）

考点：
栈、栈的本质是递归，因此也可以用递归实现

解答：


#6.输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
假设输入的前序遍历和中序遍历的结果都不含重复的数字。
例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建出图2.6所示的二叉树并输出它的头结点。


7.用两个栈实现一个队列。队列的声明如下，请实现它的两个函数appendTail和deleteHead，分别完成在队列尾部插入节点和队列头部删除节点的功能。

7_1.用两个队列实现一个栈。


8.把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。





9.写一个函数，输入n，求斐波那契数列的第n项。
常见的解法：递归和迭代，推荐迭代
package offer;

public class Fibonacii {

    /**
     * @param args
     * 0 1 1 2 3 5 8 13
     */
    public static void main(String[] args) {
        System.out.println(fibonacii3(7));

    }
    //1迭代实现
    public static int fibonacii1(int n) {
        if (n == 0 || n == 1) return n;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2;i <= n;i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
    //2递归实现(消耗栈空间不推荐)
    public static int fibonacii2(int n) {
        if (n == 0 || n == 1) return n;
        return fibonacii2(n - 1) + fibonacii2(n - 2);
    }
    //3对1的优化
    public static int fibonacii3(int n) {
        if (n == 0 || n == 1) return n;
        int minOne = 1;
        int minTwo = 0;
        int resN = 0;
        for (int i = 2;i <= n;i++) {
            resN = minOne + minTwo;
            minTwo = minOne;
            minOne = resN;
        }
        return resN;
    }

}


假设f(n)是n个台阶跳的次数。

	1. f(1) = 1

	2. f(2) 会有两个跳得方式，一次1阶或者2阶，这回归到了问题f(1),f(2) = f(2-1) + f(2-2)

	3. f(3) 会有三种跳得方式，1阶、2阶、3阶，那么就是第一次跳出1阶后面剩下：f(3-1);第一次跳出2阶，剩下f(3-2)；第一次3阶，那么剩下f(3-3).因此结论是f(3) = f(3-1)+f(3-2)+f(3-3)

	4. f(n)时，会有n中跳的方式，1阶、2阶...n阶，得出结论：


f(n) = f(n-1)+f(n-2)+...+f(n-(n-1)) + f(n-n) => f(0) + f(1) + f(2) + f(3) + ... + f(n-1) == f(n) = 2*f(n-1)
所以，可以得出结论

附代码
public long jumpFloor(int n) {
    if (n <= 0)
        return -1;
    if (n == 1)
        return 1;
    return 2 * jumpFloor(n - 1);
}

9_1.一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级台阶总共有多少种跳法。

9_2.青蛙跳台阶问题中，如果把条件改成：一只青蛙一次可以跳上1级台阶，也可以跳上2级...它也可以跳上n级，此时青蛙跳上一个n级台阶总共有多少种跳法？

9_3.我们可以用2*1的小矩形横着活着竖着去覆盖更大的矩形。请问8个2*1的小矩形无重叠地覆盖一个2*8的大巨星，总共有多少种方法？




10、统计一个整数中的1的个数
java中的位操作（注意：除2和左移一位结果一样，但是移位的效率比除法要高）

~ 按位非（NOT）

& 按位与（AND）

| 按位或（OR）

^ 按位异或（XOR）

>> 右移

>>> 无符号右移

<<左移

10.请实现一个函数，输入一个整数，输出该二进制表示中1的个数。例如把9表示成二进制是1001，有2位是1。因此如果输入9，该函数输出2。
package offer;

public class NumberOf1 {

    /**
     * @param args
     -7
     00000000 00000000 00000000 00000111
     11111111 11111111 11111111 11111001
     */
    public static void main(String[] args) {
        //System.out.println(numberOf12(7));
        System.out.println(Integer.toBinaryString(-8));

    }
    /**
原理：通过1的不断左移与原来的数做与运算来判断对应的位是否为1
     */
    //1 效率需要循环整数二进制表示的位数，32位
    public static int numberOf1(int n) {
        int count = 0;
        int flag = 1;
        while (flag != 0) {
            if ((n & flag) != 0) count++;
            flag = flag << 1;
        }
        return count;
    }
    /**
原理：把一个整数减去1，再和自己做与运算，会把该整数最右边一个1变成0.
那么一个整数的二进制表示中有多少个1，就可以进行多少次这样的操作。
     */
    //2 对法1的优化（有几个1就进行几次操作）
    public static int numberOf12(int n) {
        int count = 0;

        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }
    //3 也可以通过内建函数Integer.toBinaryString(-8)转化为二进制字符串，然后统计1的个数

}

10_1.用一条语句判断一个整数是不是2的整数次方。一个整数如果是2的整数次方，那么它的二进制表示中有且只有一位是1，而其他所有位都是0。根据前面的分析，把这个整数减去1之后再和它自己做与运算，这个整数中唯一的1就会变成0。
10_2.输入两个整数m和n，计算需要改变m的二进制表示中的多少位才能得到n。比如10的二进制表示为1010，13的二进制表示为1101，需要改变1010中的3位才能得到1101 。 我们可以分为两步解决这个问题：第一步求这两个数的异或，第二步统计异或结果中1的位数。

10_3.把一个整数减去1之后再和原来的整数做位与运算，得到的结果相当于是把整数的二进制表示中的最右边一个1变成0 。 很多二进制的问题都可以用这个思路解决。




11.实现函数double Power(double base,int exponent)，求base的exponent次方。不得使用库函数，同时不需要考虑大数问题。


注意点：浮点型的数字不能直接比较是否相等，因为精度问题，使得两者的差在小于一个范围即可

测试用例：需要考虑到底数和指数为正负 0的情况

package offer;

public class Power {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Math.pow(3.1, -1));
        System.out.println(power(3.1, -1));
    }
    //定义全局标志位,参数输入错误
    public static boolean gInvalidInput = false;
    public static double power(double base,int e) {
        if (equals(base,0.0) && e < 0) {
            gInvalidInput = true;
            return 0.0;
        }
        double res = unsignedPower(base,Math.abs(e));
        if (e < 0) res = 1.0 / res;
        return res;
    }
    //优化  原理：要是求一个数的32次方，可以求16次方然后相乘，类似分下去，可以减少相乘的次数，递归实现
    public static double unsignedPower(double base,int e) {
        if (e == 0) return 1.0;
        if (e == 1) return base;
        //右移一位代替除2提高效率
        double result = unsignedPower(base,e >> 1);
        result *= result;
        //按位与操作判断是否为奇数
        if ((e & 1) == 1) result = result * base;
        return result;
    }

//  public static double unsignedPower(double base,int e) {
//      double res = 1.0;
//      for (int i = 1;i <= e;i++) res *= base;
//      return res;
//  }
    //判断两个浮点类型的数是否相等
    public static boolean equals(double b1,double b2) {
        if (Math.abs(b1 - b2) < 0.0000001) return true;
        else return false;
    }

}

（提示由于计算机表示小数float和double型都有误差，所以不能直接利用等号（==）判断两个小数是否相等。如果两个小数的绝对值很小，例如小于0.0000001，我们可以认为其相等）


11-18 高质量的代码

sum up

从规范性、完整性和鲁棒性3个方面介绍了如何在面试时写出高质量的代码。
规范性：书写清晰，布局清晰，命名合理
完整性：完成基本功能，考虑边界条件，做好错误处理

鲁棒性：采取防御式编程，处理无效的输入

鲁棒性：是指程序能够判断输入是否符合规范，对不规范的输入做相应的处理。也就是容错性



12.输入数字n，按顺序打印出从1最大的n位十进制数。比如输入3，则打印出1、2、3一直到最大的3位数即999 。

要的：需要考虑到大数问题，用合适的数据结构来表示大数


package offer;

public class Print1ToMaxOfNDigits {

    /**
     * @param args
     */
    public static void main(String[] args) {
        print1ToMaxOfNDigits(3);

    }

    //2 使用排列实现(简化代码，有点不太好理解)
    public static void print1ToMaxOfNDigits(int n) {
        if (n <= 0) return;
        int[] member = new int[n];
        //为最高位上的数字0~9
        for (int i = 0;i < 10;i++) {
            member[0] = i;
            print1ToMaxOfNDigitsRecursively(member, member.length, 0);
        }
    }
    public static void print1ToMaxOfNDigitsRecursively(int[] member,int length,int index) {
        if (index == length - 1) {
            printDigit(member);
            return;
        }
        for (int i = 0;i < 10;i++) {
            member[index + 1] = i;
            print1ToMaxOfNDigitsRecursively(member, length, index + 1);
        }
    }

    //1 使用数组来表示大数
    public static void print1ToMaxOfNDigits1(int n) {
        if (n <= 0) return;
        int[] member = new int[n];
        while (!increment(member)) {
            printDigit(member);
        }
    }
    public static boolean increment (int[] m) {
        //到最大数的标志位
        boolean overFlow = false;
        //进位标志
        int flag = 0;
        for (int i = m.length - 1;i >= 0;i--) {
            int num = m[i] + flag;
            if (i == m.length - 1) num++;
            //进位的操作
            if (num >= 10) {
                if (i == 0)  overFlow = true;
                else {
                    num -= 10;
                    flag = 1;
                    m[i] = num;
                }
            } else {
                m[i] = num;
                break;
            }
        }
        return overFlow;
    }
    //打印
    public static void printDigit (int[] m) {
        boolean isStart0 = true;
        for (int i = 0;i < m.length;i++) {
            if (isStart0 && m[i] != 0) isStart0 = false;
            if (!isStart0) System.out.print(m[i]);
        }

        System.out.println();
    }
}

12_1.前面的代码中，我们都是用一个char型字符表示十进制数字的一位。8个bit的char型字符最多能表示256个字符，而十进制数字只有0-9的10个数字。因此用char型字符串来表示十进制的数字并没有充分利用内存，有一些浪费。有没有更高效的方式来表示大数。

12_2.定义一个函数，在该函数中可以实现任意两个整数的加法。由于没有限定输入两个数的大小范围，我们也要把它当做大数问题来处理。在前面的代码的第一个思路中，实现了在字符串表示的数字上加1的功能，我们可以参考这个思路实现两个数字相加功能，另外还有一个需要注意的问题：如果输入的数字中有负数，我们应该怎么处理？
（如果面试题关于n位的整数并且没有限定n的取值范围，或者是输入任意大小的整数，那么这个题目很有可能是需要考虑大数问题的，字符串是一个简单，有效的表示大数的方法）


13.给定单向链表的头指针和一个结点指针，定义一个函数在O（1）时间删除该节点。链表的节点与函数的定义如下：
1 struct ListNode
2 {
3     int m_nValue;
4     ListNode* m_pNext;
5 };
6 void DeleteNode(ListNode** pListHead,ListNode* pToBeDeleted);

View Code


（提示：删除单向链表结点的两种方法
（a）删除结点i之前，先从链表的头结点开始遍历到i前面的一个节点h，把h的m_pNext指向i的下一个节点j，再删除结点i ， 这也是最经常用的一个方法，自己熟悉。

（b）把节点j的内容复制覆盖节点i，接下来再把节点i的m_pNext指向j的下一个节点之后删除结点j。这种方法不用遍历链表上结点i前面的节点，只需要有指向i结点的指针即可完成操作。）



14.输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。


package offer;

public class ReorderOddEven {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {1,2,3,4,5,6,7,8};
        reorderOddEven(a);
    }
    //二指针
    public static void reorderOddEven(int[] a) {
        if (a == null || a.length < 2) return;
        int low = 0;
        int high = a.length - 1;
        while (low < high) {
            while(low < high && (a[low] & 1) != 0) low++;
            while(low < high && (a[high] & 1) != 1) high--;
            if (low < high) {
                int temp = a[low];
                a[low] = a[high];
                a[high] = temp;
                low++;
                high--;
            }
        }
        System.out.println();
    }
}

（考虑可扩展性的解决方案，将判断奇偶的部分利用函数指针抽象出来达到解耦的效果）




15.输入一个链表，输出该链表中倒数第K个结点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾结点是倒数第1个结点。例如一个链表有6个结点，从头结点开始它们的值依次是1,2,3,4,5,6。这个链表的倒数第3个结点是值为4的结点。（注意代码鲁棒性，考虑输入空指针，链表结点总数少于k，输入的k参数为0）

要点：二指针处理

package offer;

public class FindKtoTail {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        n1.nextNode = n2;
//      n2.nextNode = n3;
//      n3.nextNode = n4;
//      n4.nextNode = n5;
//      n5.nextNode = n6;
        System.out.println(findKtoTail1(n1, 3));
    }
    //1要求只遍历一次链表(二指针)
    public static ListNode findKtoTail(ListNode head,int k) {
        if (head == null || k <= 0) return null;
        int count = 1;
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && count < k) {
            fast = fast.nextNode;
            count++;
        }
        if (fast == null) return null;
        while(fast.nextNode != null) {
            fast = fast.nextNode;
            slow = slow.nextNode;
        }
        return slow;
    }
    //2稍微简化一下1
    public static ListNode findKtoTail1(ListNode head,int k) {
        if (head == null || k <= 0) return null;
        ListNode fast = head;
        ListNode slow = head;
        for (int i = 0;i < k - 1;i++) {
            if (fast.nextNode != null) fast = fast.nextNode;
            else return null;
        }
        while(fast.nextNode != null) {
            fast = fast.nextNode;
            slow = slow.nextNode;
        }
        return slow;
    }
}

15_1.求链表的中间结点。如果链表中结点总数为奇数，返回中间结点；如果结点总数为偶数，返回中间两个结点的任意一个。（通过一次遍历解决这个问题）

15_2.判断一个单项链表是否形成了环形结构。（提示：速度不同的链表指针遍历，类似于操场跑步，
当我们用一个指针遍历链表不能解决问题的时候，可以尝试利用两个指针来遍历链表，可以让其中一个指针遍历的速度快一些，比如一次在链表上走两步，或者让它先在链表上走若干步）




16.定义一个函数，输入一个链表的头结点，反转该链表并输出反转后链表的头结点。

package offer;

public class ReverseList {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        n1.nextNode = n2;
        n2.nextNode = n3;
        n3.nextNode = n4;
        n4.nextNode = n5;
        n5.nextNode = n6;
        reverseList(n1);
        System.out.println();

    }
    //通过三个指针实现 1 2 3
    public static void reverseList(ListNode head) {
        if (head == null || head.nextNode == null) return;
        ListNode reverseHead = null;
        ListNode pNode = head;
        ListNode pPrev = null;
        while (pNode != null) {
            ListNode pNext = pNode.nextNode;
            if (pNext == null) reverseHead = pNode;
            pNode.nextNode = pPrev;

            pPrev = pNode;
            pNode = pNext;
        }
        System.out.println();
    }
}

16_1.递归实现同样的反转链表的功能。



17.输入两个递增排序的链表，合并这两个链表并使新链表中结点仍然是按照递增排序的。例如输入1->3->5->7和2->4->6->8，则合并之后的升序链表应该是1->2->3->4->5->6->7->8 。

两种方式：一般和递归

package offer;

public class MergeTwoSortList {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        ListNode n7 = new ListNode(7);
        ListNode n8 = new ListNode(8);
        n1.nextNode = n3;
        n3.nextNode = n5;
        n5.nextNode = n7;
        n2.nextNode = n4;
        n4.nextNode = n6;
        n6.nextNode = n8;
        ListNode t = mergeTwoSortList1(n1, n2);
        System.out.println(t.toString());

    }
    //2递归实现
    public static ListNode mergeTwoSortList1(ListNode head1,ListNode head2) {
        if (head1 == null) return head2;
        if (head2 == null) return head1;
        ListNode newHead = null;
        if (head1.value < head2.value) {
            newHead = head1;
            newHead.nextNode = mergeTwoSortList1(head1.nextNode, head2);
        } else {
            newHead = head2;
            newHead.nextNode = mergeTwoSortList1(head1, head2.nextNode);
        }
        return newHead;
    }
    //1
    public static ListNode mergeTwoSortList(ListNode head1,ListNode head2) {
        if (head1 == null) return head2;
        if (head2 == null) return head1;
        ListNode p1 = head1;
        ListNode p2 = head2;
        ListNode newHead = null;
        if (p1.value < p2.value) {
            newHead = p1;
            p1 = p1.nextNode;
        } else {
            newHead = p2;
            p2 = p2.nextNode;
        }
        //节点始终指向新链表的结尾
        ListNode p = newHead;
        while (p1 != null && p2 != null) {
            if (p1.value < p2.value) {
                p.nextNode = p1;
                p = p1;
                p1 = p1.nextNode;

            } else {
                p.nextNode = p2;
                p = p2;
                p2 = p2.nextNode;
            }
        }
        while (p1 != null) {
            p.nextNode = p1;
            p = p1;
            p1 = p1.nextNode;
        }
        while (p2 != null) {
            p.nextNode = p2;
            p = p2;
            p2 = p2.nextNode;
        }
        return newHead;
    }
}


18.输入两颗二叉树A和B，判断B是不是A的子结构。二叉树结点的定义如下：
1 struct BinaryTreeNode
2 {
3     int m_nValue;
4     BinaryTreeNode* m_pLeft;
5     BinaryTreeNode* m_pRight;
6 };

package offer;

public class HasSubTree {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(8);
        TreeNode n2 = new TreeNode(8);
        TreeNode n3 = new TreeNode(7);
        TreeNode n4 = new TreeNode(9);
        TreeNode n5 = new TreeNode(2);
        TreeNode n6 = new TreeNode(4);
        TreeNode n7 = new TreeNode(7);

        TreeNode n8 = new TreeNode(8);
        TreeNode n9 = new TreeNode(9);
        TreeNode n10 = new TreeNode(2);
        n1.left = n2;
        n1.right = n3;
        n2.left = n4;
        n2.right = n5;
        n5.left = n6;
        n5.right = n7;

        n8.left = n9;
        n8.right = n10;
        System.out.println(hasSubTree(n1,n8));

    }
    /**
    分两步：
    1、在1树中找和2树根节点值相等的节点R
    2、在判断1树中以R为根节点的子树是不是和2树有一样的结构
     */

    //1递归查找R
    public static boolean hasSubTree(TreeNode t1,TreeNode t2) {
        boolean res = false;
        if (t1 != null && t2 != null) {
            if (t1.value == t2.value) res = doesTree1HasTree2(t1,t2);
            if (!res) res = hasSubTree(t1.left,t2);
            if (!res) res = hasSubTree(t1.right,t2);
        }
        return res;
    }
    //2遍历是否是结构相同
    public static boolean doesTree1HasTree2(TreeNode t1,TreeNode t2) {
        if (t2 == null) return true;
        if (t1 == null) return false;
        if (t1.value != t2.value) return false;
        return doesTree1HasTree2(t1.left,t2.left) && doesTree1HasTree2(t1.right,t2.right);
    }
}



19.请完成一个函数，输入一个二叉树，该函数输出它的镜像。（递归和循环应该分别怎样实现？）
package offer;

public class MirrorTree {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(8);
        TreeNode n2 = new TreeNode(6);
        TreeNode n3 = new TreeNode(10);
        TreeNode n4 = new TreeNode(5);
        TreeNode n5 = new TreeNode(7);
        TreeNode n6 = new TreeNode(9);
        TreeNode n7 = new TreeNode(11);
        n1.left = n2;
        n1.right = n3;
        n2.left = n4;
        n2.right = n5;
        n3.left = n6;
        n3.right = n7;
        mirrorTree(n1);

    }
    public static void mirrorTree(TreeNode root) {
        if (root == null) return;
        if (root.left == null && root.right == null) return;
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        if (root.left != null) mirrorTree(root.left);
        if (root.right != null) mirrorTree(root.right);
    }
}


19-28 解决面试题的思路
sum up
面试的时候难免会遇到难题，画图、举例子和分解者三种办法能够帮助解决复杂的问题。
图形能够使抽象的问题形象化，当涉及链表、二叉树等数据结构时，如果在纸上画几张草图，题目中隐藏的规律就有可能变得很直观。
一两个例子能使得抽象的问题具体化。

复杂问题分解成若干个小问题，是解决很多复杂问题的有效方法。如果我们遇到的问题很大，尝试先把大问题分解成小问题，然后递归的解决这些小问题。分治法、动态规划等方法都是基于这种思路。



20.输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。例如：如果输入如下矩阵：
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16

则依次打印出数字1、2、3、4、8、12、16、15、14、13、9、5、6、7、11、10 。




21.定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的min函数。在该栈中，调用min、push以及pop的时间复杂度都是O(1)。

思路：额外增加一个辅助栈用来保存对应栈顶元素时的最小值（两个栈的大小一样）

package offer;

import java.util.Stack;

public class MyStack {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static Stack<Integer> dataStack = new Stack<>();
    public static Stack<Integer> minStack = new Stack<>();


    public static void myPush(Integer value) {
        dataStack.push(value);
        if (dataStack.size() == 0 || minStack.peek() > value) {
            minStack.push(value);
        } else {
            minStack.push(minStack.peek());
        }
    }
    public static void myPop(Integer value) {
        if (minStack.size() > 0 && dataStack.size() > 0) {
            dataStack.pop();
            minStack.pop();
        }
    }
    public static int myMin(Integer value) {
        int res = -1;
        if (minStack.size() > 0 && dataStack.size() > 0) {
            res = minStack.peek();
        }
        return res;
    }

}




22.输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否为该栈的弹出顺序。假设压入栈的所有数字均不相等。例如序列1、2、3、4、5是某栈的压栈序列，序列4、5、3、2、1是该压栈序列对应的一个弹出序列，但4、3、5、1、2就不可能是该压栈序列的弹出序列。
思路：借助一个辅助栈来模拟压站和出栈过程

如果下一个弹出的数字刚好是栈顶的数字，那么直接弹出。如果下一个弹出的数字不是栈顶，我们把压站序列中还没有压站的数字压入辅助栈，直到把下一个需要弹出的数字压入栈顶为止。如果所有的数字都压入栈了仍然没有找到下一个要弹出的数字，那么该序列是一个不可能的弹出序列

package offer;

import java.util.Stack;

public class IsPopOrder {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {1,2,3,4,5};
        int[] b = {4,3,5,1,2};
System.out.println(isPopOrder(a,b));
    }

    public static boolean isPopOrder(int[] push, int[] pop) {
        if (push == null || pop == null || push.length != pop.length || push.length == 0) {
            return false;
        }
        boolean res = false;
        Stack<Integer> stack = new Stack<>();
        int i_push = 0;
        int i_pop = 0;
        while (i_pop < pop.length) {
            while (stack.empty() || stack.peek() != pop[i_pop]) {
                if (i_push == push.length) {
                    break;
                }
                stack.push(push[i_push]);
                i_push++;
            }
            if (stack.peek() != pop[i_pop]) {
                break;
            }
            stack.pop();
            i_pop++;
        }
        if (stack.empty() && i_pop == pop.length) {
            res = true;
        }

        return res;
    }

}




23.从上往下打印出二叉树的每个结点，同一层的结点按照从左到右的顺序打印。（二叉树的层序遍历）

package offer;

import java.util.LinkedList;
import java.util.Queue;

public class PrintFromTopToButton {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(8);
        TreeNode t2 = new TreeNode(6);
        TreeNode t3 = new TreeNode(10);
        TreeNode t4 = new TreeNode(5);
        TreeNode t5 = new TreeNode(7);
        TreeNode t6 = new TreeNode(9);
        TreeNode t7 = new TreeNode(11);
        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        t3.left = t6;
        t4.right = t7;

        printFromTopToButton(t1);
    }

    public static void printFromTopToButton(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (queue.size() != 0) {
            TreeNode temp = queue.poll();
            System.out.println(temp.value);
            if (temp.left != null) {
                queue.offer(temp.left);
            }
            if (temp.right != null) {
                queue.offer(temp.right);
            }
        }
    }
}

23_1.如何广度优先遍历一个有向图？这同样也可以基于队列实现。树是图的一种特殊退化形式，从上到下按层遍历二叉树，从本质上来讲就是广度优先遍历二叉树。


23_2.不管是广度优先遍历一个有向图还是一棵树，都要用到队列。第一步我们把起始结点（对树而言是跟结点）放入队列中，接下来每一次从队列的头部取出一个结点，遍历这个结点之后把从它能达到的结点（对树而言是子结点）都一次放入队列。我们重复这个遍历过程，直到队列中的结点全部被遍历为止。




24.输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。如果是则返回true，否则返回false。假设输入的数组的任意两个数字都互不相同。
package offer;

public class VerifySqueueOfBST {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {7,4,6,5};
        System.out.println(verifySqueueOfBST(a,0,a.length));

    }
    //思路：递归，分四步
    public static boolean verifySqueueOfBST(int[] sequeue, int start, int length) {
        if (length == 0) {
            return false;
        }
        int root = sequeue[length - 1];
        //在二叉树搜索树的左子树的节点都小于根节点
        int i = 0;
        while (i < length - 1) {
            if (sequeue[i] > root) {
                break;
            }
            i++;
        }
        //在二叉树搜索树的右子树的节点都大于根节点
        int j = i;
        while (j < length - 1) {
            if (sequeue[j] < root) {
                return false;
            }
            j++;
        }
        //判断左子树是不是二叉搜索你树
        boolean  left = true;
        if (i > 0) {
            left = verifySqueueOfBST(sequeue,0,i);
        }
        //判断右子树是不是二叉搜索你树
        boolean right = true;
        if (i < length - 1) {
            right = verifySqueueOfBST(sequeue,i,length - i - 1);
        }
        return left && right;
    }

}

24_1.输入一个整数数组，判断该数组是不是某二叉搜索树的前序遍历的结果。这和前面的问题的后序遍历很类似，只是前序遍历的序列中，第一个数字是根结点的值。

24_1.如果面试题是要求处理一颗二叉树的遍历序列，我们可以先找到二叉树的根结点，再基于根结点把整棵树的遍历序列拆分成左子树对应的子序列和右子树对应的子序列，接下来再递归地处理这两个子序列。



25.输入一颗二叉树和一个整数，打印出二叉树中结点值的和为输入整数的所有路径。从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
思路：二叉树的先序遍历和栈的结合

package offer;

import java.util.Iterator;
import java.util.Stack;

public class FindPath {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(10);
        TreeNode t2 = new TreeNode(5);
        TreeNode t3 = new TreeNode(12);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(7);

        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        findPath(t1,22);

    }
    public static void findPath(TreeNode root, int target) {
        if (root == null) {
            return;
        }
        Stack<Integer> path = new Stack<>();
        findPath(root, target, path, 0);
    }
    public static void findPath(TreeNode root, int target,Stack<Integer> path, int currentSum) {
        currentSum += root.value;
        path.push(root.value);
        //判断要是叶子节点并且和为目标值打印
        boolean isLeaf = (root.left == null) && (root.right == null);
        if (isLeaf && currentSum == target) {
            Iterator<Integer> iterator = path.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
        //如果不是叶子节点，则遍历他的子节点
        if (root.left != null) {
            findPath(root.left, target, path, currentSum);
        }
        if (root.right != null) {
            findPath(root.right, target, path, currentSum);
        }
        //在返回到父节点之前，删除路径上的当前节点
        path.pop();
    }
}


26.请实现函数ComplexListNode* Clone(ComplexListNode* pHead)，复制一个复杂链表。在复杂链表中，每个结点除了有一个m_pNext指针指向下一个结点外，还有一个m_pSibling指向链表中的任意结点或者NULL。结点C++的定义如下：
1 struct ComplexListNode
2 {
3     int m_nValue;
4     ComplexListNode* m_pNext;
5     COmplexListNode* m_pSibling;
6 };

核心：把一个大问题拆分成几个小问题求解

package offer;

public class ComplexListNodeClone {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    public static ComplexListNode clone(ComplexListNode head) {
        cloneNode(head);
        connectSibling(head);
        return reConnect(head);
    }

    //第一步在每一个节点后面复制一个它自己
    public static void cloneNode(ComplexListNode head) {
        ComplexListNode p = head;
        while (p != null) {
            ComplexListNode temp = p.next;
            ComplexListNode node = new ComplexListNode();
            node.value = p.value;
            node.next = temp;
            node.sibling = null;
            p.next = node;
            p = temp;
        }
    }
    //第二部，拼接节点的sibling
    public static void connectSibling(ComplexListNode head) {
        ComplexListNode p = head;
        while (p != null) {
            ComplexListNode clone = p.next;
            if (p.sibling != null) {
                clone.sibling = p.sibling.next;
            }
            p = clone.next;
        }
    }
    //3拆分
    public static ComplexListNode reConnect(ComplexListNode head) {
        ComplexListNode p = head;
        ComplexListNode cloneHead = null;
        ComplexListNode cloneNode = null;
        if (p != null) {
            cloneHead = p.next;
            cloneNode = p.next;
            p.next = cloneNode.next;
            p = p.next;
        }
        while (p != null) {
            cloneNode.next = p.next;
            cloneNode = cloneNode.next;
            p.next = cloneNode.next;
            p = p.next;
        }
        return cloneHead;
    }

}




27.输入一颗二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。要求不能创建人和新的结点，只能调整树中结点指针的指向。
思路：中序遍历

不过下面的答案有问题，参数传递

package offer;

public class Convert {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(10);
        TreeNode t2 = new TreeNode(6);
        TreeNode t3 = new TreeNode(14);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(8);
        TreeNode t6 = new TreeNode(12);
        TreeNode t7 = new TreeNode(16);
        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        t3.left = t6;
        t4.right = t7;
        convert(t1);
    }
    public static TreeNode convert(TreeNode root) {
        TreeNode lastNodeInList = null;
        covertNode(root, lastNodeInList);
        TreeNode headList = lastNodeInList;
        while (headList != null && headList.left != null) {
            headList = headList.left;
        }
        return headList;
    }
    public static void covertNode(TreeNode root, TreeNode lastNodeInList) {
        if (root == null) {
            return;
        }
        TreeNode cur = root;
        //处理左子树节点
        if (cur.left != null) {
            covertNode(cur.left, lastNodeInList);
        }
        cur.left = lastNodeInList;
        if (lastNodeInList != null) {
            lastNodeInList.right = cur;
        }
        //处理根节点
        lastNodeInList = cur;
        //处理右子树节点
        if (cur.right != null) {
            covertNode(cur.right, lastNodeInList);
        }
    }
}




28.输入一个字符串，打印出该字符串中字符的所有排列。例如输入字符串abc，则打印出由字符串a、b、c所能排列出来的所有字符串abc、acb、bac、bca、cab和cba。





28_1.如果不是求字符的所有排列，而是求字符的所有组合，应该怎么办？还是输入三个字符a、b、c，则它们的组合有a、b、c、ab、ac、bc、abc。当交换字符串中两个字符时，虽然能得到两个不同的排列，但却是同一个组合。比如ab和ba是不同的排列，但只算一个组合。

28_2.当输入一个含有8个数字的数组，判断有没有可能把这8个数字分别放到正方体的8个顶点上，使得正方体上三组相对的面上的4个顶点的和都相等。

28_3.在8*8的国际象棋上摆放8个皇后，使其不能相互攻击，及任意两个皇后不得处于同一行，同一列或者同意对角线上，请问总共有多少种符合条件的摆法。


28_4.如果面试题是按照一定要求摆放若干数字，我们可以先求出这些数字的所有排列，然后再一一判断每个排列是不是满足题目给定的要求。




29.数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。例如输入一个长度为9的数组{1,2,3,2,2,2,5,4,2}。由于数字2在数组中出现了5次，超过数组长度的一半，因此输出2。

时间复杂度N
两种方案：第一使用partition函数？？？
第二就是下面的这种

package offer;

public class MoreThanHalfNum {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {1,1,1,2,2,2};
        System.out.println(moreThanHalfNum(a));
    }
    //当输入的数组不满足条件时返回-1
    public static int moreThanHalfNum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int result = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == result) {
                count++;
            } else if (count == 0) {
                result = nums[i];
                count = 1;
            } else {
                count--;
            }
        }
        if (count != 0) {
            return result;
        } else {
            return -1;
        }

    }
}



29-37 优化时间和空间效率

sum up

降低时间复杂度的第一个方法是改用更加高效的算法。
降低时间复杂度的第二个方法是用空间换取时间，另外我们可以创建一个缓存保存中间的计算结果，从而避免重复计算。递归中会出现很多重复计算，所以这种保存已经计算的结果成为“记账法”。

空间和时间应该进行权衡，嵌入式系统的内存有限，所以不一定非要花费空闲去换取时间。



30.输入n个整数，找出其中最小的k个数。例如输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4 。

partition函数？？？
最大堆？？
红黑树？？



两种方案：
1、partition函数   时间 复杂度N
2、使用一个容器来保存k个数，遍历数组的时候，当容器中的元素个数小于k时，直接加入，要是容器满的时候需要比较当前的元素和容器的最大元素比较，要是大于直接丢弃，小于的话删除容器中的最大值，把当前值加入容器（考虑用红黑树实现，时间复杂度Nlogk）

第二种方法适合处理海量数据

/**
 *
 */
package array;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author lishuai
 * @data 2017-1-8 下午2:11:32
 */

public class KthLargestElementinanArray {

    /**
     * @author lishuai
     * @data 2017-1-8 下午2:11:32
Find the kth largest element in an unsorted array.
Note that it is the kth largest element in the sorted order, not the kth distinct element.

For example,
Given [3,2,1,5,6,4] and k = 2, return 5.

Note:
You may assume k is always valid, 1 ≤ k ≤ array's length.
     */

    public static void main(String[] args) {
        int[] a = {-1,2,0};
        System.out.println(findKthLargest(a,2));
    }
    /**
You can take a couple of approaches to actually solve it:

O(N lg N) running time + O(1) memory
The simplest approach is to sort the entire input array and
then access the element by it's index (which is O(1)) operation:
     */
    //4  3ms
    public static int findKthLargest(int[] nums, int k) {
        int N = nums.length;
        Arrays.sort(nums);
        return nums[N - k];
}

    /**
    O(N lg K) running time + O(K) memory
Other possibility is to use a min oriented priority queue that will store the K-th largest values.
The algorithm iterates over the whole input and maintains the size of priority queue.
     */
    //3  最大堆PriorityQueue     14ms
    public static int findKthLargest3(int[] nums, int k) {

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for(int val : nums) {
            pq.offer(val);

            if(pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
    }
    /**
Initialize left to be 0 and right to be nums.size() - 1;
Partition the array, if the pivot is at the k-1-th position, return it (we are done);
If the pivot is right to the k-1-th position, update right to be the left neighbor of the pivot;
Else update left to be the right neighbor of the pivot.
Repeat 2.
     */
    //1   和2类似   59ms
    public static int findKthLargest1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (k <= 0) {
            return 0;
        }
        int left = 0;
        int right = nums.length - 1;
        while (true) {
            int position = partition1(nums, left, right);
            if (position == nums.length - k) {
                return nums[position];
            } else if (position < nums.length - k) {
                left = position + 1;
            } else {
                right = position - 1;
            }
        }           
    }   
    public static int partition1(int[] nums, int l, int r) {
        int left = l;
        int right = r;
        int pivot = nums[left];

        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            if (left < right) {
                nums[left] = nums[right];
                left++;
            }
            while (left < right && nums[left] < pivot) {
                left++;
            }
            if (left < right) {
                nums[right] = nums[left];
                right--;
            }                     
        }

        nums[left] = pivot;
        return left;
    }


    //2九章   44ms
    public static int kthLargestElement2(int k, int[] nums) {
        // write your code here
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (k <= 0) {
            return 0;
        }
        return helper(nums, 0, nums.length - 1, nums.length - k + 1);

    }
    public static int helper(int[] nums, int l, int r, int k) {
        if (l == r) {
            return nums[l];
        }
        int position = partition(nums, l, r);
        if (position + 1 == k) {
            return nums[position];
        } else if (position + 1 < k) {
            return helper(nums, position + 1, r, k);
        }  else {
            return helper(nums, l, position - 1, k);
        }
    }
    public static int partition(int[] nums, int l, int r) {
        // 初始化左右指针和pivot
        int left = l, right = r;
        int pivot = nums[left];

        // 进行partition
        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }

        // 返还pivot点到数组里面
        nums[left] = pivot;
        return left;         
    }
}




31.输入一个整型数组，数组里有正数，也有负数。数组中一个或连续的多个整数组成一个子数组。求所有子数组的和的最大值。要求时间复杂度为O(n）。
两种方法：
1、累加子数组
2、使用动态规划？？

package offer;

public class SumOfSubArray {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {1,-2,3,10,-4,7,2,-5};
        System.out.println(sumOfSubArray1(a));
    }
    //1自己的
    public static int sumOfSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int sumMax = 0;
        int localMax = 0;
        for (int i = 0; i < nums.length; i++) {
            localMax += nums[i];
            if (localMax < nums[i]) {
                localMax = nums[i];
            }
            sumMax = Math.max(localMax, sumMax);
        }

        return sumMax;
    }
    //2书上的
    public static int sumOfSubArray1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int sumMax = 0;
        int localMax = 0;
        for (int i = 0; i < nums.length; i++) {
            if (localMax <= 0) {
                localMax = nums[i];
            } else {
                localMax += nums[i];
            }
            sumMax = Math.max(localMax, sumMax);
        }

        return sumMax;
    }

}




32.输入一个整数n，求从1到n这n个整数的十进制表示中1出现的次数。例如输入12，从1到12这些整数中包含1的数字有1,10,11和12,1一共出现5次。

未完，下面的解法不是最优解

package offer;

public class NumberOf1AndN {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(numberOf1AndN(12));
    }
    //1比较直观的答案 时间复杂度 O(N*logN)
    public static int numberOf1AndN(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            int j = i;
            while (j != 0) {
                if (j % 10 == 1) {
                    count++;
                }
                j /=10;
            }
        }
        return count;
    }
}



33.输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出所有数字中最小的一个。例如输入数组{3,32,321}，则打印出这3个数字能排成的最小数字321323。
核心：
自定义排序规则mn和nm组合的比较，较大的数排在前面

public static String largestNumber0(int[] nums) {
     StringBuilder sb = new StringBuilder();
     String[] s = new String[nums.length];
     for (int i = 0; i < nums.length; i++) {
          s[i] = Integer.toString(nums[i]);
     }
     Arrays.sort(s, new Comparator<String>() {
              @Override
              public int compare(String o1, String o2) {
                   return (o2 + o1).compareTo(o1 + o2);
              }
          });
     for (String i : s) {
          sb.append(i);
     }
     if (sb.charAt(0) == '0') {
          return "0";
     }
     return sb.toString();
    }




34.我们把只包含因子2,3和5的数称作丑数。求按从小到大的顺序的第1500个丑数。例如6、8都是丑数，但14不是，因为它包含因子7.习惯上我们把1当做第一个丑数。


package offer;

public class UglyNumbers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getUglyNumbers(4));

    }
    //2、减少不是丑数的计算，用数组保存所有之前所有的丑数（因为后面的丑数都是前面的丑数乘以2,3,5得到的）
    //每次大循环增加一个丑数
    public static int getUglyNumbers(int index) {
        if (index <= 0) {
            return 0;
        }
        int[] pUglyNumbers = new int[index];
        pUglyNumbers[0] = 1;
        int nextUglyIndex = 1;
        int mul2 = 0;
        int mul3 = 0;
        int mul5 = 0;
        while (nextUglyIndex < index) {
            int min = min(pUglyNumbers[mul2] * 2, pUglyNumbers[mul3] * 3, pUglyNumbers[mul5] * 5);
            pUglyNumbers[nextUglyIndex] = min;
            while (pUglyNumbers[mul2] * 2 <= pUglyNumbers[nextUglyIndex]) {
                mul2++;
            }
            while (pUglyNumbers[mul3] * 3 <= pUglyNumbers[nextUglyIndex]) {
                mul3++;
            }
            while (pUglyNumbers[mul5] * 5 <= pUglyNumbers[nextUglyIndex]) {
                mul5++;
            }
            nextUglyIndex++;
        }
        return pUglyNumbers[index - 1];
    }
    public static int min(int a, int b, int c) {
        int min = Math.min(a, b);
        return Math.min(min, c);
    }

    //1第n个丑数  这种方法效率有点低
    public static int getUglyNumbers1(int n) {
        if (n <= 0) {
            return 0;
        }
        int number = 0;
        int count = 0;
        while (count < n) {
            number++;
            if (isUgly(number)) {
                count++;
            }
        }
        return number;
    }

    //判断一个数是不是丑数
    public static boolean isUgly(int x) {
        while (x % 2 == 0) {
            x /= 2;
        }
        while (x % 3 == 0) {
            x /= 3;
        }
        while (x % 5 == 0) {
            x /= 5;
        }
        return x == 1 ? true : false;
    }

}



35.在字符串中找出第一个只出现一次的字符。如输入"abaccdeff"，则输出'b'。
思路：
一，使用两层循环，把当前字符和后面的所有比较一下，看看有没有重复的。时间复杂度N*N
二、使用hash统计字符出现的次数，由于字符的特殊性可以使用数字简单替代hashtable。时间复杂度N，空间复杂度O（1）

package offer;

public class FirstNotRepeatChar {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(firstNotRepeatChar("abaccdeff"));

    }
    public static char firstNotRepeatChar(String s) {
        int[] dp = new int[256];
        for (int i = 0; i < s.length(); i++) {
            dp[s.charAt(i)]++;
        }
        for (int i = 0; i < s.length(); i++) {
            if (dp[s.charAt(i)] == 1) {
                return s.charAt(i);
            }
        }
        return ' ';
    }
}

35_1.在前面的例子中，我们之所以可以把哈希表的大小设为256，是因为字符（char）是8个bit的类型，总共只有256个字符。但实际上字符不只是256个，比如中文就有几千个汉字。如果题目要求考虑汉字，前面的算法是不是有问题？如果有，可以怎么解决。
35_2.定义一个函数，输入两个字符串，从第一个字符串中删除在第二个字符串中出现过的所有字符。例如第一个字符串"we are students"，第二个字符串是"aeiou"，结果应该是"w r stdnts"。
35_3.定义一个函数，删除字符串中所有重复出现的字符。例如输入"google"，则输出结果应该是"gole"。
35_4.请完成一个函数，判断输入的两个字符串是否是Anagram。

35_5.如果需要判断多个字符是不是在某个字符串里出现过或者统计多个字符在某个字符串中出现的次数，我们可以考虑基于数组创建一个简单的哈希表。这样可以用很小的空间消耗换来时间效率的提升。



36.在数组中的两个数字如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。
核心：实质是归并排序
下面的代码有点问题

package offer;

import java.util.Arrays;

public class InversePaires {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {7,5,6,4};
        System.out.println(inversePaires(a));
    }
    public static int inversePaires(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        return inversePairesCore(nums, Arrays.copyOf(nums, nums.length), 0, nums.length - 1);
    }
    public static int inversePairesCore(int[] nums, int[] copy, int start, int end) {
        if (start == end) {
            copy[start] = nums[start];
            return 0;
        }
        int length = (end - start) / 2;
        int left = inversePairesCore(copy, nums, start, start + length);
        int right = inversePairesCore(copy, nums, start + length + 1, end);
        int i = start + length;
        int j = end;
        int indexCopy = end;
        int count = 0;
        while (i >= start && j >= start + length + 1) {
            if (nums[i] > nums[j]) {
                copy[indexCopy--] = nums[i--];
                count += j - start - length;
            } else {
                copy[indexCopy--] = nums[j--];
            }
            for (; i >= start; --i) {
                copy[indexCopy--] = nums[i];
            }
            for (; j >= start + length + 1; --j) {
                copy[indexCopy--] = nums[j];
            }
        }
        return count + left + right;
    }

}



37.输入两个单向链表，找出它们的第一个公共结点。
核心：二指针，一个比另外一个先走一定的步数

package offer;

public class FindFirstCommonNode {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        ListNode n7 = new ListNode(7);
        n1.nextNode = n2;
        n2.nextNode = n3;
        n3.nextNode = n6;
        n4.nextNode = n5;
        n5.nextNode = n6;
        n6.nextNode = n7;
        findFirstCommonNode(n1,n4);
    }
    //思路：先计算两个链表的长度，然后让长的先走两者的差，最后同时走，第一个相同的节点即使所求
    public static ListNode findFirstCommonNode(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        int l1 = getListLength(head1);
        int l2 = getListLength(head2);
        ListNode p1 = head1;
        ListNode p2 = head2;
        if (l1 >= l2) {
            for (int i = 0; i < l1 - l2; i++) {
                p1 = p1.nextNode;
            }
        } else {
            for (int i = 0; i < l2 - l1; i++) {
                p2 = p2.nextNode;
            }
        }
        while (p1 != null && p2 != null) {
            if (p1 == p2) {
                return p1;
            } else {
                p1 = p1.nextNode;
                p2 = p2.nextNode;
            }
        }
        return null;
    }

    public static int getListLength(ListNode head) {
        if (head == null) {
            return 0;
        }
        int length = 0;
        ListNode p = head;
        while (p != null) {
            length++;
            p = p.nextNode;
        }
        return length;
    }

}



38.统计一个数字在排序数组中出现的次数。例如输入排序数组{1,2,3,3,3,3,4,5}和数字3，由于3在这个数组中出现了4次，因此输出4。


思路：二分查找，分两步，首先找到数字出现的第一个元素的下标，然后找到最后一个出现的下标，时间复杂度logN


38-48 面试中的各项能力

sum up

沟通能力、学习能力。善于提问的人有较好的沟通和学习能力。
知识迁移能力能帮助我们轻松解决很多问题，举一反三的能力，平时要有一定的积累，每完成一道题目之后都要总结解题方法。
抽象建模能力，选取适当的数据结构表述模型，分析模型中的内在规律确定计算方法。

发散思维能力，跳出常规思路的束缚，从不同的角度去尝试新的方法。




39.输入一颗二叉树的根节点，求该树的深度。从根节点到叶节点依次经过的结点（含根、叶结点）形成树的一条路径，最长路径的长度为树的深度。

思路：根节点的深度为左右节点中深度大的加1，同理递归实现




39_1.输入一颗二叉树的根结点，判断该树是不是平衡二叉树。如果某二叉树中任意结点的左右子树的深度相差不超过1，那么它就是一颗平衡二叉树。
思路：后序遍历（避免重复遍历同一个节点）





40.一个整型数组里除了两个数字之外，其他的数字都出现了两次。请写程序找出这两个只出现一次的数字。要求时间复杂度是O(n)，空间复杂度O(1)。

位的异或操作

package offer;

public class FindNumsAppearOnce {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = {2,4,3,6,3,2,5,5};
        findNumsAppearOnce(a);
    }
    public static int[] findNumsAppearOnce(int[] nums) {
        if (nums == null || nums.length < 2) {
            return null;
        }
        int x = 0;
        int y = 0;
        int result = 0;
        for (int i : nums) {
            result ^= i;
        }
        result = result & -result;
        for (int i : nums) {
            if ((i & result) == 0) {
                x ^= i;
            } else {
                y ^= i;
            }
        }
        return new int[]{x,y};
    }

}


41.输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。如果有多对数字的和等于s，输出任意一对即可。

思路：二指针

41_1.输入一个正数s，打印出所有和为s的连续正数序列（至少含两个数）。例如输入15，由于1+2+3+4+5=4+5+6=7+8=15，所以结果打印出3个连续序列1~5,4~6和7~8。
思路：二指针



42.输入一个英文句子，翻转句子中单词的顺序，但单词内字符的顺序不变。为简单起见，标点符号和普通字母一样处理。例如输入字符串"I am student"，则输出"student. a am I"。

思路：先整体翻转，然后再每个单词翻转（用空格分隔单词）

42_1.字符串的左旋转操作是把字符串前面若干个字符转移到字符串的尾部。请定义一个函数实现字符串左旋转操作的功能。比如输入字符串"abcdefg"和数字2，函数将返回"cdefgab"。
思路：先根据数字切分两段分别翻转，然后再整体翻转



43.把n个骰子仍在地上，所有骰子朝上一面的点数之和为s，输入n，打印出s的所有可能的值出现的概率。
思路：1、用一个数组来存储点数，数组的大小length = 6n - n + 1.把骰子分为1 和n-1,然后剩下的n-1再分，递归实现，缺点会重复计算。
2、使用两个数组来实现，迭代





44.从扑克牌中随机抽5张牌，判断是不是一个顺子，即这5张牌是不是连续的。2~10为数字本身，A为1，J为11，Q为12，K为13，而大、小王可以看成任意数字。
思路：把抽取的5张牌抽象成长度为5的一个数字，令大小王为0.首先对数组进行排序，然后统计0的个数，在统计元素的间隔数目，在有重复元素和间隔数目大于0的个数时说明不是一个顺子



45.0~n-1这n个数字排列成一个圆圈，从数字0开始每次从这个圆圈中删除第m个数字。求出这个圆圈里剩下的最后一个数字。

著名的约瑟夫环问题
思路：
1、用环形链表模拟圆圈，时间复杂度O（mn）,空间复杂度O（n）。
2、分析每次被删除的数字规律直接计算出最后圆圈中剩下的数字（不太好想到）

法2
int lastRemaining(int n, int m) {
     if (n < 1 || m < 1) {
          return -1;
     }

     int last = 0;
     for (int i = 2; i <= n; i++) {
         last = (last + m) % i; 
     }
     return last;
}

46.求1+2+...+n，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句。
思路：
c++
1、利用构造函数
2、利用虚函数
3、利用函数指针
4、利用模板类型




47.写一个函数，求两个整数之和，要求函数体内部的使用+、-、*、\四则与水暖符号。
思路：位运算的抑或操作和与操作

int add(int num1, int num2) {
     int sum = 0;
     int carry = 0;
     do {
          sum = num1 ^ num2;
          carry = (num1 & num2) << 1;
          num1 = sum;
          num2 = carry;
     } while (num2 != 0);
     return sum;
}


49 把字符串转换成整数
思路：考虑到所有的可能输入，比如null，“”,正负号，溢出等。
























