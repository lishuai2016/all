# 收集到的面试题目，有时间了慢慢思考
百度

一面：简单的自我介绍，因为是电话面试，所以流畅了很多（你们懂的）。一个小时满满的技术问题，所以就不用向上面再赘述了，直接上干货

1. 是否了解动态规划
动归，本质上是一种划分子问题的算法，站在任何一个子问题的处理上看，当前子问题的提出都要依据现有的类似结论，而当前问题的结论是后 面问题求解的铺垫。任何DP都是基于存储的算法，核心是状态转移方程。
2. JVM调优
其实我没有实际的调优经验，但是我主要介绍了一下JVM的分区、堆的分代以及回收算法还有OOM异常的处理思路
3. 分别介绍一下Struts2和Spring
不用多说，这方面比我答得好的同学肯定大有人在，就不出丑了
4. 职责链模式（设计模式）
GoF经典设计模式的一种
5. 实践中如何优化MySQL
我当时是按以下四条依次回答的，他们四条从效果上第一条影响最大，后面越来越小。
① SQL语句及索引的优化
② 数据库表结构的优化
③ 系统配置的优化
④ 硬件的优化
6. 什么情况下设置了索引但无法使用
① 以“%”开头的LIKE语句，模糊匹配
② OR语句前后没有同时使用索引
③ 数据类型出现隐式转化（如varchar不加单引号的话可能会自动转换为int型）
7. SQL语句的优化
order by要怎么处理
alter尽量将多次合并为一次
insert和delete也需要合并
等等
8. 索引的底层实现原理和优化
B+树，经过优化的B+树
主要是在所有的叶子结点中增加了指向下一个叶子节点的指针，因此InnoDB建议为大部分表使用默认自增的主键作为主索引。
9. HTTP和HTTPS的主要区别

10. Cookie和Session的区别

11. 如何设计一个高并发的系统
① 数据库的优化，包括合理的事务隔离级别、SQL语句优化、索引的优化
② 使用缓存，尽量减少数据库 IO
③ 分布式数据库、分布式缓存
④ 服务器的负载均衡

12. linux中如何查看进程等命令

13. 两条相交的单向链表，如何求他们的第一个公共节点
很简单的链表题目，博客上的做法一搜一大把，我记得当时答在兴头上，又给面试官解释了一下如何求单向局部循环链表的入口，链表中很经典的问题（其实链表也就那几个常用算法，比如逆制、求倒数第K个节点，判断是否有环等）
大概八十分钟吧，最后问面试官有没有对我的意见或者建议，面试官说觉得我今晚的面试表现比简历上写的更出色。。。对面试官的好感度瞬间飙升

二面：可能一面面试官对我的评价还算不错，二面面试官一口气考了我11个设计模式（手动微笑），对，是11个设计模式，有直接提问，也有在场景设计中引导我使用。总共加起来11个，分别是：单例模式、简单工厂模式、工厂模式、抽象工厂模式、策略模式、观察者模式、组合模式、适配器模式、装饰模式、代理模式、外观模式。然后就是设计一个公司下有部门、部门下有经理和员工，经理可以管理经理和员工这样的一个模型，组合模式一套用就行了。后面还问了几个非技术问题，比如和产品、测试发生矛盾了怎么处理，答应的任务发现完成不了该如何处理等，大家如果遇到请随意装逼。

百度给我最大的印象就是每次新的面试，面试官一定会问什么时候能来实习，能够实习多长时间，答案符合要求了才进行下面的面试。据说百度今年要求6个月的实习时间，回答两三个月的都再无下文。

机智的我不管哪家公司问都是说从即日起到大四毕业，中间出了期末考试和毕业设计，都能参与实习，近期就能参加实习。不是故意想骗人，只是目前还没有和HR谈条件的筹码，这样说了就算最后去不了，也算是一次面试机会，如果一开始就拒绝的话，连面试机会都没有。等到技术面试结束了，跟一开始比，就有了谈条件的筹码，这个时候大家再根据情况合理要价


算法问题收集

手写代码，一组字符串按字典顺序排序
复杂度分析，有没有更快点的
手写代码，一组字符串按出现次数排序

如何求二叉树中两个节点的最短路径，有没有更快的方法
用java统计一个文本文件中出现的频率最高的20个单词
一个二分查找

常见的排序算法，尤其快排

一个找素数，一个递归求阶层，对我也算手下留情（他后来让我同学写AVL树的插入算法，想想也是醉了）

怎么找到一个随机数组的前50大数、中间50大数，（这个用最小堆和partition函数），复杂度是多少。

把一个数组中奇数放前面，偶数放后面，这个要求写出来。另一个是3亿条IP中，怎么找到次数出现最多的5000条IP

反转链表、冒泡排序、生产者消费者

第一个是二分查找，我用递归和非递归各写了一遍，重点就在于下标的控制；另一道是在N个数中求前M大个数，其实也很简单，思路就是使用快速排序的思想，每一次当把一个数字放在正确的位置上的时候跟M进行比较，其实在剑指offer上有原题。

两条相交的单向链表，如何求他们的第一个公共节点

二叉树的遍历方式，前序、中序、后序和层序



- 不用临时变量怎么实现 swap(a, b)——用加法或者异或都可以
- 二维有序数组查找数字——剑指 offer 第 3题
- 亿级日志中，查找登陆次数最多的十个用户——（不确定对不对，我的思路是）先用哈希表保存登陆次数和ID，然后用红黑树保存最大的十个数。剑指 offer 第 30题
- 简述排序算法——快排，partion函数的原理，堆排（不稳定），归并排序，基数排序。



- 把 "www.zhidao.baidu.com" 这样的字符串改成 "com/baidu/zhidao/www"。——老题目了，剑指 offer 的，两次逆序排列即可。
- 求数组中和为某个值的所有子数组，比如数组是[5,5,10,2,3]一共有四个子数组的和是 15，比如[5,10]，[5,10]，[10,2,3]，[5,5,2,3]。这个就是简单的递归了，分两种情况，当前位置的数字在子数组中，以及不在子数组中。


https://www.nowcoder.com/discuss/3043
面试心得与总结---BAT、网易、蘑菇街
2、
https://www.nowcoder.com/discuss/11971?type=2&order=0&pos=8&page=1网易+阿里内推面经
3、
Java研发方向如何准备BAT技术面试答案http://www.jianshu.com/p/b5f1121c3a2d


华为1---（三道）
1、
[编程题] 最高分是多少

老师想知道从某某同学当中，分数最高的是多少，现在请你编程模拟老师的询问。当然，老师有时候需要更新某位同学的成绩.
输入描述:

输入包括多组测试数据。
每组输入第一行是两个正整数N和M（0 < N <= 30000,0 < M < 5000）,分别代表学生的数目和操作的数目。
学生ID编号从1编到N。
第二行包含N个整数，代表这N个学生的初始成绩，其中第i个数代表ID为i的学生的成绩
接下来又M行，每一行有一个字符C（只取‘Q’或‘U’），和两个正整数A,B,当C为'Q'的时候, 表示这是一条询问操作，他询问ID从A到B（包括A,B）的学生当中，成绩最高的是多少
当C为‘U’的时候，表示这是一条更新操作，要求把ID为A的学生的成绩更改为B。


输出描述:

对于每一次询问操作，在一行里面输出最高成绩.


输入例子:

5 7
1 2 3 4 5
Q 1 5
U 3 6
Q 3 4
Q 4 5
U 4 5
U 2 9
Q 1 5


输出例子:

5
6
5
9


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Main{
    public static void main(String[] args)
    {
        //键盘输入的字节流变为字符流，再变为缓存流
        //BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        Scanner sc=new Scanner(System.in);
         int count; //几个学生
         int oper; //几次操作
         while(sc.hasNext())
         {
             count=sc.nextInt();
             oper=sc.nextInt();
         int[] res=new int[count];
         for(int i=0;i<count;i++)
         {
             res[i]=sc.nextInt();
         }
         //sc.nextLine();
         //List<Integer> ls=new ArrayList<>();
         while(oper-->0&&sc.hasNext())
         {
             String he=sc.next();
             int num1=sc.nextInt();
             int num2=sc.nextInt();
             if(he.equals("Q"))
             {
                 System.out.println(maxnum(res,num1,num2));
             }
             else if(he.equals("U"))
             {
                 update(res,num1,num2);
             }
         }


         }
    }
    //
    public static void update(int []res , int index,int num)
     {
         res[index-1]=num;

     }
    public static int maxnum(int [] res,int start,int end)
    {
        if(start>end)
            return maxnum(res,end,start);
        int max=Integer.MIN_VALUE;
        for(int i=start-1;i<end;i++)
        {
            if(res[i]>max)
                max=res[i];
        }
        return max;
    }

}
2、
[编程题] 简单错误记录

开发一个简单错误记录功能小模块，能够记录出错的代码所在的文件名称和行号。
处理:
1.记录最多8条错误记录，对相同的错误记录(即文件名称和行号完全匹配)只记录一条，错误计数增加；(文件所在的目录不同，文件名和行号相同也要合并)
2.超过16个字符的文件名称，只记录文件的最后有效16个字符；(如果文件名不同，而只是文件名的后16个字符和行号相同，也不要合并)
3.输入的文件可能带路径，记录文件名称不能带路径

输入描述:

一行或多行字符串。每行包括带路径文件名称，行号，以空格隔开。
    文件路径为windows格式
    如：E:\V1R2\product\fpgadrive.c 1325

输出描述:

将所有的记录统计并将结果输出，格式：文件名代码行数数目，一个空格隔开，如: fpgadrive.c 1325 1
    结果根据数目从多到少排序，数目相同的情况下，按照输入第一次出现顺序排序。
    如果超过8条记录，则只输出前8条记录.
    如果文件名的长度超过16个字符，则只输出后16个字符

输入例子:

E:\V1R2\product\fpgadrive.c 1325


输出例子:

fpgadrive.c 1325 1



3、
[编程题] 扑克牌大小

扑克牌游戏大家应该都比较熟悉了，一副牌由54张组成，含3~A，2各4张，小王1张，大王1张。牌面从小到大用如下字符和字符串表示（其中，小写joker表示小王，大写JOKER表示大王）:)
3 4 5 6 7 8 9 10 J Q K A 2 joker JOKER
输入两手牌，两手牌之间用“-”连接，每手牌的每张牌以空格分隔，“-”两边没有空格，如：4 4 4 4-joker JOKER
请比较两手牌大小，输出较大的牌，如果不存在比较关系则输出ERROR

基本规则：
（1）输入每手牌可能是个子，对子，顺子（连续5张），三个，炸弹（四个）和对王中的一种，不存在其他情况，由输入保证两手牌都是合法的，顺子已经从小到大排列；
（2）除了炸弹和对王可以和所有牌比较之外，其他类型的牌只能跟相同类型的存在比较关系（如，对子跟对子比较，三个跟三个比较），不考虑拆牌情况（如：将对子拆分成个子）
（3）大小规则跟大家平时了解的常见规则相同，个子，对子，三个比较牌面大小；顺子比较最小牌大小；炸弹大于前面所有的牌，炸弹之间比较牌面大小；对王是最大的牌；
（4）输入的两手牌不会出现相等的情况。

答案提示：
（1）除了炸弹和对王之外，其他必须同类型比较。
（2）输入已经保证合法性，不用检查输入是否是合法的牌。
（3）输入的顺子已经经过从小到大排序，因此不用再排序了.

输入描述:

输入两手牌，两手牌之间用“-”连接，每手牌的每张牌以空格分隔，“-”两边没有空格，如4 4 4 4-joker JOKER。


输出描述:

输出两手牌中较大的那手，不含连接符，扑克牌顺序不变，仍以空格隔开；如果不存在比较关系则输出ERROR。


输入例子:

4 4 4 4-joker JOKER


输出例子:

joker JOKER



