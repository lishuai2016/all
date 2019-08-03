package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:30
 */
public class PartTimeEmployee implements Employee {

    public String name;//姓名
    public double hourWage ;//工资
    public int workTime;//工作时间

    public PartTimeEmployee(String name,double hourWage,int workTime){
        this.name=name;
        this.hourWage=hourWage;
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

    public double getHourWage() {
        return hourWage;
    }

    public void setHourWage(double hourWage) {
        this.hourWage = hourWage;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
}
