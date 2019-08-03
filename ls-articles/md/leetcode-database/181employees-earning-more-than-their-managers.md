---
title: employees-earning-more-than-their-managers
categories: 
- leetCode
tags:
- database
---

https://leetcode.com/problems/employees-earning-more-than-their-managers/

schema 概要

The Employee table holds all employees including their managers. Every employee has an Id, and there is also a column for the manager Id.

+----+-------+--------+-----------+
| Id | Name  | Salary | ManagerId |
+----+-------+--------+-----------+
| 1  | Joe   | 70000  | 3         |
| 2  | Henry | 80000  | 4         |
| 3  | Sam   | 60000  | NULL      |
| 4  | Max   | 90000  | NULL      |
+----+-------+--------+-----------+
Given the Employee table, write a SQL query that finds out employees who earn more than their managers. For the above table, Joe is the only employee who earns more than his manager.

+----------+
| Employee |
+----------+
| Joe      |
+----------+

思路：使用一个join自关联即可
select a.Name as Employee from Employee a join Employee b on a.ManagerId = b.Id and a.Salary > b.Salary