面经知识点2
Java基础：
面向对象和面向过程的区别
Java的四个基本特性（抽象、封装、继承，多态）
Overload和Override的区别
构造器Constructor是否可被override
访问控制符public,protected,private,以及默认的区别
是否可以继承String类
String和StringBuffer、StringBuilder的区别
hashCode和equals方法的关系
抽象类和接口的区别
自动装箱与拆箱
什么是泛型、为什么要使用以及泛型擦除
Java中的集合类及关系图
HashMap实现原理(看源代码)
HashTable实现原理(看源代码)
HashMap和HashTable区别
HashTable如何实现线程安全(看源代码)
ArrayList和vector区别(看源代码)
ArrayList和LinkedList区别及使用场景
Collection和Collections的区别
Concurrenthashmap实现原理(看源代码)
Error、Exception区别
Unchecked Exception和Checked Exception，各列举几个
Java中如何实现代理机制(JDK、CGLIB)
多线程的实现方式
线程的状态转换
如何停止一个线程
什么是线程安全
如何保证线程安全
Synchronized如何使用
synchronized和Lock的区别
多线程如何进行信息交互
sleep和wait的区别(考察的方向是是否会释放锁)
多线程与死锁
如何才能产生死锁
什么叫守护线程，用什么方法实现守护线程
Java线程池技术及原理
java并发包concurrent及常用的类
volatile关键字
Java中的NIO，BIO，AIO分别是什么
IO和NIO区别
序列化与反序列化
常见的序列化协议有哪些
内存溢出和内存泄漏的区别
Java内存模型及各个区域的OOM，如何重现OOM
出现OOM如何解决
用什么工具可以查出内存泄漏
Java内存管理及回收算法
Java类加载器及如何加载类(双亲委派)
xml解析方式
Statement和PreparedStatement之间的区别
JavaEE:
servlet生命周期及各个方法
servlet中如何自定义filter
JSP原理
JSP和Servlet的区别
JSP的动态include和静态include
Struts中请求处理过程
MVC概念
Spring mvc与Struts区别
Hibernate/Ibatis两者的区别
Hibernate一级和二级缓存
Hibernate实现集群部署
Hibernate如何实现声明式事务
简述Hibernate常见优化策略
Spring bean的加载过程(推荐看Spring的源码)
Spring如何实现AOP和IOC
Spring bean注入方式
Spring的事务管理(推荐看Spring的源码)
Spring事务的传播特性
springmvc原理
springmvc用过哪些注解
Restful有几种请求
Restful好处
Tomcat，Apache，JBoss的区别
memcached和redis的区别
有没有遇到中文乱码问题，如何解决的
如何理解分布式锁
你知道的开源协议有哪些
json和xml区别
设计模式：
设计模式的六大原则
常用的设计模式
用一个设计模式写一段代码或画出一个设计模式的UML
如何理解MVC
高内聚，低耦合方面的理解
算法：
深度优先、广度优先算法
排序算法及对应的时间复杂度和空间复杂度
写一个排序算法
查找算法
B+树和二叉树查找时间复杂度
KMP算法、hash算法
常用的hash算法有哪些
如何判断一个单链表是否有环？
给你一万个数，如何找出里面所有重复的数？用所有你能想到的方法，时间复杂度和空间复杂度分别是多少？
给你一个数组，如何里面找到和为K的两个数？
100000个数找出最小或最大的10个？
一堆数字里面继续去重，要怎么处理？
数据结构：
队列、栈、链表、树、堆、图
编码实现队列、栈
Linux:
linux常用命令
如何查看内存使用情况
Linux下如何进行进程调度
操作系统：
操作系统什么情况下会死锁
产生死锁的必要条件
死锁预防
数据库：
范式
数据库事务隔离级别
数据库连接池的原理
乐观锁和悲观锁
如何实现不同数据库的数据查询分页
SQL注入的原理，如何预防
数据库索引的实现(B+树介绍、和B树、R树区别)
SQL性能优化
数据库索引的优缺点以及什么时候数据库索引失效
Redis的存储结构
网络：
OSI七层模型以及TCP/IP四层模型
HTTP和HTTPS区别
HTTP报文内容
get提交和post提交的区别
get提交是否有字节限制，如果有是在哪限制的
TCP的三次握手和四次挥手
session和cookie的区别
HTTP请求中Session实现原理
redirect与forward区别
DNS
TCP和UDP区别
安全：
如果客户端不断的发送请求连接会怎样
DDos攻击
DDos预防
那怎么知道连接是恶意的呢？可能是正常连接
其它：
说一个你参与的项目、其中作为什么角色
遇到最困的问题是什么，怎么解决的
你认为自己有那些方面不足
平常如何学习的
如何评价自己
智力题：
给你50个红球和50个黑球，有两个一模一样的桶，往桶里放球，让朋友去随机抽，采用什么策略可以让朋友抽到红球的概率更高？
从100个硬币中找出最轻的那个假币？
     以上这些考察的知识点，在强大的互联网上都可以搜索到答案，有些答案可能不是很全，所以需要自己去总结，但是对于一些需要知道原理的知识点，还是推荐看源代码或者对于的书，然后总结得到自己的东西，这样既学到真东西，还不会很容易忘。Java基础的知识点推荐《Java编程思想》，JVM的推荐《深入理解Java虚拟机》，Spring原理的推荐《Spring源码深度解析》，对于网站架构的推荐《大型网站技术架构核心原理与案例分析》。欢迎关注Java技术分享微信公众号：JavaQ，获取更多精彩技术分享。




1.面向对象和面向过程的区别
面向过程
优点：性能比面向对象高，因为类调用时需要实例化，开销比较大，比较消耗资源;比如单片机、嵌入式开发、Linux/Unix等一般采用面向过程开发，性能是最重要的因素。
缺点：没有面向对象易维护、易复用、易扩展
面向对象
优点：易维护、易复用、易扩展，由于面向对象有封装、继承、多态性的特性，可以设计出低耦合的系统，使系统更加灵活、更加易于维护
缺点：性能比面向过程低
2.Java的四个基本特性（抽象、封装、继承，多态）
抽象：就是把现实生活中的某一类东西提取出来，用程序代码表示，我们通常叫做类或者接口。抽象包括两个方面：一个是数据抽象，一个是过程抽象。数据抽象也就是对象的属性。过程抽象是对象的行为特征。
封装：把客观事物封装成抽象的类，并且类可以把自己的数据和方法只让可信的类或者对象操作，对不可信的进行封装隐藏。封装分为属性的封装和方法的封装。
继承：是对有着共同特性的多类事物，进行再抽象成一个类。这个类就是多类事物的父类。父类的意义在于抽取多类事物的共性。
多态：允许不同类的对象对同一消息做出响应。方法的重载、类的覆盖正体现了多态。
3.重载和重写的区别
重载：发生在同一个类中，方法名必须相同，参数类型不同、个数不同、顺序不同，方法返回值和访问修饰符可以不同，发生在编译时。
重写：发生在父子类中，方法名、参数列表必须相同，返回值小于等于父类，抛出的异常小于等于父类，访问修饰符大于等于父类；如果父类方法访问修饰符为private则子类中就不是重写。
4.构造器Constructor是否可被override
构造器不能被重写，不能用static修饰构造器，只能用 public private protected这三个权限修饰符，且不能有返回语句。
5.访问控制符public,protected,private,以及默认的区别
private只有在本类中才能访问；
public在任何地方都能访问；
protected在同包内的类及包外的子类能访问；
默认不写在同包内能访问。
6.是否可以继承String类
String类是final类故不可以继承，一切由final修饰过的都不能继承
7.String和StringBuffer、StringBuilder的区别
可变性：
String类中使用字符数组保存字符串，private final char value[]，所以string对象是不可变的。
StringBuilder与StringBuffer都继承自AbstractStringBuilder类，在AbstractStringBuilder中也是使用字符数组保存字符串，char[] value，这两种对象都是可变的。
线程安全性：
String中的对象是不可变的，也就可以理解为常量，线程安全。
AbstractStringBuilder是StringBuilder与StringBuffer的公共父类，定义了一些字符串的基本操作，如expandCapacity、append、insert、indexOf等公共方法。StringBuffer对方法加了同步锁或者对调用的方法加了同步锁，所以是线程安全的。StringBuilder并没有对方法进行加同步锁，所以是非线程安全的。
性能：
每次对String 类型进行改变的时候，都会生成一个新的 String 对象，然后将指针指向新的 String 对象。StringBuffer每次都会对 StringBuffer 对象本身进行操作，而不是生成新的对象并改变对象引用。相同情况下使用 StirngBuilder 相比使用 StringBuffer 仅能获得 10%~15% 左右的性能提升，但却要冒多线程不安全的风险。
8.hashCode和equals方法的关系
equals相等，hashcode必相等；hashcode相等，equals可能不相等。
9.抽象类和接口的区别
语法层次：
抽象类和接口分别给出了不同的语法定义
设计层次：
抽象层次不同，抽象类是对类抽象，而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。
跨域不同，抽象类所体现的是一种继承关系，要想使得继承关系合理，父类和派生类之间必须存在"is-a" 关系，即父类和派生类在概念本质上应该是相同的。对于接口则不然，并不要求接口的实现者和接口定义在概念本质上是一致的，仅仅是实现了接口定义的契约而已，"like-a"的关系。。
设计层次不同，抽象类是自底向上抽象而来的，接口是自顶向下设计出来的。
10.自动装箱与拆箱
装箱：将基本类型用它们对应的引用类型包装起来；
拆箱：将包装类型转换为基本数据类型；
Java使用自动装箱和拆箱机制，节省了常用数值的内存开销和创建对象的开销，提高了效率，由编译器来完成，编译器会在编译期根据语法决定是否进行装箱和拆箱动作。
11.什么是泛型、为什么要使用以及泛型擦除
泛型，即“参数化类型”。
创建集合时就指定集合元素的类型，该集合只能保存其指定类型的元素，避免使用强制类型转换。
Java编译器生成的字节码是不包涵泛型信息的，泛型类型信息将在编译处理是被擦除，这个过程即类型擦除。 泛型擦除可以简单的理解为将泛型java代码转换为普通java代码，只不过编译器更直接点，将泛型java代码直接转换成普通java字节码。
类型擦除的主要过程如下：
一.将所有的泛型参数用其最左边界（最顶级的父类型）类型替换。
二.移除所有的类型参数。
12.Java中的集合类及关系图
List和Set继承自Collection接口。
Set无序不允许元素重复。HashSet和TreeSet是两个主要的实现类。
List有序且允许元素重复。ArrayList、LinkedList和Vector是三个主要的实现类。
Map也属于集合系统，但和Collection接口没关系。Map是key对value的映射集合，其中key列就是一个集合。key不能重复，但是value可以重复。 HashMap、TreeMap和Hashtable是三个主要的实现类。
SortedSet和SortedMap接口对元素按指定规则排序，SortedMap是对key列进行排序。
13.HashMap实现原理
具体原理一句两句也说不清楚，网络文章：
HashMap的工作原理

　　HashMap基于hashing原理，我们通过put()和get()方法储存和获取对象。
 当我们往hashmap中put元素的时候，先根据key的hash值得到这个元素在数组中的位置（即下标），然后就可以把这个元素放到对应的位置中了。如果这个元素所在的位子上已经存放有其他元素了，那么在同一个位子上的元素将以链表的形式存放，新加入的放在链头，最先加入的放在链尾。从hashmap中get元素时，首先计算key的hashcode，找到数组中对应位置的某一元素，然后通过key的equals方法在对应位置的链表中找到需要的元素

当获取对象时，通过键对象的equals()方法找到正确的键值对，然后返回值对象。
HashMap使用LinkedList来解决碰撞问题，当发生碰撞了，对象将会储存在LinkedList的下一个节点中。 HashMap在每个LinkedList节点中储存键值对对象。

　　当两个不同的键对象的hashcode相同时会发生什么？ 它们会储存在同一个bucket位置的LinkedList中。键对象的equals()方法用来找到键值对。

　　因为HashMap的好处非常多，我曾经在电子商务的应用中使用HashMap作为缓存。因为金融领域非常多的运用Java，也出于性能的考虑，我们会经常用到HashMap和ConcurrentHashMap。

优化碰撞：
使用不可变的、声明作final的对象，并且采用合适的equals()和hashCode()方法的话，将会减少碰撞的发生，提高效率。不可变性使得能够缓存不同键的hashcode，这将提高整个获取对象的速度，使用String，Interger这样的wrapper类作为键是非常好的选择。

1、 为什么String, Interger这样的wrapper类适合作为键？ String, Interger这样的wrapper类作为HashMap的键是再适合不过了，而且String最为常用。因为String是不可变的，也是final的，而且已经重写了equals()和hashCode()方法了。其他的wrapper类也有这个特点。不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。不可变性还有其他的优点如线程安全。如果你可以仅仅通过将某个field声明成final就能保证hashCode是不变的，那么请这么做吧。因为获取对象的时候要用到equals()和hashCode()方法，那么键对象正确的重写这两个方法是非常重要的。如果两个不相等的对象返回不同的hashcode的话，那么碰撞的几率就会小些，这样就能提高HashMap的性能。
2、我们可以使用自定义的对象作为键吗？ 这是前一个问题的延伸。当然你可能使用任何对象作为键，只要它遵守了equals()和hashCode()方法的定义规则，并且当对象插入到Map中之后将不会再改变了。如果这个自定义对象时不可变的，那么它已经满足了作为键的条件，因为当它创建之后就已经不能改变了。
3、我们可以使用CocurrentHashMap来代替HashTable吗？这是另外一个很热门的面试题，因为ConcurrentHashMap越来越多人用了。我们知道HashTable是synchronized的，但是ConcurrentHashMap同步性能更好，因为它仅仅根据同步级别对map的一部分进行上锁。ConcurrentHashMap当然可以代替HashTable，但是HashTable提供更强的线程安全性。


3、hashmap的resize

       当hashmap中的元素越来越多的时候，碰撞的几率也就越来越高（因为数组的长度是固定的），所以为了提高查询的效率，就要对hashmap的数组进行扩容，数组扩容这个操作也会出现在ArrayList中，所以这是一个通用的操作，很多人对它的性能表示过怀疑，不过想想我们的“均摊”原理，就释然了，而在hashmap数组扩容之后，最消耗性能的点就出现了：原数组中的数据必须重新计算其在新数组中的位置，并放进去，这就是resize。

         那么hashmap什么时候进行扩容呢？当hashmap中的元素个数超过数组大小*loadFactor时，就会进行数组扩容，loadFactor的默认值为0.75，也就是说，默认情况下，数组大小为16，那么当hashmap中元素个数超过16*0.75=12的时候，就把数组的大小扩展为2*16=32，即扩大一倍，然后重新计算每个元素在数组中的位置，而这是一个非常消耗性能的操作，所以如果我们已经预知hashmap中元素的个数，那么预设元素的个数能够有效的提高hashmap的性能。比如说，我们有1000个元素new HashMap(1000), 但是理论上来讲new HashMap(1024)更合适，不过上面annegu已经说过，即使是1000，hashmap也自动会将其设置为1024。 但是new HashMap(1024)还不是更合适的，因为0.75*1000 < 1000, 也就是说为了让0.75 * size > 1000, 我们必须这样new HashMap(2048)才最合适，既考虑了&的问题，也避免了resize的问题。

