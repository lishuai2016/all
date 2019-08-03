---
title: delete-duplicate-emails
categories: 
- leetCode
tags:
- database
---

https://leetcode.com/problems/delete-duplicate-emails/



Write a SQL query to delete all duplicate email entries in a table named Person, keeping only unique emails based on its smallest Id.

+----+------------------+
| Id | Email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
| 3  | john@example.com |
+----+------------------+
Id is the primary key column for this table.
For example, after running your query, the above Person table should have the following rows:

+----+------------------+
| Id | Email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
+----+------------------+
Note:

Your output is the whole Person table after executing your sql. Use delete statement.



思路：找到所有重复邮箱和最大的id，然后根据id来删除
DELETE FROM Person
where 
Id in(
SELECT 
MAX(t1.Id) as Id ,t1.Email
FROM Person t1 
join Person t2
WHERE t1.Email = t2.Email
AND t1.Id != t2.Id GROUP BY t1.Email);

