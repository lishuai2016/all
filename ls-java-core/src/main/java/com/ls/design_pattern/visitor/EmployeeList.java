package com.ls.design_pattern.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:29
 */
public class EmployeeList {

    private List<Employee> employees = new ArrayList<>();

    public void AddEmployee(Employee employee) {
        employees.add(employee);
    }

    public void accept(Department handler) {
        for (Employee e :employees) {
            e.Accept(handler);
        }
    }

}
