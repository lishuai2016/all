---
title: duplicate-emails
categories: 
- leetCode
tags:
- database
---

https://leetcode.com/problems/duplicate-emails/


Write a SQL query to find all duplicate emails in a table named Person.

+----+---------+
| Id | Email   |
+----+---------+
| 1  | a@b.com |
| 2  | c@d.com |
| 3  | a@b.com |
+----+---------+
For example, your query should return the following for the above table:

+---------+
| Email   |
+---------+
| a@b.com |
+---------+
Note: All emails are in lowercase.



借助于id来区分是否是重复元素
select  distinct a.Email as Email from Person a join Person b on a.Email=b.Email and a.Id != b.Id

