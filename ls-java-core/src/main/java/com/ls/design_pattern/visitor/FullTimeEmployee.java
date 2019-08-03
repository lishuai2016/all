package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:30
 */
public class FullTimeEmployee implements Employee {
    public String name;//姓名
    public double weeklyWage;//工资
    public int workTime;//工作时间

    public FullTimeEmployee(String name,double weeklyWage,int workTime) {
        this.name=name;
        this.weeklyWage=weeklyWage;
        this.workTime=workTime;
    }

    @Override
    public void Accept(Department handler) {
        handler.Visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeeklyWage() {
        return weeklyWage;
    }

    public void setWeeklyWage(double weeklyWage) {
        this.weeklyWage = weeklyWage;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
}
