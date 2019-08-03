package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:25
 */
public interface Employee {
    //接受部门的访问
    void Accept(Department handler);
}
