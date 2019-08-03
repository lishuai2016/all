---
title: group by解析
categories: 
- mysql
tags:
---

[参考](https://blog.csdn.net/qq_26442553/article/details/80867076)
[参考](https://blog.csdn.net/qq_20597149/article/details/80578137)


目录
1、为什么group by后面不能使用列的别名
2、group by多字段的顺序不影响结果，只影响结果出现的顺序
3、group by需要注意的事项



# 为什么group by后面不能使用列的别名
一个问题：
select count(billingdate),to_char(billingdate,'YYYYmm') month  from tu_trade
where to_char(billingdate,'YYYY') ='2017'and reportstat = 30
group by month; 
-----执行报错，can't resolve month............

因为Sql语句执行顺序：
(7)    SELECT
(8)    DISTINCT <select_list>
(1)    FROM <left_table>
(3)    <join_type> JOIN <right_table>
(2)    ON <join_condition>
(4)    WHERE <where_condition>
(5)    GROUP BY <group_by_list>
(6)    HAVING <having_condition>
(9)    ORDER BY <order_by_condition>
(10)   LIMIT <limit_number> 

Group by不能用别名的原因，因为执行到groupby（5）时，还没执行到select中的别名，所以别名还没生效。
所以别名只能放到（7）之后，比如order中，distinct中。
遇到这种问题可以使用子查询替代
SELECT
	MONTH,
	count(MONTH)
FROM
	(
		select
		count (billingdate),
		to_char (billingdate, 'YYYYmm')   AS MONTH
	FROM
		tu_trade 
		where to_char (billingdate, 'YYYY') = '2017'
	AND reportstat = 30
	) a
GROUP BY MONTH

[注意]
在mysql中，group by中可以使用别名；where中不能使用别名；order by中可以使用别名。
其余像oracle，hive中别名的使用都是严格遵循sql执行顺序的，groupby后面不能用别名。mysql特殊是因为mysql中对查询做了加强。


# group by多字段的顺序不影响结果，只影响结果出现的顺序
group by 后面的字段顺序只是影响了结果的顺序，不会影响结果的值。
如果是 group by a,b那么就是按照 order by a,b 的顺序分组，因为分组是需要先排序的；反之 group by b,a 就是按照b,a的顺序分组

测试
```sql
#建表
CREATE TABLE tb(col1 INT,col2 INT,col3 INT)

#插入数据
INSERT tb
SELECT 1,3,5 UNION ALL
SELECT 1,3,5 UNION ALL
SELECT 2,1,8 UNION ALL
SELECT 2,1,8 UNION ALL
SELECT 3,2,3 UNION ALL
SELECT 3,2,3 UNION ALL
SELECT 4,0,NULL UNION ALL
SELECT 4,0,NULL

#表中的数据
SELECT * FROM tb
1	3	5
1	3	5
2	1	8
2	1	8
3	2	3
3	2	3
4	0	
4	0	



# 不同字段顺序分组结果分析
SELECT * FROM tb GROUP BY col1 ,col2 ,col3
SELECT * FROM tb GROUP BY col2 ,col3 ,col1
SELECT * FROM tb GROUP BY col3 ,col2 ,col1

# 结果1
1	3	5
2	1	8
3	2	3
4	0	
# 结果2
4	0	
2	1	8
3	2	3
1	3	5
# 结果3
4	0	
3	2	3
1	3	5
2	1	8


 
SELECT * FROM tb GROUP BY col1 ,col2 ,col3 ORDER BY col1 ,col2 ,col3
SELECT * FROM tb GROUP BY col2 ,col3 ,col1 ORDER BY col1 ,col2 ,col3
SELECT * FROM tb GROUP BY col3 ,col2 ,col1 ORDER BY col1 ,col2 ,col3
# 上面三条SQL得到的是同样的结果
1	3	5
2	1	8
3	2	3
4	0	

```

# group by需要注意的事项
在SQL中使用GROUP BY来对SELECT的结果进行数据分组，在具体使用GROUP BY之前需要知道一些重要的规定。
(1)GROUP BY子句可以包含任意数目的列。也就是说可以在组里再分组，为数据分组提供更细致的控制。
(2)如果在GROUP BY子句中指定多个分组，数据将在最后指定的分组上汇总。
(3)GROUP BY子句中列出的每个列都必须是检索列或有效的表达式（但不能是聚集函数）。如果在SELECT中使用了表达式，则必须在GROUP BY子句中指定相同的表达式。不能使用别名。
(4)除了聚集计算语句外，SELECT语句中的每一列都必须在GROUP BY子句中给出。
(5)如果分组列中有NULL值，则NULL将作为一个分组返回。如果有多行NULL值，它们将分为一组。
(6)GROUP BY子句必须在WHERE子句之后，ORDER BY之前。

## 过滤分组
对分组过于采用HAVING子句。HAVING子句支持所有WHERE的操作。HAVING与WHERE的区别在于WHERE是过滤行的，而HAVING是用来过滤分组。
另一种理解WHERE与HAVING的区别的方法是，WHERE在分组之前过滤，而HAVING在分组之后以每组为单位过滤。

## 分组与排序
一般在使用GROUP BY子句时，也应该使用ORDER BY子句。这是保证数据正确排序的唯一方法。