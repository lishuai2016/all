package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-27 09:46
 *
 *
Father静态变量
Father静态初始化块
Son静态变量
Son静态初始化块
Father变量
Father初始化块
Father构造器
Son变量
Son初始化块
Son构造器
son main
 */
public class Son extends Father {
    /* 静态变量 */
    public static String staticField = "Son静态变量";
    /* 变量 */
    public String field = "Son变量";
    /* 静态初始化块 */
    static {
        System.out.println( staticField );
        System.out.println( "Son静态初始化块" );
    }
    /* 初始化块 */
    {
        System.out.println( field );
        System.out.println( "Son初始化块" );
    }
    /* 构造器 */
    public Son() {
        System.out.println( "Son构造器" );
    }


    public static void main( String[] args ) {
        new Son();
        System.out.println("son main");
    }
}
