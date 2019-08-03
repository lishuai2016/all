package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:49
 *
 *
 * 接口继承的初始化：通过引用 static 字段，触发某个接口的初始化，
 * 则声明该字段的接口会被初始化，但该接口的父接口不会被初始化。
 * 注意一点，接口字段全部隐式地被修饰为 public, static, final 。
 * 因此，所有的接口字段实际上都是 static 的，无论有没有显示地声明 static 。这点和接口无法被实例化规定是相吻合的。
 *
 */
public interface I extends SuperI{
    public static String iField = Output.printWhenInit("initializing fdsafasf I.iField ");
}
