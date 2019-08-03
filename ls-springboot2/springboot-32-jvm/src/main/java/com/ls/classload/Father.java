package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-27 09:46
 */
public class Father {
    /* 静态变量 */
    public static String staticField = "Father静态变量";
    /* 变量 */
    public String field = "Father变量";
    /* 静态初始化块 */
    static {
        System.out.println( staticField );
        System.out.println( "Father静态初始化块" );
    }
    /* 初始化块 */
    {
        System.out.println( field );
        System.out.println( "Father初始化块" );
    }
    /* 构造器 */
    public Father() {
        System.out.println( "Father构造器" );
    }


    public static void main( String[] args ) {
        new InitialOrderTest();
    }
}
