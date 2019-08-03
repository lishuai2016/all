package com.ls.design_pattern.visitor;

import java.io.Console;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:38
 *
 * 计算工资功能
 */
public class FinanceDepartment extends Department {
    @Override
    public void Visit(FullTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        double weekWage = employee.getWeeklyWage();

        if (workTime > 40) {
            weekWage = weekWage + (workTime - 40) * 50;
        } else if (workTime < 40){
            weekWage = weekWage - (40 - workTime) * 80;
            if (weekWage < 0) {
                weekWage = 0;
            }
        }

        System.out.println(String.format("正式员工[%s] 实际工资为：[%s] 元", employee.getName(),  weekWage));
    }


    // 实现财务部对兼职员工数据的访问
    @Override
    public void Visit(PartTimeEmployee employee) {

        int workTime = employee.getWorkTime();
        double hourWage = employee.getHourWage();
        System.out.println(String.format("临时工 [%s] 实际工资为：[%s] 元", employee.getName(), workTime * hourWage));
    }
}
