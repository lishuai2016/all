# MySQL查询语句执行顺序
[参考](https://blog.csdn.net/l1394049664/article/details/81603013)


总结：优先进行筛选条件join-condition和where-condition，然后对返回的列字段进行筛选，最后对返回数据进行排序并限制返回的条数。


MySQL的语句一共分为11步，如下图所标注的那样，最先执行的总是FROM操作，最后执行的是LIMIT操作。其中每一个操作都会产生一张虚拟的表，这个虚拟的表作为一个处理的输入，只是这些虚拟的表对用户来说是透明的，但是只有最后一个虚拟的表才会被作为结果返回。如果没有在语句中指定某一个子句，那么将会跳过相应的步骤。
![](../../pic/mysql执行顺序.png)


下面我们来具体分析一下查询处理的每一个阶段
FORM: 对FROM的左边的表和右边的表计算笛卡尔积。产生虚表VT1
ON: 对虚表VT1进行ON筛选，只有那些符合<join-condition>的行才会被记录在虚表VT2中。（即内连接查询，完全匹配）
JOIN： 如果指定了OUTER JOIN（比如left join、 right join），那么保留表中未匹配的行就会作为外部行添加到虚拟表VT2中，产生虚拟表VT3, 如果from子句中包含两个以上的表的话，那么就会对上一个join连接产生的结果VT3和下一个表重复执行步骤1~3这三个步骤，一直到处理完所有的表为止。
WHERE： 对虚拟表VT3进行WHERE条件过滤。只有符合<where-condition>的记录才会被插入到虚拟表VT4中。
GROUP BY: 根据group by子句中的列，对VT4中的记录进行分组操作，产生VT5.
CUBE | ROLLUP: 对表VT5进行cube或者rollup操作，产生表VT6.
HAVING： 对虚拟表VT6应用having过滤，只有符合<having-condition>的记录才会被 插入到虚拟表VT7中。
SELECT： 执行select操作，选择指定的列，插入到虚拟表VT8中。
DISTINCT： 对VT8中的记录进行去重。产生虚拟表VT9.
ORDER BY: 将虚拟表VT9中的记录按照<order_by_list>进行排序操作，产生虚拟表VT10.
LIMIT：取出指定行的记录，产生虚拟表VT11, 并将结果返回。


