---
title: nth-highest-salary
categories: 
- leetCode
tags:
- database
---

https://leetcode.com/problems/nth-highest-salary/


Write a SQL query to get the nth highest salary from the Employee table.

+----+--------+
| Id | Salary |
+----+--------+
| 1  | 100    |
| 2  | 200    |
| 3  | 300    |
+----+--------+
For example, given the above Employee table, the nth highest salary where n = 2 is 200. If there is no nth highest salary, then the query should return null.

+------------------------+
| getNthHighestSalary(2) |
+------------------------+
| 200                    |
+------------------------+




CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
      # Write your MySQL query statement below.
      SELECT
    	IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT  N-1 , 1),
    NULL) AS getNthHighestSalary(N)
  );
END


{"headers": {"Employee": ["Id", "Salary"]}, "argument": 2, "rows": {"Employee": [[1, 100], [2, 200], [3, 300]]}}
Output
{"headers":["getNthHighestSalary(2)"],"values":[[100]]}
Expected
{"headers":["getNthHighestSalary(2)"],"values":[[200]]}


知识点：
自定义函数 (user-defined function UDF)








