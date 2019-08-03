# 1、两个数字求和，不使用算法运算符(位运算)

public int add(int a,int b) {
        if (b==0) {
            return a;//当进位为0的时候结束
        }
        int sum=a^b;//相加不进位
        int carry = (a&b) << 1;//进位，但不相加
        return add(sum,carry);//递归
    }
2、给一副牌（52张）使得每种组合的概率一样（假定一个随机发生器）
先打乱钱n-1个元素的次序，然后取出第n个，将他和数组中的元素随机交换

int rand(int lower,int higher){
        return lower+(int)(Math.random()*(higher-lower+1));
    }
//递归
    int[] shuffArrayRecursive(int[] a,int i) {
        if (i == 0) {
            return a;
        }
        shuffArrayRecursive(a,i-1);
        int k = rand(0,i);
        int temp = a[k];
        a[k] = a[i];
        a[i] = temp;
        return a;
    }
//迭代
    void shuffArrayRecursive1(int[] a) {

        for(int i = 0;i < a.length;i++) {
            int k = rand(0,i);
            int temp = a[k];
            a[k] = a[i];
            a[i] = temp;
        }


    }
3、从n个元素的数组中随机选出m个数，要求每个元素被选出的概率相等。

4、编写一个方法，数出0到n（含）中数字2出现的几次


5、一个单词数组，给定两个任意的两个单词，找出这个数组中这两个单词的最短距离（相隔几个单词）

思路：遍历时记下最后看见lastWord1和lastWord2的位置
当碰到Word1的时候那它和lastWord2比较，决定是否更新min，
同样的操作Word2

public int shortest(String[] words,String w1,String w2) {
        int min = Integer.MAX_VALUE;
        int lastw1 = -1;
        int lastw2 = -1;
        for (int i = 0;i < words.length;i++) {
            String cur = words[i];
            if (cur.equals(lastw1)) {
                lastw1 = i;
                int diff = lastw1 - lastw2;
                if (lastw2 >=0 && min > diff) {
                    min = diff;
                }
            }else if (cur.equals(lastw2)){
                lastw2 = i;
                int diff = lastw2 - lastw2;
                if (lastw1 >=0 && min > diff) {
                    min = diff;
                }
            }
        }
        return min;
    }

6、10亿数字，找出最小的100万个数。假定内存足够。
1、排序N*logN
2、堆排序，大顶堆100万，遍历插入元素到堆顶删除最大元素N*logm
3、选择排序，找到第i小元素，然后遍历就可以找到所以小于这个数的元素。（假设没有重复元素）
7、给一组单词，找出最长的单词切该单词由这组单词的其他单词组成
1、首先对单词数组按照长度进行排序，最大的在第一个
2、遍历单词放入map中
3、把单词切分成任意的两部分
4、检查是否在都在map中
分成两份的伪代码
for (String s:array) {
        for (int i = 0;i < s.length();i++) {
            String left = s.substring(0, i);
            String right = s.substring(i);
            if (map.contains(left)&& map.contains(right)) {
                return s;
            }
        }
    }


8、给定一个字符串s和一个包含较短字符串的数组T，根据T中的每一个较短字符串对S进行搜索。




中等难题

1、不用临时变量，直接交换两个数（异或）
原理：
a ^ a = 0
a ^ 0 = a

public void swap(int a ,int b) {
        a=a^b;
        b=a^b;
        a=a^b;
    }
2、井字游戏

？？？如何判断赢
3、n阶乘有多少个0
思路：计算阶乘的数中有多少个5，有多少就有多少个0

public int countfactzeros(int num) {
        int count = 0;
        for (int i=2;i <= num;i++) {
            count += factorsof5(i);
        }
        return count;
    }

    public int factorsof5(int n) {
        int count = 0;
        while (n % 5 == 0) {
            count++;
            n /= 5;
        }
        return count;
    }

优化（没明白）：数5的因数  n/5   n/25    n/125....
public int countfactzeros2(int num) {
        int count = 0;
        if (num < 0) {
            return -1;
        }
        for (int i=5;num / i > 0;i*=5) {
            count += num / i;
        }
        return count;
    }

3、找出两个数中较大的那个不得使用比较运算符和if-else
















