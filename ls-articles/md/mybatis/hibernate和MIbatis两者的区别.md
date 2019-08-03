# Hibernate/Ibatis两者的区别

Hibernate与Mybatis对比总结
两者相同点
- Hibernate与MyBatis都可以是通过SessionFactoryBuider由XML配置文件生成SessionFactory，然后由SessionFactory 生成Session，最后由Session来开启执行事务和SQL语句。其中SessionFactoryBuider，SessionFactory，Session的生命周期都是差不多的。
- Hibernate和MyBatis都支持JDBC和JTA事务处理。

Mybatis优势
- MyBatis可以进行更为细致的SQL优化，可以减少查询字段。
- MyBatis容易掌握，而Hibernate门槛较高。

Hibernate优势
- Hibernate的DAO层开发比MyBatis简单，Mybatis需要维护SQL和结果映射。
- Hibernate对对象的维护和缓存要比MyBatis好，对增删改查的对象的维护要方便。
- Hibernate数据库移植性很好，MyBatis的数据库移植性不好，不同的数据库需要写不同SQL。
- Hibernate有更好的二级缓存机制，可以使用第三方缓存。MyBatis本身提供的缓存机制不佳。

他人总结
- Hibernate功能强大，数据库无关性好，O/R映射能力强，如果你对Hibernate相当精通，而且对Hibernate进行了适当的封装，那么你的项目整个持久层代码会相当简单，需要写的代码很少，开发速度很快，非常爽。
- Hibernate的缺点就是学习门槛不低，要精通门槛更高，而且怎么设计O/R映射，在性能和对象模型之间如何权衡取得平衡，以及怎样用好Hibernate方面需要你的经验和能力都很强才行。
- iBATIS入门简单，即学即用，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验，对于没有那么高的对象模型要求的项目来说，相当完美。
- iBATIS的缺点就是框架还是比较简陋，功能尚有缺失，虽然简化了数据绑定代码，但是整个底层数据库查询实际还是要自己写的，工作量也比较大，而且不太容易适应快速数据库修改。





1、Hibernate偏向于对象的操作达到数据库相关操作的目的；而ibatis更偏向于sql语句的优化。

2、Hibernate的使用的查询语句是自己的hql，而ibatis则是标准的sql语句。

3、Hibernate相对复杂，不易学习；ibatis类似sql语句，简单易学。

性能方面：

1、如果系统数据处理量巨大，性能要求极为苛刻时，往往需要人工编写高性能的sql语句或存错过程，此时ibatis具有更好的可控性，因此性能优于Hibernate。

2、同样的需求下，由于hibernate可以自动生成hql语句，而ibatis需要手动写sql语句，此时采用Hibernate的效率高于ibatis。

优化Hibernate所鼓励的7大措施
1.尽量使用many-to-one，避免使用单项one-to-many
2.灵活使用单向one-to-many
3.不用一对一，使用多对一代替一对一
4.配置对象缓存，不使用集合缓存
5.一对多使用Bag 多对一使用Set
6.继承使用显示多态 HQL:from object polymorphism="exlicit" 避免查处所有对象
7.消除大表，使用二级缓存
