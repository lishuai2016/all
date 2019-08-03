package com.ls.lishuai.java.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/18 20:51
 */
public interface SubI extends I {
    public static String subField = Output.printWhenInit(" initializing SubI.subField ");
}
