package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:26
 */
public abstract class Department {

    // 声明一组重载的访问方法，用于访问不同类型的具体元素
    public abstract void Visit(FullTimeEmployee employee);
    public abstract void Visit(PartTimeEmployee employee);
}