4、key的hashcode与equals方法改写
在第一部分hashmap的数据结构中，annegu就写了get方法的过程：首先计算key的hashcode，找到数组中对应位置的某一元素，然后通过key的equals方法在对应位置的链表中找到需要的元素。所以，hashcode与equals方法对于找到对应元素是两个关键方法。

Hashmap的key可以是任何类型的对象，例如User这种对象，为了保证两个具有相同属性的user的hashcode相同，我们就需要改写hashcode方法，比方把hashcode值的计算与User对象的id关联起来，那么只要user对象拥有相同id，那么他们的hashcode也能保持一致了，这样就可以找到在hashmap数组中的位置了。如果这个位置上有多个元素，还需要用key的equals方法在对应位置的链表中找到需要的元素，所以只改写了hashcode方法是不够的，equals方法也是需要改写滴~当然啦，按正常思维逻辑，equals方法一般都会根据实际的业务内容来定义，例如根据user对象的id来判断两个user是否相等。
在改写equals方法的时候，需要满足以下三点：
(1) 自反性：就是说a.equals(a)必须为true。
(2) 对称性：就是说a.equals(b)=true的话，b.equals(a)也必须为true。
(3) 传递性：就是说a.equals(b)=true，并且b.equals(c)=true的话，a.equals(c)也必须为true。
通过改写key对象的equals和hashcode方法，我们可以将任意的业务对象作为map的key(前提是你确实有这样的需要)。

loadFactor：负载因子loadFactor定义为：散列表的实际元素数目(n)/ 散列表的容量(m)。
   负载因子衡量的是一个散列表的空间的使用程度，负载因子越大表示散列表的装填程度越高，反之愈小。对于使用链表法的散列表来说，查找一个元素的平均时间是O(1+a)，因此如果负载因子越大，对空间的利用更充分，然而后果是查找效率的降低；如果负载因子太小，那么散列表的数据将过于稀疏，对空间造成严重浪费。
   HashMap的实现中，通过threshold字段来判断HashMap的最大容量：
- threshold = (int)(capacity * loadFactor);

Fail-Fast机制：
   我们知道java.util.HashMap不是线程安全的，因此如果在使用迭代器的过程中有其他线程修改了map，那么将抛出ConcurrentModificationException，这就是所谓fail-fast策略。
   这一策略在源码中的实现是通过modCount域，modCount顾名思义就是修改次数，对HashMap内容的修改都将增加这个值，那么在迭代器初始化过程中会将这个值赋给迭代器的expectedModCount。

Java代码


- HashIterator() {
-     expectedModCount = modCount;
-     if (size > 0) { // advance to first entry
-     Entry[] t = table;
-     while (index < t.length && (next = t[index++]) == null)
-         ;
-     }
- }


   在迭代过程中，判断modCount跟expectedModCount是否相等，如果不相等就表示已经有其他线程修改了Map：
   注意到modCount声明为volatile，保证线程之间修改的可见性。

Java代码


