package com.ls.design_pattern.visitor;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 10:38
 *
 * 考核员工的工作时长
 */
public class HRDepartment extends Department {

    @Override
    public void Visit(FullTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        System.out.println(String.format("正式员工 [%s] 实际工作时间为：[%s] 小时", employee.getName(), workTime));

        if (workTime > 40) {
            System.out.println(String.format("正式员工 [%s] 加班时间为：[%s] 小时", employee.getName(), workTime - 40));
        } else if (workTime < 40) {
            System.out.println(String.format("正式员工 [%s] 请假时间为：[%s] 小时", employee.getName(), 40 - workTime));
        }
    }

    @Override
    public void Visit(PartTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        System.out.println(String.format("临时工 [%s] 实际工作时间为：[%s] 小时", employee.getName(), workTime));
    }
}
