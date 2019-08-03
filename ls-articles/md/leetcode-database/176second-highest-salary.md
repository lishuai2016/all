---
title: second-highest-salary
categories: 
- leetCode
tags:
- database
---

https://leetcode.com/problems/second-highest-salary/

Write a SQL query to get the second highest salary from the Employee table.

+----+--------+
| Id | Salary from  |
+----+--------+
| 1  | 100    |
| 2  | 200    |
| 3  | 300    |
+----+--------+
For example, given the above Employee table, the query should return 200 as the second highest salary. If there is no second highest salary, then the query should return null.

+---------------------+
| SecondHighestSalary |
+---------------------+
| 200                 |
+---------------------+

输入：{"headers": {"Employee": ["Id", "Salary"]}, "rows": {"Employee": [[1, 100], [2, 100]]}}
期望输出：{"headers":["SecondHighestSalary"],"values":[[null]]}
即需要进行去重处理






方案1： 
SELECT  MAX(salary) AS SecondHighestSalary
FROM    Employee
WHERE   salary NOT IN ( SELECT  MAX(salary)  FROM    Employee);

耗时：134ms

方案2：DISTINCT不能少
SELECT
    IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT 1 OFFSET 1),
    NULL) AS SecondHighestSalary;

耗时：162ms


SELECT
    IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT 1,1),
    NULL) AS SecondHighestSalary;

耗时：146ms



知识点
1、Mysql limit offset  和limit的区别
例假设数据库表student存在13条数据。
代码示例:语句1：select * from student limit 9,4语句2：slect * from student limit 4 offset 9// 语句1和2均返回表student的第10、11、12、13行  //语句2中的4表示返回4行，9表示从表的第十行开始

2、MySQL IFNULL()函数用法
IFNULL(expr1,expr2)
如果 expr1 不是 NULL，IFNULL() 返回 expr1，否则它返回 expr2。
IFNULL()返回一个数字或字符串值，取决于它被使用的上下文环境。
在方案二中是如何通过IFNULL区分0条符合条件的记录和NULL的？？？





下面使用在case when中直接判断 IS NULL 不行，返回的是，没有符合条件的记录
SELECT  
case when Salary IS NULL THEN NULL else Salary end 
AS SecondHighestSalary
FROM    Employee
group by  Salary order by   Salary desc limit 1,1;


MySQL的空值和null http://www.ywnds.com/?p=10295