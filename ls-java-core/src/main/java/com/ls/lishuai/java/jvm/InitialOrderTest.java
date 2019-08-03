package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/23 9:54
 */
public class InitialOrderTest {
    static {

        System.out.println( "静态初始化块1111" );
    }
    /* 静态变量 */
    public static String staticField = "静态变量";
    {

        System.out.println( "初始化块22222222222" );
    }

    /* 变量 */
    public String field = "变量";
    public float f;
    /* 静态初始化块 */
    static {
        System.out.println( staticField );
        System.out.println( "静态初始化块" );
    }
    /* 初始化块 */
    {
        System.out.println( field );
        System.out.println( f );
        System.out.println( "初始化块" );
    }
    /* 构造器 */
    public InitialOrderTest()
    {
        System.out.println( "构造器" );
        System.out.println( f );
    }
    {

        System.out.println( "初始化块2333333333333333" );
    }

    public static void main( String[] args )
    {
        new InitialOrderTest();
    }
}