- final Entry<K,V> nextEntry() {
-     if (modCount != expectedModCount)
-         throw new ConcurrentModificationException();


   在HashMap的API中指出：
   由所有HashMap类的“collection 视图方法”所返回的迭代器都是快速失败的：在迭代器创建之后，如果从结构上对映射进行修改，除非通过迭代器本身的 remove 方法，其他任何时间任何方式的修改，迭代器都将抛出ConcurrentModificationException。因此，面对并发的修改，迭代器很快就会完全失败，而不冒在将来不确定的时间发生任意不确定行为的风险。
   注意，迭代器的快速失败行为不能得到保证，一般来说，存在非同步的并发修改时，不可能作出任何坚决的保证。快速失败迭代器尽最大努力抛出 ConcurrentModificationException。因此，编写依赖于此异常的程序的做法是错误的，正确做法是：迭代器的快速失败行为应该仅用于检测程序错误。

http://zhangshixi.iteye.com/blog/672697
http://www.admin10000.com/document/3322.html

http://www.iteye.com/topic/539465


14.HashTable实现原理
具体原理一句两句也说不清楚，网络文章：


http://www.cnblogs.com/skywang12345/p/3310887.html
http://blog.csdn.net/chdjj/article/details/38581035
总结：
1.Hashtable是个线程安全的类（HashMap线程安全）；
2.Hasbtable并不允许值和键为空（null），若为空，会抛空指针（HashMap可以）；
3.Hashtable不允许键重复，若键重复，则新插入的值会覆盖旧值（同HashMap）；
4.Hashtable同样是通过链表法解决冲突；
5.Hashtable根据hashcode计算索引时将hashcode值先与上0x7FFFFFFF,这是为了保证hash值始终为正数;
6.Hashtable的容量为任意正数（最小为1），而HashMap的容量始终为2的n次方。Hashtable默认容量为 11
    HashMap默认容量为      16；
7.Hashtable每次扩容，新容量为旧容量的2倍加2，而HashMap为旧容量的2倍；
8.Hashtable和HashMap默认负载因子都为0.75;


15.HashMap和HashTable区别
一.HashTable的方法前面都有synchronized来同步，是线程安全的；HashMap未经同步，是非线程安全的。
二.HashTable不允许null值(key和value都不可以) ；HashMap允许null值(key和value都可以)。
三.HashTable有一个contains(Object value)功能和containsValue(Object value)功能一样。
四.HashTable使用Enumeration进行遍历；HashMap使用Iterator进行遍历。
五.HashTable中hash数组默认大小是11，增加的方式是 old*2+1；HashMap中hash数组的默认大小是16，而且一定是2的指数。
六.哈希值的使用不同，HashTable直接使用对象的hashCode； HashMap重新计算hash值，而且用与代替求模。
16.ArrayList和vector区别
ArrayList和 Vector都实现了List接口， 都是通过数组实现的。
Vector是线程安全的，而ArrayList是非线程安全的。
List第一次创建的时候，会有一个初始大小，随着不断向List中增加元素，当 List 认为容量不够的时候就会进行扩容。Vector缺省情况下自动增长原来一倍的数组长度，ArrayList增长原来的50%。
17.ArrayList和LinkedList区别及使用场景
ArrayList底层是用数组实现的，可以认为ArrayList是一个可改变大小的数组。随着越来越多的元素被添加到ArrayList中，其规模是动态增加的。
LinkedList底层是通过双向链表实现的， LinkedList和ArrayList相比，增删的速度较快。但是查询和修改值的速度较慢。同时，LinkedList还实现了Queue接口，所以他还提供了offer(), peek(), poll()等方法。
LinkedList更适合从中间插入或者删除（链表的特性）。 ArrayList更适合检索和在末尾插入或删除（数组的特性）。
18.Collection和Collections的区别
java.util.Collection 是一个集合接口。它提供了对集合对象进行基本操作的通用接口方法。Collection接口在Java 类库中有很多具体的实现。Collection接口的意义是为各种具体的集合提供了最大化的统一操作方式。
java.util.Collections 是一个包装类。它包含有各种有关集合操作的静态多态方法。此类不能实例化，就像一个工具类，服务于Java的Collection框架。
19.Concurrenthashmap实现原理（分段锁机制）
具体原理一句两句也说不清楚，网络文章：
ConcurrentHashMap和Hashtable主要区别就是围绕着锁的粒度以及如何锁,可以简单理解成把一个大的HashTable分解成多个，形成了锁分离。如图:
而Hashtable的实现方式是---锁整个hash表

ConcurrentHashMap 类中包含两个静态内部类 HashEntry 和 Segment。HashEntry 用来封装映射表的键 / 值对；Segment 用来充当锁的角色，每个 Segment 对象守护整个散列映射表的若干个桶。每个桶是由若干个 HashEntry 对象链接起来的链表。一个 ConcurrentHashMap 实例中包含由若干个 Segment 对象组成的数组。

ConcurrentHashMap是由Segment数组结构和HashEntry数组结构组成。Segment是一种可重入锁ReentrantLock，在ConcurrentHashMap里扮演锁的角色，HashEntry则用于存储键值对数据。一个ConcurrentHashMap里包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表结构， 一个Segment里包含一个HashEntry数组，每个HashEntry是一个链表结构的元素， 每个Segment守护者一个HashEntry数组里的元素,当对HashEntry数组的数据进行修改时，必须首先获得它对应的Segment锁。

总结

ConcurrentHashMap 是一个并发散列映射表的实现，它允许完全并发的读取，并且支持给定数量的并发更新。相比于 HashTable 和用同步包装器包装的 HashMap（Collections.synchronizedMap(new HashMap())），ConcurrentHashMap 拥有更高的并发性。在 HashTable 和由同步包装器包装的 HashMap 中，使用一个全局的锁来同步不同线程间的并发访问。同一时间点，只能有一个线程持有锁，也就是说在同一时间点，只能有一个线程能访问容器。这虽然保证多线程间的安全并发访问，但同时也导致对容器的访问变成串行化的了。

在使用锁来协调多线程间并发访问的模式下，减小对锁的竞争可以有效提高并发性。有两种方式可以减小对锁的竞争：

- 减小请求 同一个锁的 频率。
- 减少持有锁的 时间。

ConcurrentHashMap 的高并发性主要来自于三个方面：

- 用分离锁实现多个线程间的更深层次的共享访问。
- 用 HashEntery 对象的不变性来降低执行读操作的线程在遍历链表期间对加锁的需求。
- 通过对同一个 Volatile 变量的写 / 读访问，协调不同线程间读 / 写操作的内存可见性。

使用分离锁，减小了请求 同一个锁的频率。

通过 HashEntery 对象的不变性及对同一个 Volatile 变量的读 / 写来协调内存可见性，使得 读操作大多数时候不需要加锁就能成功获取到需要的值。由于散列映射表在实际应用中大多数操作都是成功的 读操作，所以 2 和 3 既可以减少请求同一个锁的频率，也可以有效减少持有锁的时间。

通过减小请求同一个锁的频率和尽量减少持有锁的时间 ，使得 ConcurrentHashMap 的并发性相对于 HashTable 和用同步包装器包装的 HashMap有了质的提高。

http://www.cnblogs.com/ITtangtang/p/3948786.html
http://ifeve.com/concurrenthashmap/
https://www.ibm.com/developerworks/cn/java/java-lo-concurrenthashmap/index.html

20.Error、Exception区别
Error类和Exception类的父类都是throwable类，他们的区别是：
Error类一般是指与虚拟机相关的问题，如系统崩溃，虚拟机错误，内存空间不足，方法调用栈溢等。对于这类错误的导致的应用程序中断，仅靠程序本身无法恢复和和预防，遇到这样的错误，建议让程序终止。
Exception类表示程序可以处理的异常，可以捕获且可能恢复。遇到这类异常，应该尽可能处理异常，使程序恢复运行，而不应该随意终止异常。
21.Unchecked Exception和Checked Exception，各列举几个
Unchecked Exception:
a. 指的是程序的瑕疵或逻辑错误，并且在运行时无法恢复。
b. 包括Error与RuntimeException及其子类，如：OutOfMemoryError, UndeclaredThrowableException, IllegalArgumentException, IllegalMonitorStateException, NullPointerException, IllegalStateException, IndexOutOfBoundsException等。
c. 语法上不需要声明抛出异常。
Checked Exception:
a. 代表程序不能直接控制的无效外界情况（如用户输入，数据库问题，网络异常，文件丢失等）
b. 除了Error和RuntimeException及其子类之外，如：ClassNotFoundException, NamingException, ServletException, SQLException, IOException等。
c. 需要try catch处理或throws声明抛出异常。
22.Java中如何实现代理机制(JDK、CGLIB)


JDK动态代理：代理类和目标类实现了共同的接口，用到InvocationHandler接口。
CGLIB动态代理：代理类是目标类的子类， 用到MethodInterceptor接口
23.多线程的实现方式
继承Thread类、实现Runnable接口、使用ExecutorService、Callable、Future实现有返回结果的多线程。
24.线程的状态转换
25.如何停止一个线程
停止一个线程意味着在任务处理完任务之前停掉正在做的操作，也就是放弃当前的操作。停止一个线程可以用Thread.stop()方法，但最好不要用它。虽然它确实可以停止一个正在运行的线程，但是这个方法是不安全的，而且是已被废弃的方法。
在java中有以下3种方法可以终止正在运行的线程：

1、 使用退出标志，使线程正常退出，也就是当run方法完成后线程终止。

2、使用stop方法强行终止，但是不推荐这个方法，因为stop和suspend及resume一样都是过期作废的方法。

3、使用interrupt方法中断线程。

这个问题简单总结不一定说的清，看一篇网络文章：
http://www.cnblogs.com/greta/p/5624839.html
26.什么是线程安全
线程安全就是多线程访问同一代码，不会产生不确定的结果。
27.如何保证线程安全
对非安全的代码进行加锁控制；
使用线程安全的类；
多线程并发情况下，线程共享的变量改为方法级的局部变量。
28.Synchronized如何使用
synchronized是Java中的关键字，是一种同步锁。它修饰的对象有以下几种：
一. 修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象；
二. 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象；
三. 修改一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象；
四. 修改一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。
29.synchronized和Lock的区别
主要相同点：Lock能完成synchronized所实现的所有功能
主要不同点：Lock有比synchronized更精确的线程语义和更好的性能。Lock的锁定是通过代码实现的，而synchronized是在JVM层面上实现的，synchronized会自动释放锁，而Lock一定要求程序员手工释放，并且必须在finally从句中释放。Lock还有更强大的功能，例如，它的tryLock方法可以非阻塞方式去拿锁。Lock锁的范围有局限性，块范围，而synchronized可以锁住块、对象、类。
30.多线程如何进行信息交互
void notify() 唤醒在此对象监视器上等待的单个线程。
void notifyAll() 唤醒在此对象监视器上等待的所有线程。
void wait() 导致当前的线程等待，直到其他线程调用此对象的notify()方法或notifyAll()方法。
void wait(long timeout) 导致当前的线程等待，直到其他线程调用此对象的notify()方法或notifyAll()方法，或者超过指定的时间量。
void wait(long timeout, int nanos) 导致当前的线程等待，直到其他线程调用此对象的notify()方法或notifyAll()方法，或者其他某个线程中断当前线程，或者已超过某个实际时间量。
31.sleep和wait的区别(考察的方向是是否会释放锁)
sleep()方法是Thread类中方法，而wait()方法是Object类中的方法。
sleep()方法导致了程序暂停执行指定的时间，让出cpu该其他线程，但是他的监控状态依然保持者，当指定的时间到了又会自动恢复运行状态，在调用sleep()方法的过程中，线程不会释放对象锁。而当调用wait()方法的时候，线程会放弃对象锁，进入等待此对象的等待锁定池，只有针对此对象调用notify()方法后本线程才进入对象锁定池准备
32.多线程与死锁
死锁是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，若无外力作用，它们都将无法推进下去。
产生死锁的原因：
一.因为系统资源不足。
二.进程运行推进的顺序不合适。
三.资源分配不当。
33.如何才能产生死锁
产生死锁的四个必要条件：
一.互斥条件：所谓互斥就是进程在某一时间内独占资源。
二.请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
三.不剥夺条件:进程已获得资源，在末使用完之前，不能强行剥夺。
四.循环等待条件:若干进程之间形成一种头尾相接的循环等待资源关系。
34.死锁的预防
打破产生死锁的四个必要条件中的一个或几个，保证系统不会进入死锁状态。
一.打破互斥条件。即允许进程同时访问某些资源。但是，有的资源是不允许被同时访问的，像打印机等等，这是由资源本身的属性所决定的。所以，这种办法并无实用价值。
二.打破不可抢占条件。即允许进程强行从占有者那里夺取某些资源。就是说，当一个进程已占有了某些资源，它又申请新的资源，但不能立即被满足时，它必须释放所占有的全部资源，以后再重新申请。它所释放的资源可以分配给其它进程。这就相当于该进程占有的资源被隐蔽地强占了。这种预防死锁的方法实现起来困难，会降低系统性能。
三.打破占有且申请条件。可以实行资源预先分配策略。即进程在运行前一次性地向系统申请它所需要的全部资源。如果某个进程所需的全部资源得不到满足，则不分配任何资源，此进程暂不运行。只有当系统能够满足当前进程的全部资源需求时，才一次性地将所申请的资源全部分配给该进程。由于运行的进程已占有了它所需的全部资源，所以不会发生占有资源又申请资源的现象，因此不会发生死锁。
四.打破循环等待条件，实行资源有序分配策略。采用这种策略，即把资源事先分类编号，按号分配，使进程在申请，占用资源时不会形成环路。所有进程对资源的请求必须严格按资源序号递增的顺序提出。进程占用了小号资源，才能申请大号资源，就不会产生环路，从而预防了死锁。
35.什么叫守护线程，用什么方法实现守护线程
守护线程是为其他线程的运行提供服务的线程。
setDaemon(boolean on)方法可以方便的设置线程的Daemon模式，true为守护模式，false为用户模式。
36.Java线程池技术及原理
这个有点长，还是看一篇文章吧：
核心类ThreadPoolExecutor
Executor<---ExecutorService<---AbstractExecutorService<---ThreadPoolExecutor（前面两个是接口，后面连个是类）

到这里，大部分朋友应该对任务提交给线程池之后到被执行的整个过程有了一个基本的了解，下面总结一下：

　　1）首先，要清楚corePoolSize和maximumPoolSize的含义；

　　2）其次，要知道Worker是用来起到什么作用的；

　　3）要知道任务提交给线程池之后的处理策略，这里总结一下主要有4点：

- 如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；

- 如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；

- 如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；

- 如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

http://www.importnew.com/19011.html
http://www.cnblogs.com/dolphin0520/p/3932921.html
37.java并发包concurrent及常用的类
这个内容有点多，需要仔细看：
并发包诸类概览：http://www.raychase.net/1912
并发容器

这些容器的关键方法大部分都实现了线程安全的功能，却不使用同步关键字(synchronized)。值得注意的是Queue接口本身定义的几个常用方法的区别，

- add方法和offer方法的区别在于超出容量限制时前者抛出异常，后者返回false；
- remove方法和poll方法都从队列中拿掉元素并返回，但是他们的区别在于空队列下操作前者抛出异常，而后者返回null；
- element方法和peek方法都返回队列顶端的元素，但是不把元素从队列中删掉，区别在于前者在空队列的时候抛出异常，后者返回null。



线程池：http://www.cnblogs.com/dolphin0520/p/3932921.html


锁：http://www.cnblogs.com/dolphin0520/p/3923167.html
Lock提供了比synchronized更多的功能。但是要注意以下几点：

　　1）Lock不是Java语言内置的，synchronized是Java语言的关键字，因此是内置特性。Lock是一个类，通过这个类可以实现同步访问；

　　2）Lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用；而Lock则必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象。

ock lock = ...;
lock.lock();
try{
    //处理任务
}catch(Exception ex){


}finally{
    lock.unlock();   //释放锁
}


.Lock和synchronized的选择

　　总结来说，Lock和synchronized有以下几点不同：

　　1）Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；

　　2）synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；

　　3）Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；

　　4）通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。

　　5）Lock可以提高多个线程进行读操作的效率。

　　在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。

锁的相关概念介绍

1.可重入锁

　　如果锁具备可重入性，则称作为可重入锁。像synchronized和ReentrantLock都是可重入锁，可重入性在我看来实际上表明了锁的分配机制：基于线程的分配，而不是基于方法调用的分配。举个简单的例子，当一个线程执行到某个synchronized方法时，比如说method1，而在method1中会调用另外一个synchronized方法method2，此时线程不必重新去申请锁，而是可以直接执行方法method2。

2.可中断锁

　　可中断锁：顾名思义，就是可以相应中断的锁。

　　在Java中，synchronized就不是可中断锁，而Lock是可中断锁。

　　如果某一线程A正在执行锁中的代码，另一线程B正在等待获取该锁，可能由于等待时间过长，线程B不想等待了，想先处理其他事情，我们可以让它中断自己或者在别的线程中中断它，这种就是可中断锁。

　　在前面演示lockInterruptibly()的用法时已经体现了Lock的可中断性。

3.公平锁

　　公平锁即尽量以请求锁的顺序来获取锁。比如同是有多个线程在等待一个锁，当这个锁被释放时，等待时间最久的线程（最先请求的线程）会获得该所，这种就是公平锁。

　　非公平锁即无法保证锁的获取是按照请求锁的顺序进行的。这样就可能导致某个或者一些线程永远获取不到锁。

　　在Java中，synchronized就是非公平锁，它无法保证等待的线程获取锁的顺序。

　　而对于ReentrantLock和ReentrantReadWriteLock，它默认情况下是非公平锁，但是可以设置为公平锁。
　4.读写锁

　　读写锁将对一个资源（比如文件）的访问分成了2个锁，一个读锁和一个写锁。

　　正因为有了读写锁，才使得多个线程之间的读操作不会发生冲突。

　　ReadWriteLock就是读写锁，它是一个接口，ReentrantReadWriteLock实现了这个接口。

　　可以通过readLock()获取读锁，通过writeLock()获取写锁。

集合：http://www.cnblogs.com/huangfox/archive/2012/08/16/2642666.html
ConcurrentHashMap是线程安全的HashMap的实现。

CopyOnWriteArrayList是一个线程安全、并且在读操作时无锁的ArrayList。
CopyOnWriteArrayList基于ReentrantLock保证了增加元素和删除元素动作的互斥。在读操作上没有任何锁，这样就保证了读的性能，带来的副作用是有时候可能会读取到脏数据。

CopyOnWriteArraySet是基于CopyOnWriteArrayList的，可以知道set是不容许重复数据的，因此add操作和CopyOnWriteArrayList有所区别，他是调用CopyOnWriteArrayList的addIfAbsent方法。
在add方面，CopyOnWriteArraySet效率要比CopyOnWriteArrayList低一点。

ArrayBlockingQueue是一个基于数组、先进先出、线程安全的集合类，其特点是实现指定时间的阻塞读写，并且容量是可以限制的。


38.volatile关键字
用volatile修饰的变量，线程在每次使用变量的时候，都会读取变量修改后的最的值。volatile很容易被误用，用来进行原子性操作。
Java语言中的volatile变量可以被看作是一种 “程度较轻的 synchronized”；与 synchronized 块相比，volatile 变量所需的编码较少，并且运行时开销也较少，但是它所能实现的功能也仅是synchronized的一部分。锁提供了两种主要特性：互斥（mutual exclusion）和可见性（visibility）。互斥即一次只允许一个线程持有某个特定的锁，因此可使用该特性实现对共享数据的协调访问协议，这样，一次就只有一个线程能够使用该共享数据。可见性必须确保释放锁之前对共享数据做出的更改对于随后获得该锁的另一个线程是可见的，如果没有同步机制提供的这种可见性保证，线程看到的共享变量可能是修改前的值或不一致的值，这将引发许多严重问题。Volatile变量具有synchronized的可见性特性，但是不具备原子特性。这就是说线程能够自动发现 volatile 变量的最新值。
要使volatile变量提供理想的线程安全，必须同时满足下面两个条件：对变量的写操作不依赖于当前值；该变量没有包含在具有其他变量的不变式中。
第一个条件的限制使volatile变量不能用作线程安全计数器。虽然增量操作（x++）看上去类似一个单独操作，实际上它是一个由读取－修改－写入操作序列组成的组合操作，必须以原子方式执行，而volatile不能提供必须的原子特性。实现正确的操作需要使 x 的值在操作期间保持不变，而 volatile 变量无法实现这点。
每一个线程运行时都有一个线程栈，线程栈保存了线程运行时候变量值信息。当线程访问某一个对象时候值的时候，首先通过对象的引用找到对应在堆内存的变量的值，然后把堆内存变量的具体值load到线程本地内存中，建立一个变量副本，之后线程就不再和对象在堆内存变量值有任何关系，而是直接修改副本变量的值，在修改完之后的某一个时刻（线程退出之前），自动把线程变量副本的值回写到对象在堆中变量。这样在堆中的对象的值就产生变化了。


read and load 从主存复制变量到当前工作内存
use and assign 执行代码，改变共享变量值
store and write 用工作内存数据刷新主存相关内容
其中use and assign 可以多次出现，但是这一些操作并不是原子性，也就是 在read load之后，如果主内存count变量发生修改之后，线程工作内存中的值由于已经加载，不会产生对应的变化，所以计算出来的结果会和预期不一样。

lock
unlock
read
load
use
assign
store
write

39.Java中的NIO，BIO，AIO分别是什么
BIO:同步并阻塞，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销，当然可以通过线程池机制改善。BIO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，但程序直观简单易理解。
NIO:同步非阻塞，服务器实现模式为一个请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求时才启动一个线程进行处理。NIO方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，编程比较复杂，JDK1.4开始支持。
AIO:异步非阻塞，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理.AIO方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。
40.IO和NIO区别
一.IO是面向流的，NIO是面向缓冲区的。
二.IO的各种流是阻塞的，NIO是非阻塞模式。
三.Java NIO的选择器允许一个单独的线程来监视多个输入通道，你可以注册多个通道使用一个选择器，然后使用一个单独的线程来“选择”通道：这些通道里已经有可以处理的输入，或者选择已准备写入的通道。这种选择机制，使得一个单独的线程很容易来管理多个通道。
41.序列化与反序列化
把对象转换为字节序列的过程称为对象的序列化。
把字节序列恢复为对象的过程称为对象的反序列化。
对象的序列化主要有两种用途：
一.把对象的字节序列永久地保存到硬盘上，通常存放在一个文件中；
二.在网络上传送对象的字节序列。
当两个进程在进行远程通信时，彼此可以发送各种类型的数据。无论是何种类型的数据，都会以二进制序列的形式在网络上传送。发送方需要把这个Java对象转换为字节序列，才能在网络上传送；接收方则需要把字节序列再恢复为Java对象。
42.常见的序列化协议有哪些
Protobuf, Thrift, Hessian, Kryo
43.内存溢出和内存泄漏的区别
内存溢出是指程序在申请内存时，没有足够的内存空间供其使用，出现out of memory。
内存泄漏是指分配出去的内存不再使用，但是无法回收。
44.Java内存模型及各个区域的OOM，如何重现OOM

OutOfMemoryError（下称OOM）

VM运行时数据区域：
1.程序计数器（Program Counter Register），唯一一个不会呀OOM
2.Java虚拟机栈（Java Virtual Machine Stacks）
3.本地方法栈（Native Method Stacks）
4.Java堆（Java Heap）
5.方法区（Method Area）

这部分内容很重要，详细阅读《深入理解Java虚拟机》，也可以详细阅读这篇网络文章http://hllvm.group.iteye.com/group/wiki/2857-JVM
45.出现OOM如何解决
一. 可通过命令定期抓取heap dump或者启动参数OOM时自动抓取heap dump文件。
二. 通过对比多个heap dump，以及heap dump的内容，分析代码找出内存占用最多的地方。
三. 分析占用的内存对象，是否是因为错误导致的内存未及时释放，或者数据过多导致的内存溢出。
46.用什么工具可以查出内存泄漏
一. Memory Analyzer－是一款开源的JAVA内存分析软件，查找内存泄漏，能容易找到大块内存并验证谁在一直占用它，它是基于Eclipse RCP(Rich Client Platform)，可以下载RCP的独立版本或者Eclipse的插件。
二. JProbe－分析Java的内存泄漏。
三. JProfiler－一个全功能的Java剖析工具，专用于分析J2SE和J2EE应用程序。它把CPU、执行绪和内存的剖析组合在一个强大的应用中，GUI可以找到效能瓶颈、抓出内存泄漏、并解决执行绪的问题。
四. JRockit－用来诊断Java内存泄漏并指出根本原因，专门针对Intel平台并得到优化，能在Intel硬件上获得最高的性能。
五. YourKit .NET & Java Profiling业界领先的Java和.NET程序性能分析工具。
六. AutomatedQA －AutomatedQA的获奖产品performance profiling和memory debugging工具集的下一代替换产品，支持Microsoft, Borland, Intel, Compaq 和 GNU编译器。可以为.NET和Windows程序生成全面细致的报告，从而帮助您轻松隔离并排除代码中含有的性能问题和内存/资源泄露问题。支持.Net 1.0,1.1,2.0,3.0和Windows 32/64位应用程序。
七. Compuware DevPartner Java Edition－包含Java内存检测,代码覆盖率测试,代码性能测试,线程死锁,分布式应用等几大功能模块
47.Java内存管理及回收算法
阅读这篇文章：http://www.cnblogs.com/hnrainll/archive/2013/11/06/3410042.html
48.Java类加载器及如何加载类(双亲委派)

类加载器的树状组织结构

Java 中的类加载器大致可以分成两类，一类是系统提供的，另外一类则是由 Java 应用开发人员编写的。系统提供的类加载器主要有下面三个：

- 引导类加载器（bootstrap class loader）：它用来加载 Java 的核心库，是用原生代码来实现的，并不继承自 java.lang.ClassLoader。

- 扩展类加载器（extensions class loader）：它用来加载 Java 的扩展库。Java 虚拟机的实现会提供一个扩展库目录。该类加载器在此目录里面查找并加载 Java 类。

- 系统类加载器（system class loader）：它根据 Java 应用的类路径（CLASSPATH）来加载 Java 类。一般来说，Java 应用的类都是由它来完成加载的。可以通过 ClassLoader.getSystemClassLoader()来获取它。

除了系统提供的类加载器以外，开发人员可以通过继承 java.lang.ClassLoader类的方式实现自己的类加载器，以满足一些特殊的需求。

JVM在加载类时默认采用的是双亲委派机制。通俗的讲，就是某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器，依次递归，如果父类加载器可以完成类加载任务，就成功返回；只有父类加载器无法完成此加载任务时，才自己去加载

 Java 虚拟机是如何判定两个 Java 类是相同的。Java 虚拟机不仅要看类的全名是否相同，还要看加载此类的类加载器是否一样。只有两者都相同的情况，才认为两个类是相同的。即便是同样的字节代码，被不同的类加载器加载之后所得到的类，也是不同的。

阅读文章：https://www.ibm.com/developerworks/cn/java/j-lo-classloader/ （推荐）
或http://blog.csdn.net/zhoudaxia/article/details/35824249
http://www.blogjava.net/zhuxing/archive/2008/08/08/220841.html
49.xml解析方式
一.DOM(JAXP Crimson解析器)
二.SAX
三.JDOM
四.DOM4J
区别：
一.DOM4J性能最好，连Sun的JAXM也在用DOM4J。目前许多开源项目中大量采用DOM4J，例如大名鼎鼎的hibernate也用DOM4J来读取XML配置文件。如果不考虑可移植性，那就采用DOM4J.
二.JDOM和DOM在性能测试时表现不佳，在测试10M文档时内存溢出。在小文档情况下还值得考虑使用DOM和JDOM。虽然JDOM的开发者已经说明他们期望在正式发行版前专注性能问题，但是从性能观点来看，它确实没有值得推荐之处。另外，DOM仍是一个非常好的选择。DOM实现广泛应用于多种编程语言。它还是许多其它与XML相关的标准的基础，因为它正式获得W3C推荐(与基于非标准的Java模型相对)，所以在某些类型的项目中可能也需要它(如在JavaScript中使用DOM)。
三.SAX表现较好，这要依赖于它特定的解析方式－事件驱动。一个SAX检测即将到来的XML流，但并没有载入到内存(当然当XML流被读入时，会有部分文档暂时隐藏在内存中)。
50.Statement和PreparedStatement之间的区别
一.PreparedStatement是预编译的,对于批量处理可以大大提高效率. 也叫JDBC存储过程
二.使用 Statement 对象。在对数据库只执行一次性存取的时侯，用 Statement 对象进行处理。PreparedStatement 对象的开销比Statement大，对于一次性操作并不会带来额外的好处。
三.statement每次执行sql语句，相关数据库都要执行sql语句的编译，preparedstatement是预编译得, preparedstatement支持批处理
四.代码片段1:
String updateString = "UPDATE COFFEES SET SALES = 75 " + "WHERE COF_NAME LIKE ′Colombian′";
stmt.executeUpdate(updateString);
代码片段2:
PreparedStatement updateSales = con.prepareStatement("UPDATE COFFEES SET SALES = ? WHERE COF_NAME LIKE ? ");
updateSales.setInt(1, 75);
updateSales.setString(2, "Colombian");
updateSales.executeUpdate();
片断2和片断1的区别在于，后者使用了PreparedStatement对象，而前者是普通的Statement对象。PreparedStatement对象不仅包含了SQL语句，而且大多数情况下这个语句已经被预编译过，因而当其执行时，只需DBMS运行SQL语句，而不必先编译。当你需要执行Statement对象多次的时候，PreparedStatement对象将会大大降低运行时间，当然也加快了访问数据库的速度。
这种转换也给你带来很大的便利，不必重复SQL语句的句法，而只需更改其中变量的值，便可重新执行SQL语句。选择PreparedStatement对象与否，在于相同句法的SQL语句是否执行了多次，而且两次之间的差别仅仅是变量的不同。如果仅仅执行了一次的话，它应该和普通的对象毫无差异，体现不出它预编译的优越性。
五.执行许多SQL语句的JDBC程序产生大量的Statement和PreparedStatement对象。通常认为PreparedStatement对象比Statement对象更有效,特别是如果带有不同参数的同一SQL语句被多次执行的时候。PreparedStatement对象允许数据库预编译SQL语句，这样在随后的运行中可以节省时间并增加代码的可读性。
然而，在Oracle环境中，开发人员实际上有更大的灵活性。当使用Statement或PreparedStatement对象时，Oracle数据库会缓存SQL语句以便以后使用。在一些情况下,由于驱动器自身需要额外的处理和在Java应用程序和Oracle服务器间增加的网络活动，执行PreparedStatement对象实际上会花更长的时间。
然而，除了缓冲的问题之外，至少还有一个更好的原因使我们在企业应用程序中更喜欢使用PreparedStatement对象,那就是安全性。传递给PreparedStatement对象的参数可以被强制进行类型转换，使开发人员可以确保在插入或查询数据时与底层的数据库格式匹配。
当处理公共Web站点上的用户传来的数据的时候，安全性的问题就变得极为重要。传递给PreparedStatement的字符串参数会自动被驱动器忽略。最简单的情况下，这就意味着当你的程序试着将字符串“D'Angelo”插入到VARCHAR2中时，该语句将不会识别第一个“，”，从而导致悲惨的失败。几乎很少有必要创建你自己的字符串忽略代码。
在Web环境中，有恶意的用户会利用那些设计不完善的、不能正确处理字符串的应用程序。特别是在公共Web站点上,在没有首先通过PreparedStatement对象处理的情况下，所有的用户输入都不应该传递给SQL语句。此外，在用户有机会修改SQL语句的地方，如HTML的隐藏区域或一个查询字符串上，SQL语句都不应该被显示出来。


51.servlet生命周期及各个方法
继承HttpServlet。
我们每次定义一个Servlet的时候，都必须实现doGet或doPost等这些方法

参考文章http://www.cnblogs.com/xuekyo/archive/2013/02/24/2924072.html
52.servlet中如何自定义filter
filter的执行过程：

Filter开发分为二个步骤：

1、编写java类实现Filter接口，并实现(三个方法)其doFilter方法。

2、在 web.xml 文件中使用<filter>和<filter-mapping>元素对编写的filter类进行注册，并设置它所能拦截的资源。

Filter常见应用

（1）统一全站字符编码的过滤器
 2）禁止浏览器缓存所有动态页面的过滤器
（3）控制浏览器缓存页面中的静态资源的过滤器：

场景：有些动态页面中引用了一些图片或css文件以修饰页面效果，这些图片和css文件经常是不变化的，所以为减轻服务器的压力，可以使用filter控制浏览器缓存这些文件，以提升服务器的性能。

4、实现用户自动登陆的过滤器在用户登陆成功后，以cookis形式发送用户名、密码给客户端

5、使用Filter实现URL级别的权限认证

（1）情景：在实际开发中我们经常把一些执行敏感操作的servlet映射到一些特殊目录中，并用filter把这些特殊目录保护起来，限制只能拥有相应访问权限的用户才能访问这些目录下的资源。从而在我们系统中实现一种URL级别的权限功能。

要求：为使Filter具有通用性，Filter保护的资源和相应的访问权限通过filter参数的形式予以配置。

（2）系统中存在很多资源，将需要进行权限控制的资源，放入特殊路径中，编写过滤器管理访问特殊路径的请求，如果没有相应身份和权限，控制无法访问

认证：who are you ? 用户身份的识别 ------------ 登陆功能

权限：以认证为基础 what can you do ? 您能做什么？ 必须先登陆，才有身份，有了身份，才能确定可以执行哪些操作


参考文章http://www.cnblogs.com/javawebsoa/archive/2013/07/31/3228858.html
53.JSP原理
JSP执行过程：
1）首先，客户端发出请求(request )，请求访问JSP网页
2）接着，JSP Container将要访问的.JSP文件 转译成Servlet的源代码（.java文件）
3）然后，将产生的Servlet的源代码（.java文件）经过编译，生成.class文件，并加载到内存执行
4）最后把结果响应(response )给客户端
   执行JSP网页文件时，需要经过两个时期：转译时期(TranslationTime)和请求时期(RequestTime)。
   转译时期：JSP转译成Servlet类(.class文件)。
   请求时期：Servlet类(.class文件)执行后，响应结果至客户端。
  转译期间主要做了两件事情：
   (1)将JSP网页转译为Servlet源代码(.java)，此段称为转译时期(Translation time)；
   (2)将Servlet源代码(.java)编译成Servlet类(.class)，此阶段称为编译时期(Compilation time)。

其实，JSP就是一个Servlet。

参考文章http://blog.csdn.net/hanxuemin12345/article/details/23831645
54.JSP和Servlet的区别
(1)JSP经编译后就变成了“类servlet”。
(2)JSP由HTML代码和JSP标签构成，更擅长页面显示；Servlet更擅长流程控制。
(3)JSP中嵌入JAVA代码，而Servlet中嵌入HTML代码。
55.JSP的动态include和静态include
(1)动态include用jsp:include动作实现，如<jsp:include page="abc.jsp" flush="true" />，它总是会检查所含文件中的变化，适合用于包含动态页面，并且可以带参数。会先解析所要包含的页面，解析后和主页面合并一起显示，即先编译后包含。
(2)静态include用include伪码实现，不会检查所含文件的变化，适用于包含静态页面，如<%@ include file="qq.htm" %>，不会提前解析所要包含的页面，先把要显示的页面包含进来，然后统一编译，即先包含后编译。
56.Struts中请求处理过程
依照上图，我们可以看出一个请求在struts的处理大概有如下步骤：

　　1、客户端初始化一个指向Servlet容器（例如Tomcat）的请求；

　　2、这个请求经过一系列的过滤器（Filter）（这些过滤器中有一个叫做ActionContextCleanUp的可选过滤器，这个过滤器对于Struts2和其他框架的集成很有帮助，例如：SiteMesh Plugin）；

　　3、接着StrutsPrepareAndExecuteFilter被调用，StrutsPrepareAndExecuteFilter询问ActionMapper来决定这个请求是否需要调用某个Action；

　　4、如果ActionMapper决定需要调用某个Action，FilterDispatcher把请求的处理交给ActionProxy；

　　5、ActionProxy通过Configuration Manager询问框架的配置文件，找到需要调用的Action类；

　　6、ActionProxy创建一个ActionInvocation的实例。

　　7、ActionInvocation实例使用命名模式来调用，在调用Action的过程前后，涉及到相关拦截器（Intercepter）的调用。

　　8、一旦Action执行完毕，ActionInvocation负责根据struts.xml中的配置找到对应的返回结果。返回结果通常是（但不总是，也可能是另外的一个Action链）一个需要被表示的JSP或者FreeMarker的模版。在表示的过程中可以使用Struts2 框架中继承的标签。在这个过程中需要涉及到ActionMapper。

首先我们使用struts2框架都会在web.xml中注册和映射struts2，配置内容如下：
<filter>
    <filter-name>struts2</filter-name> 
    <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
  </filter> 
  <filter-mapping>
    <filter-name>struts2</filter-name> 
    <url-pattern>/*</url-pattern>
  </filter-mapping>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
    "http://struts.apache.org/dtds/struts-2.3.dtd">

<struts>
    <!-- 开发模式：每次访问时都会重新读取xml配置文件信息 -->
    <constant name="struts.devMode" value="true"></constant>
    <constant name="struts.action.extension" value="do"></constant>

    <package name="p1" extends="struts-default">
         <action name="helloworld" class="com.itheima.action.HelloWroldAction" method="sayHello">
             <result name="success">/success.jsp</result>
             <result name="error">/error.jsp</result>
         </action>
    </package>

</struts>

参考文章http://www.cnblogs.com/liuling/p/2013-8-10-01.html
57.MVC概念
- Model :模型层（用于数据库打交道）
- View :视图层（用于展示内容给用户看）
- Controller :控制层（控制业务逻辑）

使用MVC的目的是将Model和View的实现代码分离，从而使同一个程序可以使用不同的表现形式。对于同一批数据（Model），可以通过不同的View以不同的形式展示给用户。例如，对于BBS中的贴子，可以用不同的方式来显示。而Controller则是用来控制Model和View进行同步的控制器，如果Model有所改变形，View中应该同步更新相应的数据。

参考文章http://www.cnblogs.com/scwyh/articles/1436802.html
58.Spring mvc与Struts区别


参考文章http://blog.csdn.net/tch918/article/details/38305395
参考文章http://blog.csdn.net/chenleixing/article/details/44570681
59.Hibernate/Ibatis两者的区别
Hibernate与Mybatis对比总结
两者相同点
- Hibernate与MyBatis都可以是通过SessionFactoryBuider由XML配置文件生成SessionFactory，然后由SessionFactory 生成Session，最后由Session来开启执行事务和SQL语句。其中SessionFactoryBuider，SessionFactory，Session的生命周期都是差不多的。
- Hibernate和MyBatis都支持JDBC和JTA事务处理。

Mybatis优势
- MyBatis可以进行更为细致的SQL优化，可以减少查询字段。
- MyBatis容易掌握，而Hibernate门槛较高。

Hibernate优势
- Hibernate的DAO层开发比MyBatis简单，Mybatis需要维护SQL和结果映射。
- Hibernate对对象的维护和缓存要比MyBatis好，对增删改查的对象的维护要方便。
- Hibernate数据库移植性很好，MyBatis的数据库移植性不好，不同的数据库需要写不同SQL。
- Hibernate有更好的二级缓存机制，可以使用第三方缓存。MyBatis本身提供的缓存机制不佳。

他人总结
- Hibernate功能强大，数据库无关性好，O/R映射能力强，如果你对Hibernate相当精通，而且对Hibernate进行了适当的封装，那么你的项目整个持久层代码会相当简单，需要写的代码很少，开发速度很快，非常爽。
- Hibernate的缺点就是学习门槛不低，要精通门槛更高，而且怎么设计O/R映射，在性能和对象模型之间如何权衡取得平衡，以及怎样用好Hibernate方面需要你的经验和能力都很强才行。
- iBATIS入门简单，即学即用，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验，对于没有那么高的对象模型要求的项目来说，相当完美。
- iBATIS的缺点就是框架还是比较简陋，功能尚有缺失，虽然简化了数据绑定代码，但是整个底层数据库查询实际还是要自己写的，工作量也比较大，而且不太容易适应快速数据库修改。

参考文章http://blog.csdn.net/firejuly/article/details/8190229
60.Hibernate一级和二级缓存


参考文章http://blog.csdn.net/windrui/article/details/23165845
61.简述Hibernate常见优化策略


参考文章http://blog.csdn.net/shimiso/article/details/8819114
62.Spring bean的加载过程(推荐看Spring的源码)


参考文章http://geeekr.com/read-spring-source-1-how-to-load-bean/
63.Spring bean的实例化(推荐看Spring的源码)


参考文章http://geeekr.com/read-spring-source-two-beans-initialization/
64.Spring如何实现AOP和IOC(推荐看Spring的源码)


参考文章http://www.360doc.com/content/15/0116/21/12385684_441408260.shtml
65.Spring bean注入方式


参考文章http://blessht.iteye.com/blog/1162131
66.Spring的事务管理


这个主题的参考文章没找到特别好的，http://blog.csdn.net/trigl/article/details/50968079这个还可以。
67.Spring事务的传播特性


参考文章http://blog.csdn.net/lfsf802/article/details/9417095
68.springmvc原理


参考文章http://blog.sina.com.cn/s/blog_7ef0a3fb0101po57.html
69.springmvc用过哪些注解
1、Controller控制器是通过服务接口定义的提供访问应用程序的一种行为，它解释用户的输入，将其转换成一个模型然后将试图呈献给用户。
2、@RequestMapping 既可以作用在类级别，也可以作用在方法级别。当它定义在类级别时，标明该控制器处理所有的请求都被映射到 /favsoft 路径下。@RequestMapping中可以使用 method 属性标记其所接受的方法类型，如果不指定方法类型的话，可以使用 HTTP GET/POST 方法请求数据，但是一旦指定方法类型，就只能使用该类型获取数据。
3、@PathVariable 注解方法参数并将其绑定到URI模板变量的值上
4、@RequestParam将请求的参数绑定到方法中的参数上，如下面的代码所示

5、@RequestBody是指方法参数应该被绑定到HTTP请求Body上。
6、@ResponseBody与@RequestBody类似，它的作用是将返回类型直接输入到HTTP response body中。@ResponseBody在输出JSON格式的数据时，会经常用到

7、@RestController用来创建REST类型的控制器，与@Controller类型。@RestController就是这样一种类型，它避免了你重复的写@RequestMapping与@ResponseBody。

8、 HttpEntity除了能获得request请求和response响应之外，它还能访问请求和响应头，

参考文章http://aijuans.iteye.com/blog/2160141
70.Restful有几种请求


参考文章，http://www.infoq.com/cn/articles/designing-restful-http-apps-roth
71.Restful好处
(1)客户-服务器：客户-服务器约束背后的原则是分离关注点。通过分离用户接口和数据存储这两个关注点，改善了用户接口跨多个平台的可移植性；同时通过简化服务器组件，改善了系统的可伸缩性。
(2)无状态：通信在本质上是无状态的，改善了可见性、可靠性、可伸缩性.
(3)缓存：改善了网络效率减少一系列交互的平均延迟时间，来提高效率、可伸缩性和用户可觉察的性能。
(4)统一接口：REST架构风格区别于其他基于网络的架构风格的核心特征是，它强调组件之间要有一个统一的接口。
72.Tomcat，Apache，JBoss的区别
Apache:HTTP服务器(WEB服务器)，类似IIS，可以用于建立虚拟站点，编译处理静态页面，可以支持SSL技术，支持多个虚拟主机等功能。
Tomcat:Servlet容器，用于解析jsp，Servlet的Servlet容器，是高效，轻量级的容器。缺点是不支持EJB，只能用于java应用。
Jboss:应用服务器，运行EJB的J2EE应用服务器，遵循J2EE规范，能够提供更多平台的支持和更多集成功能，如数据库连接，JCA等，其对Servlet的支持是通过集成其他Servlet容器来实现的，如tomcat和jetty。
73.memcached和redis的区别
(1)性能对比：由于Redis只使用单核，而Memcached可以使用多核，所以平均每一个核上Redis在存储小数据时比Memcached性能更高。而在100k以上的数据中，Memcached性能要高于Redis，虽然Redis最近也在存储大数据的性能上进行优化，但是比起Memcached，还是稍有逊色。
(2)内存使用效率对比：使用简单的key-value存储的话，Memcached的内存利用率更高，而如果Redis采用hash结构来做key-value存储，由于其组合式的压缩，其内存利用率会高于Memcached。
(3)Redis支持服务器端的数据操作：Redis相比Memcached来说，拥有更多的数据结构和并支持更丰富的数据操作，通常在Memcached里，你需要将数据拿到客户端来进行类似的修改再set回去。这大大增加了网络IO的次数和数据体积。在Redis中，这些复杂的操作通常和一般的GET/SET一样高效。所以，如果需要缓存能够支持更复杂的结构和操作，那么Redis会是不错的选择。
74.如何理解分布式锁
参考文章http://blog.csdn.net/zheng0518/article/details/51607063和http://blog.csdn.net/nicewuranran/article/details/51730131
75.你知道的开源协议有哪些
常见的开源协议有GPL、LGPL、BSD、Apache Licence vesion 2.0、MIT，详细内容参考文章http://blog.jobbole.com/44175/和http://www.ruanyifeng.com/blog/2011/05/how_to_choose_free_software_licenses.html
76.json和xml区别
XML:
(1)应用广泛，可扩展性强，被广泛应用各种场合；
(2)读取、解析没有JSON快；
(3)可读性强，可描述复杂结构。
JSON:
(1)结构简单，都是键值对；
(2)读取、解析速度快，很多语言支持；
(3)传输数据量小，传输速率大大提高；
(4)描述复杂结构能力较弱。
77.设计模式
参考文章http://www.cnblogs.com/beijiguangyong/archive/2010/11/15/2302807.html#_Toc281750445
78.设计模式的六大原则
参考文章http://www.uml.org.cn/sjms/201211023.asp
79.用一个设计模式写一段代码或画出一个设计模式的UML
参考文章http://www.cnblogs.com/beijiguangyong/archive/2010/11/15/2302807.html#_Toc281750445
80.高内聚，低耦合方面的理解
参考文章http://my.oschina.net/heweipo/blog/423235
81.深度优先和广度优先算法
推荐看书籍复习！网络文章只做参考，http://blog.163.com/zhoumhan_0351/blog/static/3995422720098342257387/
http://blog.163.com/zhoumhan_0351/blog/static/3995422720098711040303/
http://blog.csdn.net/andyelvis/article/details/1728378
http://driftcloudy.iteye.com/blog/782873
82.排序算法及对应的时间复杂度和空间复杂度
推荐看书籍复习！网络文章只做参考，http://www.cnblogs.com/liuling/p/2013-7-24-01.html
http://blog.csdn.net/cyuyanenen/article/details/51514443
http://blog.csdn.net/whuslei/article/details/6442755
83.排序算法编码实现
参考http://www.cnblogs.com/liuling/p/2013-7-24-01.html
84.查找算法
参考http://sanwen8.cn/p/142Wbu5.html
85.B+树
参考http://www.cnblogs.com/syxchina/archive/2011/03/02/2197251.html
86.KMP算法
推荐阅读数据复习！参考http://www.cnblogs.com/c-cloud/p/3224788.html
87.hash算法及常用的hash算法
1、hash函数
2、处理冲突
3、负载因子

可如下描述哈希表：根据设定的哈希函数H(key)和所选中的处理冲突的方法，将一组关键字映象到一个有限的、地址连续的地址集(区间)上并以关键字在地址集中的“象”作为相应记录在表中的存储位置，这种表被称为哈希表。

参考http://www.360doc.com/content/13/0409/14/10384031_277138819.shtml
88.如何判断一个单链表是否有环？
参考http://www.jianshu.com/p/0e28d31600dd
参考http://my.oschina.net/u/2391658/blog/693277?p={{totalPage}}
89.队列、栈、链表、树、堆、图
推荐阅读数据复习！
90.linux常用命令
阅读http://www.jianshu.com/p/03cfc1a721b8
91.如何查看内存使用情况
参考http://blog.csdn.net/windrui/article/details/40046413
92.Linux下如何进行进程调度
推荐阅读书籍复习，参考
http://www.cnblogs.com/zhaoyl/archive/2012/09/04/2671156.html
http://blog.csdn.net/rainharder/article/details/7975387
93.产生死锁的必要条件
参考http://blog.sina.com.cn/s/blog_5e3604840100ddgq.html
94.死锁预防
参考http://blog.sina.com.cn/s/blog_5e3604840100ddgq.html
95.数据库范式
参考http://www.360doc.com/content/12/0712/20/5287961_223855037.shtml
96.数据库事务隔离级别
参考http://blog.csdn.net/fg2006/article/details/6937413
97.数据库连接池的原理
对于共享资源，有一个很著名的设计模式：资源池（resource pool）


参考http://blog.csdn.net/shuaihj/article/details/14223015
98.乐观锁和悲观锁
参考http://www.open-open.com/lib/view/open1452046967245.html
99.如何实现不同数据库的数据查询分页
参考http://blog.csdn.net/yztezhl/article/details/20489387
100.SQL注入的原理，如何预防
参考https://www.aliyun.com/zixun/content/3_15_245099.html
101.数据库索引的实现(B+树介绍、和B树、R树区别)
参考http://blog.csdn.net/kennyrose/article/details/7532032
http://www.xuebuyuan.com/2216918.html
102.SQL性能优化


参考http://database.51cto.com/art/200904/118526.htm
http://www.cnblogs.com/rootq/archive/2008/11/17/1334727.html
103.数据库索引的优缺点以及什么时候数据库索引失效
参考http://www.cnblogs.com/mxmbk/articles/5226344.html
http://www.cnblogs.com/simplefrog/archive/2012/07/15/2592527.html
http://www.open-open.com/lib/view/open1418476492792.html
http://blog.csdn.net/colin_liu2009/article/details/7301089
http://www.cnblogs.com/hongfei/archive/2012/10/20/2732589.html
104.Redis的数据类型
参考http://blog.csdn.net/hechurui/article/details/49508735
105.OSI七层模型以及TCP/IP四层模型
参考http://blog.csdn.net/sprintfwater/article/details/8751453
http://www.cnblogs.com/commanderzhu/p/4821555.html
http://blog.csdn.net/superjunjin/article/details/7841099
106.HTTP和HTTPS区别
参考http://blog.csdn.net/mingli198611/article/details/8055261
http://www.mahaixiang.cn/internet/1233.html
107.HTTP报文内容
参考https://yq.aliyun.com/articles/44675
http://www.cnblogs.com/klguang/p/4618526.html
http://my.oschina.net/orgsky/blog/387759
108.get提交和post提交的区别
参考
http://www.cnblogs.com/hyddd/archive/2009/03/31/1426026.html
http://www.jellythink.com/archives/806
109.get提交是否有字节限制，如果有是在哪限制的
参考http://www.jellythink.com/archives/806
110.TCP的三次握手和四次挥手
阅读http://www.jianshu.com/p/f7d1010fa603
111.session和cookie的区别
参考http://www.cnblogs.com/shiyangxt/archive/2008/10/07/1305506.html
112.HTTP请求中Session实现原理
参考http://blog.csdn.net/zhq426/article/details/2992488
113.redirect与forward区别
参考http://www.cnblogs.com/wxgblogs/p/5602849.html
114.TCP和UDP区别
参考http://www.cnblogs.com/bizhu/archive/2012/05/12/2497493.html
115.DDos攻击及预防
参考http://blog.csdn.net/huwei2003/article/details/45476743
http://www.leiphone.com/news/201509/9zGlIDvLhwguqOtg.html


面经总结的知识点1
J2SE基础
1. 九种基本数据类型的大小，以及他们的封装类。
2. Switch能否用string做参数？
3. equals与==的区别。
4. Object有哪些公用方法？
5. Java的四种引用，强弱软虚，用到的场景。
6. Hashcode的作用。
7. ArrayList、LinkedList、Vector的区别。
8. String、StringBuffer与StringBuilder的区别。
9. Map、Set、List、Queue、Stack的特点与用法。
10. HashMap和HashTable的区别。
11. HashMap和ConcurrentHashMap的区别，HashMap的底层源码。
12. TreeMap、HashMap、LindedHashMap的区别。
13. Collection包结构，与Collections的区别。
14. try catch finally，try里有return，finally还执行么？
15. Excption与Error包结构。OOM你遇到过哪些情况，SOF你遇到过哪些情况。
16. Java面向对象的三个特征与含义。
17. Override和Overload的含义去区别。
18. Interface与abstract类的区别。
19. Static class 与non static class的区别。
20. java多态的实现原理。
21. 实现多线程的两种方法：Thread与Runable。
22. 线程同步的方法：sychronized、lock、reentrantLock等。
23. 锁的等级：方法锁、对象锁、类锁。
24. 写出生产者消费者模式。
25. ThreadLocal的设计理念与作用。
26. ThreadPool用法与优势。
27. Concurrent包里的其他东西：ArrayBlockingQueue、CountDownLatch等等。
28. wait()和sleep()的区别。
29. foreach与正常for循环效率对比。
30. Java IO与NIO。
31. 反射的作用于原理。
32. 泛型常用特点，List<String>能否转为List<Object>。
33. 解析XML的几种方式的原理与特点：DOM、SAX、PULL。
34. Java与C++对比。
35. Java1.7与1.8新特性。
36. 设计模式：单例、工厂、适配器、责任链、观察者等等。
37. JNI的使用。
Java里有很多很杂的东西，有时候需要你阅读源码，大多数可能书里面讲的不是太清楚，需要你在网上寻找答案。
推荐书籍：《java核心技术卷I》《Thinking in java》《java并发编程》《effictive java》《大话设计模式》

JVM
1. 内存模型以及分区，需要详细到每个区放什么。
2. 堆里面的分区：Eden，survival from to，老年代，各自的特点。
3. 对象创建方法，对象的内存分配，对象的访问定位。
4. GC的两种判定方法：引用计数与引用链。
5. GC的三种收集方法：标记清除、标记整理、复制算法的原理与特点，分别用在什么地方，如果让你优化收集方法，有什么思路？
6. GC收集器有哪些？CMS收集器与G1收集器的特点。
7. Minor GC与Full GC分别在什么时候发生？
8. 几种常用的内存调试工具：jmap、jstack、jconsole。
9. 类加载的五个过程：加载、验证、准备、解析、初始化。
10. 双亲委派模型：Bootstrap ClassLoader、Extension ClassLoader、ApplicationClassLoader。
11. 分派：静态分派与动态分派。
JVM过去过来就问了这么些问题，没怎么变，内存模型和GC算法这块问得比较多，可以在网上多找几篇博客来看看。
推荐书籍：《深入理解java虚拟机》

操作系统
1. 进程和线程的区别。
2. 死锁的必要条件，怎么处理死锁。
3. Window内存管理方式：段存储，页存储，段页存储。
4. 进程的几种状态。
5. IPC几种通信方式。
6. 什么是虚拟内存。
7. 虚拟地址、逻辑地址、线性地址、物理地址的区别。
因为是做android的这一块问得比较少一点，还有可能上我简历上没有写操作系统的原因。
推荐书籍：《深入理解现代操作系统》

TCP/IP
1. OSI与TCP/IP各层的结构与功能，都有哪些协议。
2. TCP与UDP的区别。
3. TCP报文结构。
4. TCP的三次握手与四次挥手过程，各个状态名称与含义，TIMEWAIT的作用。
5. TCP拥塞控制。
6. TCP滑动窗口与回退N针协议。
7. Http的报文结构。
8. Http的状态码含义。
9. Http request的几种类型。
10. Http1.1和Http1.0的区别
11. Http怎么处理长连接。
12. Cookie与Session的作用于原理。
13. 电脑上访问一个网页，整个过程是怎么样的：DNS、HTTP、TCP、OSPF、IP、ARP。
14. Ping的整个过程。ICMP报文是什么。
15. C/S模式下使用socket通信，几个关键函数。
16. IP地址分类。
17. 路由器与交换机区别。
网络其实大体分为两块，一个TCP协议，一个HTTP协议，只要把这两块以及相关协议搞清楚，一般问题不大。
推荐书籍：《TCP/IP协议族》

数据结构与算法
1. 链表与数组。
2. 队列和栈，出栈与入栈。
3. 链表的删除、插入、反向。
4. 字符串操作。
5. Hash表的hash函数，冲突解决方法有哪些。
6. 各种排序：冒泡、选择、插入、希尔、归并、快排、堆排、桶排、基数的原理、平均时间复杂度、最坏时间复杂度、空间复杂度、是否稳定。
7. 快排的partition函数与归并的Merge函数。
8. 对冒泡与快排的改进。
9. 二分查找，与变种二分查找。
10. 二叉树、B+树、AVL树、红黑树、哈夫曼树。
11. 二叉树的前中后续遍历：递归与非递归写法，层序遍历算法。
12. 图的BFS与DFS算法，最小生成树prim算法与最短路径Dijkstra算法。
13. KMP算法。
14. 排列组合问题。
15. 动态规划、贪心算法、分治算法。（一般不会问到）
16. 大数据处理：类似10亿条数据找出最大的1000个数.........等等
算法的话其实是个重点，因为最后都是要你写代码，所以算法还是需要花不少时间准备，这里有太多算法题，写不全，我的建议是没事多在OJ上刷刷题（牛客网、leetcode等），剑指offer上的算法要能理解并自己写出来，编程之美也推荐看一看。
推荐书籍：《大话数据结构》《剑指offer》《编程之美》


面试3
1、
CVTE

二分查找，
N个数中求前M大个数
struts2和SpringMVC的区别、Spring中IoC和AOP的理解
MySQL中如何定为查询效率较慢的SQL语句，比如慢查询日志、EXPLAIN关键字还有PROFILES等

百度
1. 是否了解动态规划
动归，本质上是一种划分子问题的算法，站在任何一个子问题的处理上看，当前子问题的提出都要依据现有的类似结论，而当前问题的结论是后 面问题求解的铺垫。任何DP都是基于存储的算法，核心是状态转移方程。
2. JVM调优
其实我没有实际的调优经验，但是我主要介绍了一下JVM的分区、堆的分代以及回收算法还有OOM异常的处理思路
3. 分别介绍一下Struts2和Spring
不用多说，这方面比我答得好的同学肯定大有人在，就不出丑了
4. 职责链模式（设计模式）
GoF经典设计模式的一种
5. 实践中如何优化MySQL
我当时是按以下四条依次回答的，他们四条从效果上第一条影响最大，后面越来越小。
① SQL语句及索引的优化
② 数据库表结构的优化
③ 系统配置的优化
④ 硬件的优化
6. 什么情况下设置了索引但无法使用
① 以“%”开头的LIKE语句，模糊匹配
② OR语句前后没有同时使用索引
③ 数据类型出现隐式转化（如varchar不加单引号的话可能会自动转换为int型）
7. SQL语句的优化
order by要怎么处理
alter尽量将多次合并为一次
insert和delete也需要合并
等等
8. 索引的底层实现原理和优化
B+树，经过优化的B+树
主要是在所有的叶子结点中增加了指向下一个叶子节点的指针，因此InnoDB建议为大部分表使用默认自增的主键作为主索引。
9. HTTP和HTTPS的主要区别
10. Cookie和Session的区别

11. 如何设计一个高并发的系统
① 数据库的优化，包括合理的事务隔离级别、SQL语句优化、索引的优化
② 使用缓存，尽量减少数据库 IO
③ 分布式数据库、分布式缓存
④ 服务器的负载均衡
12. linux中如何查看进程等命令

13. 两条相交的单向链表，如何求他们的第一个公共节点
很简单的链表题目，博客上的做法一搜一大把，我记得当时答在兴头上，又给面试官解释了一下如何求单向局部循环链表的入口，链表中很经典的问题（其实链表也就那几个常用算法，比如逆制、求倒数第K个节点，判断是否有环等）








单例模式、简单工厂模式、工厂模式、抽象工厂模式、策略模式、观察者模式、组合模式、适配器模式、装饰模式、代理模式、外观模式。然后就是设计一个公司下有部门、部门下有经理和员工，经理可以管理经理和员工这样的一个模型，组合模式一套用就行了

阿里

1. 二叉树的遍历方式，前序、中序、后序和层序
二叉树本身就是一个递归的产物，那前序举例，访问根节点，然后左节点，再右节点，如果左节点是一棵子树，那么就先访问左子树的根节点，再访问左子树的左节点，依次递归；而层序，使用队列进行辅助，实现广度优先搜索
2. volatile关键字
给大家推荐两本书：《Java多线程实战》和《Java并发编程的艺术》，这会儿已经三点了，脑子有点乱书名可能未必无误。对Java实现多线程描述的非常详细。现场跟面试官老师扯了很多，我在这里挑主要的说
volatile关键字是Java并发的最轻量级实现，本质上有两个功能，在生成的汇编语句中加入LOCK关键字和内存屏障
作用就是保证每一次线程load和write两个操作，都会直接从主内存中进行读取和覆盖，而非普通变量从线程内的工作空间（默认各位已经熟悉Java多线程内存模型）
但它有一个很致命的缺点，导致它的使用范围不多，就是他只保证在读取和写入这两个过程是线程安全的。如果我们对一个volatile修饰的变量进行多线程下的自增操作，还是会出现线程安全问题。根本原因在于volatile关键字无法对自增进行安全性修饰，因为自增分为三步，读取-》+1-》写入。中间多个线程同时执行+1操作，还是会出现线程安全性问题。
3. synchronized
锁的优化：偏向锁、轻量级锁、自旋锁、重量级锁
锁的膨胀模型，以及锁的优化原理，为什么要这样设计
与Concurrent包下的Lock的区别和联系
Lock能够实现synchronized的所有功能，同时，能够实现长时间请求不到锁时自动放弃、通过构造方法实现公平锁、出现异常时synchronized会由JVM自动释放，而Lock必须手动释放，因此我们需要把unLock()方法放在finally{}语句块中
4. concurrentHashMap
两个hash过程，第一次找到所在的桶，并将桶锁定，第二次执行写操作。
而读操作不加锁，JDK1.8中ConcurrentHashMap从1600行激增到6000行，中间做了很多细粒度的优化，大家可以查一下。
5. 锁的优化策略
① 读写分离
② 分段加锁
③ 减少锁持有的时间
④ 多个线程尽量以相同的顺序去获取资源
等等，这些都不是绝对原则，都要根据情况，比如不能将锁的粒度过于细化，不然可能会出现线程的加锁和释放次数过多，反而效率不如一次加一把大锁。这部分跟面试官谈了很久
6. 操作系统






在项目部分，可能是我整个阿里面试过程中最提心吊胆的，缓存的使用，如果现在需要实现一个简单的缓存，供搜索框中的ajax异步请求调用，使用什么结构？我回答ConcurrentHashMap，可是内存中的缓存不能一直存在，用什么算法定期将搜索权重较低的entry去掉？我说先按热度递减放进一个CopyOnWriteArrayList中，保留前多少个然后再存回ConcurrentHashMap中，面试官说效率过低，有没有更高效的算法，我假装冥思苦想（用假装其实是因为，确实想不到方法）
后来面试官说其实这个问题有点难了，换一个，又跟我扯到线程的问题，大体就跟一面面试官问的差不多，就不赘述了。这部分感觉面试官还比较满意，就问题TCP如何保证安全性，我说三次握手、四次回收、超时重传、保序性、奇偶校验、去重、拥塞控制。还讲了滑动窗口模型。
后面又考了一些红黑树的问题，问到B+数，还有JDK1.8中对HashMap的增强，如果一个桶上的节点数量过多，链表+数组的结构就会转换为红黑树。
面试官问我项目中使用的单机服务器，如果将它部署成分布式服务器？我当时心里一惊，这个问题确实没有准备过，眼看就要被问死了，临时抖了个机灵，说有一次跟一个师兄尝试这么做的时候，遇到了session共享问题，然后成功地把面试官引向了session共享的问题，跟他讨论了10分钟左右的分布式系统中如何做到session共享。后面面试官可能也觉得我这部分
手写一个线程安全的单例模式，经典的不能再经典，没什么好说的，懒汉饿汉随便选一个。
还有一些MySQL的常见优化方式、定为慢查询等，回答的七七八八，之前面试总结的问题还有印象，所以感谢自己有面试完及时总结的习惯。





网易2017秋招---八道
1、
[编程题] 回文序列

如果一个数字序列逆置之后跟原序列是一样的就称这样的数字序列为回文序列。例如：
{1, 2, 1}, {15, 78, 78, 15} , {112} 是回文序列,
{1, 2, 2}, {15, 78, 87, 51} ,{112, 2, 11} 不是回文序列。
现在给出一个数字序列，允许使用一种转换操作：
选择任意两个相邻的数，然后从序列移除这两个数，并用这两个数字的和插入到这两个数之前的位置(只插入一个和)。
现在对于所给序列要求出最少需要多少次操作可以将其变成回文序列。

输入描述:

输入为两行，第一行为序列长度n ( 1 ≤ n ≤ 50)
第二行为序列中的n个整数item[i]  (1 ≤ iteam[i] ≤ 1000)，以空格分隔。


输出描述:

输出一个数，表示最少需要的转换次数


输入例子:

4
1 1 1 3


输出例子:

2


package niuke;

import java.util.Scanner;

public class Huiwen {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int res = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
            }

            int left = 0;
            int right = array.length - 1;
            while (left < right) {
                if (array[left] == array[right]) {
                    left++;
                    right--;
                } else if (array[left] < array[right]) {
                    int temp = array[left] + array[left + 1];
                    left++;
                    array[left] = temp;
                    res++;
                } else {
                     int temp = array[right] + array[right - 1];
                     right--;
                     array[right] = temp;
                     res++;
                }
            }
            System.out.println(res);
        }
        scanner.close();

    }

}
2、
小易有一个圆心在坐标原点的圆，小易知道圆的半径的平方。小易认为在圆上的点而且横纵坐标都是整数的点是优雅的，小易现在想寻找一个算法计算出优雅的点的个数，请你来帮帮他。
例如：半径的平方如果为25
优雅的点就有：(+/-3, +/-4), (+/-4, +/-3), (0, +/-5) (+/-5, 0)，一共12个点。
输入描述:

输入为一个整数，即为圆半径的平方,范围在32位int范围内。


输出描述:

输出为一个整数，即为优雅的点的个数


输入例子:

25


输出例子:

12


Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int res = 0;
        double r = Math.sqrt(n);
        for (int i = 0; i < r; i++) {
            double temp = Math.sqrt(n - i * i);
            if ((int)temp == temp) {
                res++;
            }
        }
        System.out.print(res << 2);
3、

小易来到了一条石板路前，每块石板上从1挨着编号为：1、2、3.......
这条石板路要根据特殊的规则才能前进：对于小易当前所在的编号为K的 石板，小易单次只能往前跳K的一个约数(不含1和K)步，即跳到K+X(X为K的一个非1和本身的约数)的位置。 小易当前处在编号为N的石板，他想跳到编号恰好为M的石板去，小易想知道最少需要跳跃几次可以到达。
例如：
N = 4，M = 24：
4->6->8->12->18->24
于是小易最少需要跳跃5次，就可以从4号石板跳到24号石板
输入描述:

输入为一行，有两个整数N，M，以空格隔开。
(4 ≤ N ≤ 100000)
(N ≤ M ≤ 100000)


输出描述:

输出小易最少需要跳跃的步数,如果不能到达输出-1


输入例子:

4 24


输出例子:

5



4、
[编程题] 暗黑的字符串

一个只包含'A'、'B'和'C'的字符串，如果存在某一段长度为3的连续子串中恰好'A'、'B'和'C'各有一个，那么这个字符串就是纯净的，否则这个字符串就是暗黑的。例如：
BAACAACCBAAA 连续子串"CBA"中包含了'A','B','C'各一个，所以是纯净的字符串
AABBCCAABB 不存在一个长度为3的连续子串包含'A','B','C',所以是暗黑的字符串
你的任务就是计算出长度为n的字符串(只包含'A'、'B'和'C')，有多少个是暗黑的字符串。
输入描述:

输入一个整数n，表示字符串长度(1 ≤ n ≤ 30)


输出描述:

输出一个整数表示有多少个暗黑字符串


输入例子:

2
3


输出例子:

9
21



5、

[编程题] 数字翻转

对于一个整数X，定义操作rev(X)为将X按数位翻转过来，并且去除掉前导0。例如:
如果 X = 123，则rev(X) = 321;
如果 X = 100，则rev(X) = 1.
现在给出整数x和y,要求rev(rev(x) + rev(y))为多少？
输入描述:

输入为一行，x、y(1 ≤ x、y ≤ 1000)，以空格隔开。


输出描述:

输出rev(rev(x) + rev(y))的值


输入例子:

123 100


输出例子:

223


public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        int y = in.nextInt();

        while (x % 10 == 0) {
            x /= 10;
        }
        while (y % 10 == 0) {
            y /= 10;
        }
        //100 123

        int temp = integerReverse(x) + integerReverse(y);
        while (temp % 10 == 0) {
            temp /= 10;
        }
        System.out.println(integerReverse(temp));
    }
    public static int integerReverse(int n) {
        char[] c = String.valueOf(n).toCharArray();
        int left = 0;
        int right = c.length - 1;
        while (left < right) {
            char temp = c[left];
            c[left] = c[right];
            c[right] = temp;
            left++;
            right--;
        }
        return Integer.valueOf(String.valueOf(c));
    }
6、
[编程题] 最大的奇约数

小易是一个数论爱好者，并且对于一个数的奇数约数十分感兴趣。一天小易遇到这样一个问题： 定义函数f(x)为x最大的奇数约数，x为正整数。 例如:f(44) = 11.
现在给出一个N，需要求出 f(1) + f(2) + f(3).......f(N)
例如： N = 7
f(1) + f(2) + f(3) + f(4) + f(5) + f(6) + f(7) = 1 + 1 + 3 + 1 + 5 + 3 + 7 = 21
小易计算这个问题遇到了困难，需要你来设计一个算法帮助他。
输入描述:

输入一个整数N (1 ≤ N ≤ 1000000000)


输出描述:

输出一个整数，即为f(1) + f(2) + f(3).......f(N)


输入例子:

7


输出例子:

21



7、
[编程题] 买苹果

小易去附近的商店买苹果，奸诈的商贩使用了捆绑交易，只提供6个每袋和8个每袋的包装(包装不可拆分)。 可是小易现在只想购买恰好n个苹果，小易想购买尽量少的袋数方便携带。如果不能购买恰好n个苹果，小易将不会购买。
输入描述:

输入一个整数n，表示小易想购买n(1 ≤ n ≤ 100)个苹果


输出描述:

输出一个整数表示最少需要购买的袋数，如果不能买恰好n个苹果则输出-1


输入例子:

20


输出例子:

3


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int res = -1;
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n % 8 == 0) {
            System.out.println(n / 8);
            return;
        }
        for (int i = n / 8; i >= 0; i--) {
           if ((n - 8 * i) % 6 == 0) {
               res = i + (n - 8 * i) / 6;
               break;
           }
        }
        System.out.print(res);         
    }
}
8、
[编程题] 计算糖果

A,B,C三个人是好朋友,每个人手里都有一些糖果,我们不知道他们每个人手上具体有多少个糖果,但是我们知道以下的信息：
A - B, B - C, A + B, B + C. 这四个数值.每个字母代表每个人所拥有的糖果数.
现在需要通过这四个数值计算出每个人手里有多少个糖果,即A,B,C。这里保证最多只有一组整数A,B,C满足所有题设条件。
输入描述:

输入为一行，一共4个整数，分别为A - B，B - C，A + B，B + C，用空格隔开。
范围均在-30到30之间(闭区间)。


输出描述:

输出为一行，如果存在满足的整数A，B，C则按顺序输出A，B，C，用空格隔开，行末无空格。
如果不存在这样的整数A，B，C，则输出No


输入例子:

1 -2 3 4


输出例子:

2 1 3


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] array = new int[4];
        for (int i = 0; i < 4; i++) {
            array[i] = in.nextInt();
        }
        int b1 = (array[2] - array[0]);
        int b2 = (array[1] + array[3]);
        if ((b1 % 2 == 0) && (b2 % 2 == 0) && b1 == b2) {
            int b = b1 / 2;
            int a = b + array[0];
            int c = array[3] - b;
            System.out.print(a + " " + b + " " + c );
        } else {
            System.out.println("No");
        }
    }
}


网易2015校招JAVA工程师笔试题
1、java异常类的结构
io异常必须捕获
runtimeexception可以不捕获

2、
类中的加载顺序：
1.静态代码块 2.构造代码块3.构造方法的执行顺序是1>2>3;明白他们是干嘛的就理解了。

1.静态代码块：是在类的加载过程的第三步初始化的时候进行的，主要目的是给类变量赋予初始值。

2.构造代码块：是独立的，必须依附载体才能运行，Java会把构造代码块放到每种构造方法的前面，用于实例化一些共有的实例变量，减少代码量。

3.构造方法：用于实例化变量。

1是类级别的，2、3是实例级别的，自然1要优先23.

在就明白一点：对子类得主动使用会导致对其父类得主动使用，所以尽管实例化的是子类，但也会导致父类的初始化和实例化，且优于子类执行。

public class HelloB extends HelloA
{
 public HelloB()
 {
 }
 {
     System.out.println("I’m B class");
 }
 static
 {
     System.out.println("static B");
 }
 public static void main(String[] args)
 {
     new HelloB();
 }
}
class HelloA
{
 public HelloA()
 {
 }
 {
     System.out.println("I’m A class");
 }
 static
 {
     System.out.println("static A");
 }
}
结果：
static A
static B
I’m A class
I’m B class

3、

public class T {
     public static void main(String[] args) {
           int i = 0;
           i = i++;
           System.out.println(i);
        }
}


结果：0
4、 实现回话跟踪技术
1，使用cookie

2，隐藏表单域

3，URL重写

4，session
5、jvm的类加载结构
jvm classLoader architecture :
a、Bootstrap ClassLoader/启动类加载器
主要负责jdk_home/lib目录下的核心 api 或 -Xbootclasspath 选项指定的jar包装入工作.
B、Extension ClassLoader/扩展类加载器
主要负责jdk_home/lib/ext目录下的jar包或 -Djava.ext.dirs 指定目录下的jar包装入工作
C、System ClassLoader/系统类加载器
主要负责java -classpath/-Djava.class.path所指的目录下的类与jar包装入工作.
B、 User Custom ClassLoader/用户自定义类加载器(java.lang.ClassLoader的子类)
在程序运行期间, 通过java.lang.ClassLoader的子类动态加载class文件, 体现java动态实时类装入特性

6、
任意2n个整数，从其中选出n个整数，使得选出的n个整数和同剩下的n个整数之和的差最小。
7、
有两个有序的集合，集合的每个元素都是一段范围，求其交集，例如集合{[4,8],[9,13]}和{[6,12]}的交集为{[6,8],[9,12]}

8、
一个文件中有10000个数，用Java实现一个多线程程序将这个10000个数输出到5个不用文件中（不要求输出到每个文件中的数量相同）。要求启动10个线程，两两一组，分为5组。每组两个线程分别将文件中的奇数和偶数输出到该组对应的一个文件中，需要偶数线程每打印10个偶数以后，就将奇数线程打印10个奇数，如此交替进行。同时需要记录输出进度，每完成1000个数就在控制台中打印当前完成数量，并在所有线程结束后，在控制台打印”Done”.
9、




搜狐
1、http状态码

304未修改（表示客户机缓存的版本是最新的，客户机应该继续使用它。）

404找不到改页面

302暂时重定向

400代表客户端发起的请求不符合服务器对请求的某些限制，或者请求本身存在一定的错误。
2、ip地址的种类，以及计算
IP地址=网络号+主机号。

根据子网掩码255.255.254.0，可以看出，前两段都已满，第三段二进制是1111 1110，最后一位可用，最后一段8位可用。

所以可用主机地址为：2^9=512。

全1和全0地址留作特殊用途，题目又说网关设备用一个地址，所以512-3=509
3、数据结构的特点

再来说说数据结构：

数组：连续存储，遍历快且方便，长度固定，缺点移除和添加 需要迁移n个数据或者后移n个数据

链表：离散存储，添加删除方便，空间和时间消耗大，双向链表比单向的更灵活，但是空间耗费也更大

Hash表：数据离散存储，利用hash算法决定存储位置，遍历麻烦。以java的HashMap为例

二叉树：一般的查找遍历，有深度优先和广度优先，遍历分前序、中序、后序遍历，效率都差不多，但是如果数据经过排序，二叉树效率还是不错。

图：表示物件与物件之间的关系的数学对象，常用遍历方式深度优先遍历和广度优先遍历，这两种遍历方式对有向图和无向图均适用，遍历查找不及前面人一种数据结构。







