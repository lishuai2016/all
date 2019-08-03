package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:59
 */
public class Program {

    public static void main (String[] args) {
        EmployeeList empList = new EmployeeList();
        Employee fteA = new FullTimeEmployee("梁思成", 3200.00, 45);
        Employee fteB = new FullTimeEmployee("徐志摩", 2000, 40);
        Employee fteC = new FullTimeEmployee("梁徽因", 2400, 38);
        Employee fteD = new PartTimeEmployee("方鸿渐", 80, 20);
        Employee fteE = new PartTimeEmployee("唐宛如", 60, 18);

        empList.AddEmployee(fteA);
        empList.AddEmployee(fteB);
        empList.AddEmployee(fteC);
        empList.AddEmployee(fteD);
        empList.AddEmployee(fteE);

        //在这里来切换访问者
        Department dept = new FinanceDepartment();
        //Department dept = new HRDepartment();
        if (dept != null) {
            empList.accept(dept);
        }
    }
}
