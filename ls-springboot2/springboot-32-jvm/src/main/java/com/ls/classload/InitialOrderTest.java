package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * 输出：
第一种情况：主函数调用： new InitialOrderTest();
静态变量
静态初始化块
变量
初始化块
构造器

结论：
1、类的静态代码块要是使用了类的静态变量，那么静态变量的声明必须在代码块使用之前；
2、同样普通代码块使用了普通的类变量，那么类变量也必须在普通代码块前声明；
3、普通代码块可以引用静态的类变量，而静态代码块不能引用普通的类变量；

第二种情况：主函数调用：  System.out.println( "main" );
静态变量
静态初始化块
main

说明：根据输出来看，只进行了类的加载，而没有进行类的实例化，没有调用构造器；

第三种情况：主函数调用：  System.out.println( "main" );
静态变量
静态初始化块
staticFunction1

说明：依旧没有调用构造器实例化对象；


 */
public class InitialOrderTest {
    /* 静态变量 */
    public static String staticField = "静态变量";
    /* 变量 */
    public String field = "变量";
    /* 静态初始化块 */
    static {
        System.out.println( staticField );
        System.out.println( "静态初始化块" );
    }
    /* 初始化块 */
    {
        System.out.println( field );
        System.out.println( "初始化块" );
    }
    /* 构造器 */
    public InitialOrderTest() {
        System.out.println( "构造器" );
    }
    public static void staticFunction1(){
        System.out.println("staticFunction1");
    }

    public static void main( String[] args ) {
         //new InitialOrderTest();
        //System.out.println( "main" );
        staticFunction1();
    }

}
