package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-27 09:36
 第一种情况：static StaticTest st = new StaticTest();//步骤1
输出:
2
3
a=110,b=0
1
4

 说明：从输出的结果来看
 在步骤1进行类的初始化的过程中，实例化了类,所以在类加载之前先实例化了变量st，然后继续进行类加载？？？


 第二种情况：static Other o = new Other(); //步骤1
 输出：
1
4

 符合一般的逻辑：只对类StaticTest进行了加载，而没有实例化

第三种情况： StaticTest st = new StaticTest();//步骤1
输出：
1
4

 没有实例化对象，所以没有走步骤一，只进行了类加载

 */
public class StaticTest {

    static StaticTest st = new StaticTest();//步骤1
    //StaticTest st1 = new StaticTest();//步骤1
    //static Other o = new Other(); //步骤1

    static { //步骤2
        System.out.println("1");
    }

    { //步骤3
        System.out.println("2");
    }

    StaticTest() { //步骤4
        System.out.println("3");
        System.out.println("a="+a+",b="+b);
    }

    public static void staticFunction(){ //步骤5
        System.out.println("4");
    }

    int a=110;   //步骤6
    static int b =112;      //步骤7             //位置不同影响b的值输出
    public static void main(String[] args) {
        staticFunction();
    } //步骤8
}
