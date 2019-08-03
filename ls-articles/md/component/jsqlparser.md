jsqlparser


# 1、简介

SQL语法解释器jsqlparser 是用java开发的解析器,可以生成java类层次结构。采用的是观察者模式，Visitor。

JSqlPaser将所有的SQL语句抽象为Statement，Statement表示对数据库的一个操作。

Abstract Syntax Tree, AST 抽象的语法树

JavaCC是java的compiler compiler。JavaCC是LL解析器生成器，可处理的语法范围比较狭窄，但支持无限长的token超前扫描。





# 2、SQL语句（statement）

```
/**
 * An operation on the db (SELECT, UPDATE ecc.)
 */
public interface Statement {

    void accept(StatementVisitor statementVisitor);
}

```


常见的Statement有：Select,Create,Drop,Insert,Delete等，它们作为Statement实现类，均实现accept方法。这是Visitor模式的典型应用，贯穿JSqlParser解析SQL语句的每个角落。这里你只需要知道Statement对应StatementVisitor。如果要针对SQL语句进行定制化处理，你只需实现StatementVisitor接口即可。

```
public class Select implements Statement {

    private SelectBody selectBody;
    private List<WithItem> withItemsList;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

}
```

可以看到，Select对象有两个重要的成员：SelectBody,List<WithItem>，其中WithItem对应SQL语句的with关键字，并不多见。可见常用Select语句的重点在SelectBody。SelectBody是一个接口，


```
public interface SelectBody {

    void accept(SelectVisitor selectVisitor);
}
```

又是一个Visitor，只不过这里换成了SelectVisitor。即针对Select语句的访问者。如果想定制化解析Select语句，可以实现该接口。

Select语句进一步细分，大致可发表示如下：

select    SelectItem   from   FromItem   where   Expression

```
/**
 * Anything between "SELECT" and "FROM"<BR>
 * (that is, any column or expression etc to be retrieved with the query)
 */
public interface SelectItem {

    void accept(SelectItemVisitor selectItemVisitor);
}



FromItem表示数据来源（表或者嵌入选择语句）
/**
 * An item in a "SELECT [...] FROM item1" statement. (for example a table or a
 * sub-select)
 */
public interface FromItem {

    void accept(FromItemVisitor fromItemVisitor);

    Alias getAlias();

    void setAlias(Alias alias);

    Pivot getPivot();

    void setPivot(Pivot pivot);

}
可以看到，SelectItem解析的时候会用到SeletItemVisitor，FromItem解析的时候会用到FromItemVisitor，模式都是相同的。
Expression稍微复杂一些，下面单独介绍。

```


# 3、表达式（expression）

SQL解析过程中， 条件的解析最为复杂。JSqlParser把where 与order by 之间的条件表达式抽象有Exception。

```
public interface Expression {

    void accept(ExpressionVisitor expressionVisitor);
}
```


哈哈，又看到一个ExpressionVisitor，是不是觉得JSqlParser的思路还蛮简洁的。
Expression进一步细分成好多种，常见的有：

> 1、条件表达式

如：AndExpression（and），OrExpression(or)

> 2、关系表达式

如：EqualsTo(=)，MinorThan(<)，GreaterThan(>)，……

> 3、算术表达式

如：Addition（+），Subtraction（-），Multiplication（*）,Division(/),……

> 4、列表达式

如：Column

> 5、case表达式

如：CaseExpression

> 6、值表达式

如：StringValue，DateValue,LongValue,DoubleValue,……

> 7、函数表达式

如：Function

> 8、参数表达式

如：JdbcParameter，JdbcNameParameter,……

如果要定制ExpressionVisitor，针对上面不同的表达式，应该给出相应的处理。


# 4、访问者（Visitor）

上面已经提到，常用的Visitor有StatementVisitor,SelectVisitor,ExpressionVisitor,SelectItemVisitor,FromItemVisitor等，定制化解析具体某一块SQL语句时，需要定制相关的Visitor。






# 5、源码解析


```
public interface Expression extends ASTNodeAccess {
    void accept(ExpressionVisitor var1);
}


public interface ExpressionVisitor {
	 void visit(AndExpression var1);

    void visit(OrExpression var1);
    ...

}


public class AndExpression extends BinaryExpression {
    public AndExpression(Expression leftExpression, Expression rightExpression) {
        this.setLeftExpression(leftExpression);
        this.setRightExpression(rightExpression);
    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String getStringExpression() {
        return "AND";
    }
}


从上面三个之间的关系可以看出，被访问者调用Expression.accept 通过访问者的visit来实现最终的逻辑。



如果在访问者内部调用xxxx.accept() 就可以实现通过访问者模式的回调，间接调用自己的visit函数进行数据的处理



public class TablesNamesFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {
    private static final String NOT_SUPPORTED_YET = "Not supported yet.";
    private List<String> tables;
    private boolean allowColumnProcessing = false;
    private List<String> otherItemNames;

    public TablesNamesFinder() {
    }

    public List<String> getTableList(Statement statement) {
        this.init(false);
        statement.accept(this);  这里把自己作为访问者调用，间接调用下面的一系列visit函数实现获得表名称
        return this.tables;
    }

    public void visit(Select select) {
        if (select.getWithItemsList() != null) {
            Iterator var2 = select.getWithItemsList().iterator();

            while(var2.hasNext()) {
                WithItem withItem = (WithItem)var2.next();
                withItem.accept(this);
            }
        }

        select.getSelectBody().accept(this);
    }

...
}

访问者模式
Expression的实现类为被访问者
ExpressionVisitor 接口的实现类为访问者，根据访问者的方法来获取信息

```
















# 相关文章

- [JSqlParser系列之二代码结构（原）](https://www.cnblogs.com/liuwt0911/p/4420472.html)
- [数据权限实现(Mybatis拦截器+JSqlParser)](https://blog.csdn.net/hzh1234565/article/details/70226248)
- [JSqlParser](https://github.com/JSQLParser/JSqlParser)
- [JSqlParser wiki](https://github.com/JSQLParser/JSqlParser/wiki)
- [设计模式的征途—16.访问者（Visitor）模式](https://www.cnblogs.com/edisonchou/p/7247990.html)
- [JSqlParser系列之一源代码运行（原）](https://www.cnblogs.com/liuwt0911/p/4417909.html#4045523)
- [Druid SQL 解析器](https://www.jianshu.com/p/437aa22ea3ca)






