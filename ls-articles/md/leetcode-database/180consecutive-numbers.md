---
title: consecutive-numbers
categories: 
- leetCode
tags:
- database
---


https://leetcode.com/problems/consecutive-numbers/


Write a SQL query to find all numbers that appear at least three times consecutively.

+----+-----+
| Id | Num |
+----+-----+
| 1  |  1  |
| 2  |  1  |
| 3  |  1  |
| 4  |  2  |
| 5  |  1  |
| 6  |  2  |
| 7  |  2  |
+----+-----+
For example, given the above Logs table, 1 is the only number that appears consecutively for at least three times.

+-----------------+
| ConsecutiveNums |
+-----------------+
| 1               |
+-----------------+



借助于id的递增关系
SELECT DISTINCT L1.Num as ConsecutiveNums
FROM Logs L1, Logs L2, Logs L3
WHERE L2.Id = L1.Id + 1
AND L3.Id = L2.Id + 1
AND L1.Num = L2.Num
AND L2.Num = L3.Num

也可以使用JOIN子句完成同样的功能：

SELECT DISTINCT L1.Num 
FROM Logs L1
JOIN Logs L2 ON L1.Id + 1 = L2.Id
JOIN Logs L3 ON L1.Id + 2 = L3.Id
WHERE L1.Num = L2.Num AND L1.Num = L3.Num
ORDER BY L1.Num

上面两种方法可以用于找到至少三次连续出现的数字，如果将连续出现的数字扩展到N个，按照上面思路写出的SQL语句就会比较长。因此可以用下面这种方式来查询：

SELECT DISTINCT Num
FROM (
  SELECT Num, 
    CASE 
      WHEN @prev = Num THEN @count := @count + 1
      WHEN (@prev := Num) IS NOT NULL THEN @count := 1
    END CNT
  FROM Logs, (SELECT @prev := NULL) X
  ORDER BY Id
) AS A
WHERE A.CNT >= 3

将最后一行的3改为N，即可用于查询至少N次连续出现的数字。


有待研究下












